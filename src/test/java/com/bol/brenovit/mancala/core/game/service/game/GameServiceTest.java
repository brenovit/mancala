package com.bol.brenovit.mancala.core.game.service.game;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import com.bol.brenovit.mancala.core.game.dto.Board;
import com.bol.brenovit.mancala.core.game.dto.Game;
import com.bol.brenovit.mancala.core.game.dto.GameCreateTemplate;
import com.bol.brenovit.mancala.core.game.dto.GamePlayer;
import com.bol.brenovit.mancala.core.game.dto.GameStatus;
import com.bol.brenovit.mancala.core.game.dto.Hole;
import com.bol.brenovit.mancala.core.game.dto.HoleType;
import com.bol.brenovit.mancala.core.player.Player;
import com.bol.brenovit.mancala.gateway.game.repository.GameRepository;

@SpringBootTest
class GameServiceTest {

    @SpyBean
    private GameRepository gameRepository;

    @Autowired
    private GameService service;

    @Test
    void shouldReturnAllGames() {
        GamePlayer player1 = new GamePlayer("1", "name1");
        Board board = new Board();
        Game expectedGame = defaultGame(player1, board);

        // given
        when(gameRepository.findAll()).thenReturn(Arrays.asList(expectedGame, expectedGame));

        // when
        List<Game> games = service.findAll();

        // then
        assertThat(games).size().isEqualTo(2);
        assertThat(games).usingRecursiveComparison().isEqualTo(Arrays.asList(expectedGame, expectedGame));
    }

    @Test
    void shouldReturnEmptyList() {
        // given
        when(gameRepository.findAll()).thenReturn(new ArrayList<Game>());

        // when
        List<Game> games = service.findAll();

        // then
        assertThat(games).isEmpty();
    }

    @Test
    void shouldReturnANewGameWithDefaultConfig() {
        // given

        GamePlayer player1 = new GamePlayer("1", "name1");
        Board board = new Board();

        Game expectedGame = new Game("gameName", player1, board, new GameConfigurationService().getDefaultConfig());
        expectedGame.waiting();

        Player player = new Player("1", "name1");
        GameCreateTemplate gameTemplate = new GameCreateTemplate("gameName", player, 0);
        // when
        Game createdGame = service.create(gameTemplate);

        // then
        assertThat(createdGame).usingRecursiveComparison().ignoringFields("id").isEqualTo(expectedGame);
        assertThat(createdGame.getId()).isNotNull();
    }
    
    @Test
    void shouldReturnANewGameWithPersonalizedConfigs() {
        // given

        GamePlayer player1 = new GamePlayer("1", "name1");
        Board board = new Board();

        Game expectedGame = new Game("gameName", player1, board, new GameConfigurationService().getDefaultHole(4));
        expectedGame.waiting();

        Player player = new Player("1", "name1");
        GameCreateTemplate gameTemplate = new GameCreateTemplate("gameName", player, 4);
        // when
        Game createdGame = service.create(gameTemplate);

        // then
        assertThat(createdGame).usingRecursiveComparison().ignoringFields("id").isEqualTo(expectedGame);
        assertThat(createdGame.getId()).isNotNull();
    }

    @Test
    void shouldThrowTheErrorGameIdEmptyWhenJoinGame() {
        // given

        String gameId = "";
        Player player = new Player("1", "name");

        // when
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> service.join(gameId, player));

