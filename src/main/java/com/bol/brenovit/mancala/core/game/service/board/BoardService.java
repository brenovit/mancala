package com.bol.brenovit.mancala.core.game.service.board;

import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.bol.brenovit.mancala.core.game.common.GameConfigurationConsts;
import com.bol.brenovit.mancala.core.game.dto.Board;
import com.bol.brenovit.mancala.core.game.dto.Game;
import com.bol.brenovit.mancala.core.game.dto.GameConfiguration;
import com.bol.brenovit.mancala.core.game.dto.Hole;
import com.bol.brenovit.mancala.core.game.dto.Hole.HoleBuilder;
import com.bol.brenovit.mancala.core.game.dto.HoleType;
import com.bol.brenovit.mancala.core.game.dto.GamePlayer;

@Service
public class BoardService {

    public Game fillBoadHoles(Game game) {
        Board board = game.getBoard();

        Stream.of(game.getPlayer1(), game.getPlayer2())
                .forEach(player -> fillHolesForPlayer(board, player, game.getConfig()));

        return game;
    }

    private void fillHolesForPlayer(Board board, GamePlayer player, GameConfiguration gameConfig) {
        for (int i = gameConfig.getMaxHolesOnBoard(); i > 0; i--) {
            HoleBuilder hole = Hole.builder()
                    .position(i)
                    .seeds(gameConfig.getInitialSeedByHole())
                    .owner(player)
                    .type(HoleType.PIT);

            if (i == GameConfigurationConsts.POT_POSITION) {
                hole
                        .type(HoleType.POT)
                        .seeds(0);
            }

            board.addHole(hole.build());
        }
    }
}
