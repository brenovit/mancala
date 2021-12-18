package com.bol.brenovit.mancala.core.game.service.game;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.bol.brenovit.mancala.core.common.error.ErrorCode;
import com.bol.brenovit.mancala.core.common.exception.ResourceNotFoundException;
import com.bol.brenovit.mancala.core.common.mapper.CustomObjectMapper;
import com.bol.brenovit.mancala.core.game.dto.Board;
import com.bol.brenovit.mancala.core.game.dto.Game;
import com.bol.brenovit.mancala.core.game.dto.GameConfiguration;
import com.bol.brenovit.mancala.core.game.dto.GameCreateTemplate;
import com.bol.brenovit.mancala.core.game.dto.GamePlayer;
import com.bol.brenovit.mancala.core.game.dto.Hole;
import com.bol.brenovit.mancala.core.game.exception.GameErrorException;
import com.bol.brenovit.mancala.core.game.service.board.BoardService;
import com.bol.brenovit.mancala.core.game.service.board.HoleService;
import com.bol.brenovit.mancala.core.player.Player;
import com.bol.brenovit.mancala.gateway.game.message.GameMessaging;
import com.bol.brenovit.mancala.gateway.game.repository.GameRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class GameService {

    private HoleService holeService;
    private BoardService boardService;

    private GameRepository gameRepository;
    private GameConfigurationService gameConfigurationService;

    private GameMessaging gameMessaging;

    public List<Game> findAll() {
        return gameRepository.findAll();
    }

    public Game create(GameCreateTemplate gameTemplate) {
        GamePlayer player1 = CustomObjectMapper.map(gameTemplate.getPlayer(), GamePlayer.class);
        Board board = new Board();
        GameConfiguration config = gameTemplate.getSeeds() > 0
                ? gameConfigurationService.getDefaultHole(gameTemplate.getSeeds())
                : gameConfigurationService.getDefaultConfig();
        Game game = new Game(gameTemplate.getName(), player1, board, config);
        game.waiting();
        Game savedGame = gameRepository.save(game);

        gameMessaging.post(savedGame);

        return savedGame;
    }

    public Game join(String gameId, Player player) {
        Game game = findById(gameId);
        if (game.hasPlayer(player.getId())) {
            return game;
        }

        if (!game.isWaiting()) {
            throw new GameErrorException(ErrorCode.GAME_FULL);
        }

        GamePlayer player2 = new GamePlayer(player.getId(), player.getName());

        game.addPlayer(player2);
        game.ready();

        Game savedGame = gameRepository.save(game);

        gameMessaging.post(savedGame);

        return savedGame;
    }

    public Game start(String gameId) {
        Game game = findById(gameId);
        if (!game.isReady()) {
            throw new GameErrorException(ErrorCode.GAME_NOT_READY);
        }
        game = boardService.fillBoadHoles(game);
        game.start();
        Game savedGame = gameRepository.save(game);

        gameMessaging.post(savedGame);

        return savedGame;
    }

    public Game abandon(String gameId, String playerId) {
        Game game = findById(gameId);        
        if(game.hasWatcher(playerId)) {
            game.removeWatcher(playerId);
            game = gameRepository.save(game);
            return game; 
        }
        if (!game.isStarted()) {
            throw new GameErrorException(ErrorCode.GAME_NOT_STARTED);
        }
        if (!game.hasPlayer(playerId)) {
            throw new GameErrorException(ErrorCode.GAME_NOT_PLAYER);
        }
        Optional<GamePlayer> player = game.findPlayerById(playerId);
        game.setMessage(ErrorCode.GAME_ABANDONED.getFormatedMessage(player.get().getName()));
        game.finished();
        game = gameRepository.save(game);
        gameMessaging.post(game);
        return game;
    }

    public Game spectate(String gameId, Player watcher) {
        GamePlayer gameWatcher = CustomObjectMapper.map(watcher, GamePlayer.class);
        Game game = findById(gameId);
        game.addWatcher(gameWatcher);
        game = gameRepository.save(game);
        return game;
    }

    public Game makeAMove(String gameId, int holePosition, String playerId) {
        Game game = findById(gameId);

        validateGame(game, playerId);

        GamePlayer player = game.getTurn();

        boolean isACompleteTurn = isACompleteTurn(holePosition, game);

        Hole lastHole = sow(holePosition, game);

        try {
            if (holeService.isAnySideWithAllPitHolesEmpty(game.getBoard())) {
                holeService.moveAllOpponentSeedsFromPitToPot(game.getBoard(), player);
                List<Hole> potHoles = holeService.getPotsWithLargestsAmountOfSeeds(game.getBoard());
                if (potHoles.size() > 1) {
                    Hole drawnHole = potHoles.get(0);
                    String drawnNames = potHoles
                            .stream()
                            .map(Hole::getOwnerName)
                            .collect(Collectors.joining(", "));
                    game.setMessage(ErrorCode.GAME_FINISH_DRAWN.getFormatedMessage(drawnNames,
                            String.valueOf(drawnHole.getSeeds()))); 
                } else {
                    Hole winnerHole = potHoles.get(0);
                    game.setMessage(ErrorCode.GAME_FINISH_WINNER.getFormatedMessage(winnerHole.getOwnerName(),
                            String.valueOf(winnerHole.getSeeds())));
                }
                game.finished();
            } else {

                if (isACaptureMovement(isACompleteTurn, lastHole, player)) {
                    makeACaptureMove(lastHole, game);
                    game.swapTurn();
                } else if (lastHole.isPit()) {
                    game.swapTurn();
                }
            }
        } finally {
            game = gameRepository.save(game);
            gameMessaging.post(game);
        }

        return game;
    }

    public Game findById(String gameId) {
        return gameRepository.findById(gameId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.GAME_NOT_FOUND));
    }

    private void validateGame(Game game, String playerId) {
        if (!game.isStarted()) {
            throw new GameErrorException(ErrorCode.GAME_NOT_STARTED);
        }
        if (!game.hasPlayer(playerId)) {
            throw new GameErrorException(ErrorCode.GAME_NOT_PLAYER);
        }
        if (!game.isTurn(playerId)) {
            throw new GameErrorException(ErrorCode.GAME_NOT_TURN);
        }
    }

    private boolean isACompleteTurn(int holePosition, Game game) {
        return holeService.isACompleteTurn(holePosition, game.getBoard(), game.getTurn());
    }

    private Hole sow(int holePosition, Game game) {
        return holeService.distributeHoleSeedsAcrossBoardHoles(holePosition, game.getBoard(), game.getTurn());
    }

    private boolean isACaptureMovement(boolean isACompleteTurn, Hole lastHole, GamePlayer player) {
        return isACompleteTurn &&
                lastHole.isOwner(player) &&
                lastHole.getSeeds() == 1 &&
                lastHole.isPit();
    }

    private void makeACaptureMove(Hole lastHole, Game game) {
        holeService.captureOpponentOppositeHoleSeeds(lastHole.getPosition(), game.getBoard(), game.getTurn());
        holeService.moveAllHoleSeedsFromPitToPot(lastHole.getPosition(), game.getBoard(), game.getTurn());
    }
}