        // then
        assertThat(thrown.getMessage()).isEqualTo("Game not found");
    }

    @Test
    void shouldThrowTheErrorGameNotFoundWhenJoinGame() {
        // given
        when(gameRepository.findById(anyString())).thenReturn(Optional.empty());

        String gameId = "2";
        Player player = new Player("1", "name");

        // when
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> service.join(gameId, player));

        // then
        assertThat(thrown.getMessage()).isEqualTo("Game not found");
    }
    
    @Test
    void shouldThrowTheErrorGameFulldWhenJoinNotWaitingGame() {
        // given
        GamePlayer player1 = new GamePlayer("1", "name1");
        GamePlayer player2 = new GamePlayer("2", "name2");

        Board board = new Board();

        Game game = defaultGame(player1, board);
        game.addPlayer(player2);
        game.ready();        

        when(gameRepository.findById("1")).thenReturn(Optional.of(game));

        String gameId = "1";
        Player player = new Player("3", "name");

        // when
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> service.join(gameId, player));

        // then
        assertThat(thrown.getMessage()).isEqualTo("The game is already full! Join as spector or create a new one!");
    }

    @Test
    void shouldReturnTheGameWhenPlayerPresentJoinGame() {
        // given
        GamePlayer player1 = new GamePlayer("1", "name1");
        GamePlayer player2 = new GamePlayer("2", "name2");

        Board board = new Board();

        Game game = defaultGame(player1, board);
        game.addPlayer(player2);
        game.waiting();

        when(gameRepository.findById("1")).thenReturn(Optional.of(game));

        String gameId = "1";

        Player player = new Player("1", "name");
        // when
        Game joinedGame = service.join(gameId, player);

        // then
        assertThat(joinedGame).usingRecursiveComparison().isEqualTo(game);
    }

    @Test
    void shouldJoinGame() {
        // given
        GamePlayer player1 = new GamePlayer("1", "name1");
        Board board = new Board();
        Game game = defaultGame(player1, board);
        game.setId("1");
        game.waiting();

        when(gameRepository.findById(anyString())).thenReturn(Optional.of(game));

        Game expectedGame = defaultGame(player1, board);
        GamePlayer player2 = new GamePlayer("2", "name2");
        expectedGame.addPlayer(player2);
        expectedGame.ready();
        expectedGame.setId("1");

        String gameId = "1";

        // when
        Player player = new Player("2", "name2");
        Game joinedGame = service.join(gameId, player);

        // then
        assertThat(joinedGame).usingRecursiveComparison().isEqualTo(expectedGame);
    }

    @Test
    void shouldStartGame() {
        // given
        GamePlayer player1 = new GamePlayer("1", "name1");
        GamePlayer player2 = new GamePlayer("2", "name2");
        Board board = new Board();
        Game game = defaultGame(player1, board);
        game.addPlayer(player2);
        game.setId("1");
        game.ready();

        when(gameRepository.findById(anyString())).thenReturn(Optional.of(game));

        Game expectedGame = defaultGame(player1, board);
        expectedGame.addPlayer(player2);
        expectedGame.setId("1");
        expectedGame.start();

        String gameId = "1";

        // when
        Game joinedGame = service.start(gameId);

        // then
        assertThat(joinedGame).usingRecursiveComparison().isEqualTo(expectedGame);
    }

    @Test
    void shouldThrowTheErrorGameNotReadyWhenStartGame() {
        // given
        GamePlayer player1 = new GamePlayer("1", "name1");
        Board board = new Board();
        Game game = defaultGame(player1, board);
        game.setId("1");
        game.waiting();

        when(gameRepository.findById(anyString())).thenReturn(Optional.of(game));

        String gameId = "1";

        // when
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> service.start(gameId));

        // then
        assertThat(thrown.getMessage()).isEqualTo("The game is not ready!");
    }

    @Test
    void shouldThrowTheErrorGameNotAPlayerAbandonGame() {
        // given
        GamePlayer player1 = new GamePlayer("1", "name1");
        Board board = new Board();
        Game game = defaultGame(player1, board);
        game.addPlayer(player1);
        game.setId("1");
        game.start();

        when(gameRepository.findById(anyString())).thenReturn(Optional.of(game));

        String gameId = "1";
        String playerId = "2";

        // when
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> service.abandon(gameId, playerId));

        // then
        assertThat(thrown.getMessage()).isEqualTo("You are not a player of this game!");
    }

    @Test
    void shouldThrowTheErrorGameNotStartedWhenAbandonNotStartedGame() {
        // given
        GamePlayer player1 = new GamePlayer("1", "name1");
        Board board = new Board();
        Game game = defaultGame(player1, board);
        game.addPlayer(player1);
        game.setId("1");
        game.waiting();

        when(gameRepository.findById(anyString())).thenReturn(Optional.of(game));

        String gameId = "1";
        String playerId = "1";

        // when
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> service.abandon(gameId, playerId));

        // then
        assertThat(thrown.getMessage()).isEqualTo("The game was not started yet!");
    }

    @Test
    void shouldFinishTheGameWhenPlayerLeave() {
        // given
        GamePlayer player1 = new GamePlayer("1", "name1");
        Board board = new Board();
        Game game = defaultGame(player1, board);
        game.addPlayer(player1);
        game.setId("1");
        game.start();

        when(gameRepository.findById(anyString())).thenReturn(Optional.of(game));

        String gameId = "1";
        String playerId = "1";

        // when
        Game finishedGame = service.abandon(gameId, playerId);

        // then
        assertThat(finishedGame.getMessage()).isEqualTo("The player name1 abandoned the game!");
        assertThat(finishedGame.getStatus()).isEqualTo(GameStatus.FINISHED);
    }
    
    @Test
    void shouldReturnGameAndRemoveWatcherWhenWatcherLeave() {
        // given
        GamePlayer player1 = new GamePlayer("1", "name1");
        Board board = new Board();
        Game game = defaultGame(player1, board);
        game.addPlayer(player1);
        game.setId("1");
        game.addWatcher(new GamePlayer("2", "name2"));
        game.start();

        when(gameRepository.findById(anyString())).thenReturn(Optional.of(game));

        String gameId = "1";
        String playerId = "2";

        // when
        Game finishedGame = service.abandon(gameId, playerId);

        // then        
        assertThat(finishedGame.getStatus()).isEqualTo(GameStatus.STARTED);
        assertThat(finishedGame.getWatchers()).size().isEqualTo(0);
    }

    @Test
    void shouldThrowErrorWhenIsNotThePlayerTurn() {
        // given
        GamePlayer player1 = new GamePlayer("1", "name1");
        GamePlayer player2 = new GamePlayer("2", "name2");

        Board board = new Board();

        Game game = defaultGame(player1, board);
        game.addPlayer(player2);
        game.start();

        when(gameRepository.findById(anyString())).thenReturn(Optional.of(game));

        int holePosition = 1;
        String gameId = "1";
        String playerId = "2";

        // when
        RuntimeException thrown = assertThrows(RuntimeException.class,
                () -> service.makeAMove(gameId, holePosition, playerId));

        // then
        assertThat(thrown.getMessage()).isEqualTo("It's not your turn, wait until the opponent move!");
    }

    @Test
    void shouldThrowErrorWhenGameNotStartedMakeAMove() {
        // given
        GamePlayer player1 = new GamePlayer("1", "name1");
        GamePlayer player2 = new GamePlayer("2", "name2");

        Board board = new Board();

        Game game = defaultGame(player1, board);
        game.addPlayer(player2);
        game.ready();

        when(gameRepository.findById(anyString())).thenReturn(Optional.of(game));

        int holePosition = 1;
        String gameId = "1";
        String playerId = "2";

        // when
        RuntimeException thrown = assertThrows(RuntimeException.class,
                () -> service.makeAMove(gameId, holePosition, playerId));

        // then
        assertThat(thrown.getMessage()).isEqualTo("The game was not started yet!");
    }

    @Test
    void shouldThrowErrorWhenThePlayerDoesntBelongToGame() {
        // given
        GamePlayer player1 = new GamePlayer("1", "name1");
        GamePlayer player2 = new GamePlayer("2", "name2");

        Board board = new Board();

        Game game = defaultGame(player1, board);
        game.addPlayer(player2);
        game.start();

        when(gameRepository.findById(anyString())).thenReturn(Optional.of(game));

        int holePosition = 1;
        String gameId = "1";
        String playerId = "3";

        // when
        RuntimeException thrown = assertThrows(RuntimeException.class,
                () -> service.makeAMove(gameId, holePosition, playerId));

        // then
        assertThat(thrown.getMessage()).isEqualTo("You are not a player of this game!");
    }

    @Test
    void shouldDistributeTheSeedsAccrosBoardAndChangeTheTurn() {
        // given
        GamePlayer player1 = new GamePlayer("1", "name1");
        GamePlayer player2 = new GamePlayer("2", "name2");

        Board board = new Board();
        board.addHole(Hole.builder().position(3).type(HoleType.PIT).seeds(3).owner(player1).build());
        board.addHole(Hole.builder().position(2).type(HoleType.PIT).seeds(3).owner(player1).build());
        board.addHole(Hole.builder().position(1).type(HoleType.POT).seeds(0).owner(player1).build());
        board.addHole(Hole.builder().position(3).type(HoleType.PIT).seeds(3).owner(player2).build());
        board.addHole(Hole.builder().position(2).type(HoleType.PIT).seeds(3).owner(player2).build());
        board.addHole(Hole.builder().position(1).type(HoleType.POT).seeds(0).owner(player2).build());

        Game game = defaultGame(player1, board);
        game.addPlayer(player2);
        game.setId("1");
        game.start();

        when(gameRepository.findById(anyString())).thenReturn(Optional.of(game));

        Board expectedBoard = new Board();
        expectedBoard.addHole(Hole.builder().position(3).type(HoleType.PIT).seeds(0).owner(player1).build());
        expectedBoard.addHole(Hole.builder().position(2).type(HoleType.PIT).seeds(4).owner(player1).build());
        expectedBoard.addHole(Hole.builder().position(1).type(HoleType.POT).seeds(1).owner(player1).build());
        expectedBoard.addHole(Hole.builder().position(3).type(HoleType.PIT).seeds(4).owner(player2).build());
        expectedBoard.addHole(Hole.builder().position(2).type(HoleType.PIT).seeds(3).owner(player2).build());
        expectedBoard.addHole(Hole.builder().position(1).type(HoleType.POT).seeds(0).owner(player2).build());

        int holePosition = 3;
        String gameId = "1";
        String playerId = "1";
        // when
        Game returnedGame = service.makeAMove(gameId, holePosition, playerId);

        // then
        assertThat(returnedGame.getBoard().getHoles()).usingRecursiveComparison().isEqualTo(expectedBoard.getHoles());
        assertThat(returnedGame.getTurn()).isEqualTo(player2);
    }

    @Test
    void shouldPlayer1RepeatTheTurn() {
        // given
        GamePlayer player1 = new GamePlayer("1", "name1");
        GamePlayer player2 = new GamePlayer("2", "name2");

        Board board = new Board();
        board.addHole(Hole.builder().position(3).type(HoleType.PIT).seeds(2).owner(player1).build());
        board.addHole(Hole.builder().position(2).type(HoleType.PIT).seeds(3).owner(player1).build());
        board.addHole(Hole.builder().position(1).type(HoleType.POT).seeds(0).owner(player1).build());
        board.addHole(Hole.builder().position(3).type(HoleType.PIT).seeds(3).owner(player2).build());
        board.addHole(Hole.builder().position(2).type(HoleType.PIT).seeds(3).owner(player2).build());
        board.addHole(Hole.builder().position(1).type(HoleType.POT).seeds(0).owner(player2).build());

        Game game = defaultGame(player1, board);
        game.addPlayer(player2);
        game.setId("1");
        game.start();
        when(gameRepository.findById(anyString())).thenReturn(Optional.of(game));

        Board expectedBoard = new Board();
        expectedBoard.addHole(Hole.builder().position(3).type(HoleType.PIT).seeds(0).owner(player1).build());
        expectedBoard.addHole(Hole.builder().position(2).type(HoleType.PIT).seeds(4).owner(player1).build());
        expectedBoard.addHole(Hole.builder().position(1).type(HoleType.POT).seeds(1).owner(player1).build());
        expectedBoard.addHole(Hole.builder().position(3).type(HoleType.PIT).seeds(3).owner(player2).build());
        expectedBoard.addHole(Hole.builder().position(2).type(HoleType.PIT).seeds(3).owner(player2).build());
        expectedBoard.addHole(Hole.builder().position(1).type(HoleType.POT).seeds(0).owner(player2).build());

        int holePosition = 3;
        String gameId = "1";
        String playerId = "1";
        // when
        Game returnedGame = service.makeAMove(gameId, holePosition, playerId);

        // then
        assertThat(returnedGame.getBoard().getHoles()).usingRecursiveComparison().isEqualTo(expectedBoard.getHoles());
        assertThat(returnedGame.getTurn()).isEqualTo(player1);
    }

    @Test
    void shouldCaptureOpponentOppositeHole() {
        // given
        GamePlayer player1 = new GamePlayer("1", "name1");
        GamePlayer player2 = new GamePlayer("2", "name2");

        Board board = new Board();
        board.addHole(Hole.builder().position(3).type(HoleType.PIT).seeds(5).owner(player1).build());
        board.addHole(Hole.builder().position(2).type(HoleType.PIT).seeds(0).owner(player1).build());
        board.addHole(Hole.builder().position(1).type(HoleType.POT).seeds(0).owner(player1).build());
        board.addHole(Hole.builder().position(3).type(HoleType.PIT).seeds(3).owner(player2).build());
        board.addHole(Hole.builder().position(2).type(HoleType.PIT).seeds(3).owner(player2).build());
        board.addHole(Hole.builder().position(1).type(HoleType.POT).seeds(0).owner(player2).build());

        Game game = defaultGame(player1, board);
        game.addPlayer(player2);
        game.setId("1");
        game.start();

        when(gameRepository.findById(anyString())).thenReturn(Optional.of(game));

        Board expectedBoard = new Board();
        expectedBoard.addHole(Hole.builder().position(3).type(HoleType.PIT).seeds(0).owner(player1).build());
        expectedBoard.addHole(Hole.builder().position(2).type(HoleType.PIT).seeds(1).owner(player1).build());
        expectedBoard.addHole(Hole.builder().position(1).type(HoleType.POT).seeds(6).owner(player1).build());
        expectedBoard.addHole(Hole.builder().position(3).type(HoleType.PIT).seeds(0).owner(player2).build());
        expectedBoard.addHole(Hole.builder().position(2).type(HoleType.PIT).seeds(4).owner(player2).build());
        expectedBoard.addHole(Hole.builder().position(1).type(HoleType.POT).seeds(0).owner(player2).build());

        int holePosition = 3;
        String gameId = "1";
        String playerId = "1";
        // when
        Game returnedGame = service.makeAMove(gameId, holePosition, playerId);

        // then
        assertThat(returnedGame.getBoard().getHoles()).usingRecursiveComparison().isEqualTo(expectedBoard.getHoles());
        assertThat(returnedGame.getTurn()).isEqualTo(player2);
    }

    @Test
    void shouldPlayer1WinTheGame() {
        // given
        GamePlayer player1 = new GamePlayer("1", "name1");
        GamePlayer player2 = new GamePlayer("2", "name2");

        Board board = new Board();
        board.addHole(Hole.builder().position(3).type(HoleType.PIT).seeds(0).owner(player1).build());
        board.addHole(Hole.builder().position(2).type(HoleType.PIT).seeds(2).owner(player1).build());
        board.addHole(Hole.builder().position(1).type(HoleType.POT).seeds(4).owner(player1).build());
        board.addHole(Hole.builder().position(3).type(HoleType.PIT).seeds(2).owner(player2).build());
        board.addHole(Hole.builder().position(2).type(HoleType.PIT).seeds(1).owner(player2).build());
        board.addHole(Hole.builder().position(1).type(HoleType.POT).seeds(0).owner(player2).build());

        Game game = defaultGame(player1, board);
        game.addPlayer(player2);
        game.start();

        when(gameRepository.findById(anyString())).thenReturn(Optional.of(game));

        int holePosition = 2;
        String gameId = "1";
        String playerId = "1";

        // when
        Game finishedGame = service.makeAMove(gameId, holePosition, playerId);

        // then
        Board expectedBoard = new Board();
        expectedBoard.addHole(Hole.builder().position(3).type(HoleType.PIT).seeds(0).owner(player1).build());
        expectedBoard.addHole(Hole.builder().position(2).type(HoleType.PIT).seeds(0).owner(player1).build());
        expectedBoard.addHole(Hole.builder().position(1).type(HoleType.POT).seeds(5).owner(player1).build());
        expectedBoard.addHole(Hole.builder().position(3).type(HoleType.PIT).seeds(0).owner(player2).build());
        expectedBoard.addHole(Hole.builder().position(2).type(HoleType.PIT).seeds(0).owner(player2).build());
        expectedBoard.addHole(Hole.builder().position(1).type(HoleType.POT).seeds(4).owner(player2).build());

        assertThat(finishedGame.getMessage()).isEqualTo("name1 won the game with 5 seeds!");
        assertThat(finishedGame.getStatus()).isEqualTo(GameStatus.FINISHED);
        assertThat(game.getBoard().getHoles()).usingRecursiveComparison().isEqualTo(expectedBoard.getHoles());

    }

    @Test
    void shouldPlayer2WinTheGame() {
        // given
        GamePlayer player1 = new GamePlayer("1", "name1");
        GamePlayer player2 = new GamePlayer("2", "name2");

        Board board = new Board();
        board.addHole(Hole.builder().position(3).type(HoleType.PIT).seeds(0).owner(player1).build());
        board.addHole(Hole.builder().position(2).type(HoleType.PIT).seeds(2).owner(player1).build());
        board.addHole(Hole.builder().position(1).type(HoleType.POT).seeds(2).owner(player1).build());
        board.addHole(Hole.builder().position(3).type(HoleType.PIT).seeds(2).owner(player2).build());
        board.addHole(Hole.builder().position(2).type(HoleType.PIT).seeds(1).owner(player2).build());
        board.addHole(Hole.builder().position(1).type(HoleType.POT).seeds(0).owner(player2).build());

        Game game = defaultGame(player1, board);
        game.addPlayer(player2);
        game.start();

        when(gameRepository.findById(anyString())).thenReturn(Optional.of(game));

        int holePosition = 2;
        String gameId = "1";
        String playerId = "1";

        // when
        Game finishedGame = service.makeAMove(gameId, holePosition, playerId);

        // then
        Board expectedBoard = new Board();
        expectedBoard.addHole(Hole.builder().position(3).type(HoleType.PIT).seeds(0).owner(player1).build());
        expectedBoard.addHole(Hole.builder().position(2).type(HoleType.PIT).seeds(0).owner(player1).build());
        expectedBoard.addHole(Hole.builder().position(1).type(HoleType.POT).seeds(3).owner(player1).build());
        expectedBoard.addHole(Hole.builder().position(3).type(HoleType.PIT).seeds(0).owner(player2).build());
        expectedBoard.addHole(Hole.builder().position(2).type(HoleType.PIT).seeds(0).owner(player2).build());
        expectedBoard.addHole(Hole.builder().position(1).type(HoleType.POT).seeds(4).owner(player2).build());

        assertThat(finishedGame.getMessage()).isEqualTo("name2 won the game with 4 seeds!");
        assertThat(finishedGame.getStatus()).isEqualTo(GameStatus.FINISHED);
        assertThat(game.getBoard().getHoles()).usingRecursiveComparison().isEqualTo(expectedBoard.getHoles());
    }

    @Test
    void shouldBeADrawn() {
        // given
        GamePlayer player1 = new GamePlayer("1", "name1");
        GamePlayer player2 = new GamePlayer("2", "name2");

        Board board = new Board();
        board.addHole(Hole.builder().position(3).type(HoleType.PIT).seeds(0).owner(player1).build());
        board.addHole(Hole.builder().position(2).type(HoleType.PIT).seeds(2).owner(player1).build());
        board.addHole(Hole.builder().position(1).type(HoleType.POT).seeds(3).owner(player1).build());
        board.addHole(Hole.builder().position(3).type(HoleType.PIT).seeds(2).owner(player2).build());
        board.addHole(Hole.builder().position(2).type(HoleType.PIT).seeds(1).owner(player2).build());
        board.addHole(Hole.builder().position(1).type(HoleType.POT).seeds(0).owner(player2).build());

        Game game = defaultGame(player1, board);
        game.addPlayer(player2);
        game.start();

        int holePosition = 2;
        String gameId = "2";
        String playerId = "1";
        when(gameRepository.findById(anyString())).thenReturn(Optional.of(game));

        // when
        Game finishedGame = service.makeAMove(gameId, holePosition, playerId);

        // then
        Board expectedBoard = new Board();
        expectedBoard.addHole(Hole.builder().position(3).type(HoleType.PIT).seeds(0).owner(player1).build());
        expectedBoard.addHole(Hole.builder().position(2).type(HoleType.PIT).seeds(0).owner(player1).build());
        expectedBoard.addHole(Hole.builder().position(1).type(HoleType.POT).seeds(4).owner(player1).build());
        expectedBoard.addHole(Hole.builder().position(3).type(HoleType.PIT).seeds(0).owner(player2).build());
        expectedBoard.addHole(Hole.builder().position(2).type(HoleType.PIT).seeds(0).owner(player2).build());
        expectedBoard.addHole(Hole.builder().position(1).type(HoleType.POT).seeds(4).owner(player2).build());

        assertThat(finishedGame.getMessage()).isEqualTo("name1, name2 drawn the game with 4 seeds!");
        assertThat(finishedGame.getStatus()).isEqualTo(GameStatus.FINISHED);
        assertThat(game.getBoard().getHoles()).usingRecursiveComparison().isEqualTo(expectedBoard.getHoles());
    }
    
    @Test
    void shouldAddWatcher() {
        // given
        GamePlayer player1 = new GamePlayer("1", "name1");

        Board board = new Board();
        Game game = defaultGame(player1, board);

        String gameId = "1";
        when(gameRepository.findById(anyString())).thenReturn(Optional.of(game));

        // when
        Game finishedGame = service.spectate(gameId, new Player("1", "name1"));

        // then        
        assertThat(finishedGame.getWatchers()).size().isEqualTo(1);
    }
    
    @Test
    void shouldIgnoreTheAlreadyPresentWatcher() {
        // given
        GamePlayer player1 = new GamePlayer("1", "name1");

        Board board = new Board();
        Game game = defaultGame(player1, board);
        game.addWatcher(new GamePlayer("1", "name2"));
        game.addWatcher(new GamePlayer("2", "name3"));
        String gameId = "1";
        when(gameRepository.findById(anyString())).thenReturn(Optional.of(game));

        // when
        Game finishedGame = service.spectate(gameId, new Player("1", "name1"));

        // then        
        assertThat(finishedGame.getWatchers()).size().isEqualTo(2);
    }

    private Game defaultGame(GamePlayer player1, Board board) {
        return new Game("gameName", player1, board, new GameConfigurationService().getDefaultConfig());
    }

}
