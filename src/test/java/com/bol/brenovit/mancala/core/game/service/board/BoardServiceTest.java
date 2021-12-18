package com.bol.brenovit.mancala.core.game.service.board;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.bol.brenovit.mancala.core.game.common.GameConfigurationConsts;
import com.bol.brenovit.mancala.core.game.dto.Board;
import com.bol.brenovit.mancala.core.game.dto.Game;
import com.bol.brenovit.mancala.core.game.dto.GameConfiguration;
import com.bol.brenovit.mancala.core.game.dto.HoleType;
import com.bol.brenovit.mancala.core.game.dto.GamePlayer;
import com.bol.brenovit.mancala.core.game.linkedlist.Node;

class BoardServiceTest {
    
    private BoardService service = new BoardService();
        
    @Test
    void shouldFillBoardWithMaxHolesNumber() {
        //given
        Game game = new Game("gameName", new GamePlayer("1", "name1"), new Board(), new GameConfiguration(2, 3));
        game.addPlayer(new GamePlayer("2", "name2"));
        
        //when
        Game gameFilled = service.fillBoadHoles(game);
        Board board = gameFilled.getBoard();
        
        //then
        assertEquals(4, board.getHoles().size());
    }
    
    @Test
    void shouldBuildHolesWithInverseOrder() {
        //given
        Game game = new Game("gameName", new GamePlayer("1", "name1"), new Board(), new GameConfiguration(3, 3));
        game.addPlayer(new GamePlayer("2", "name2"));
        
        //when
        Game gameFilled = service.fillBoadHoles(game);
        Board board = gameFilled.getBoard();
        
        //then
        Optional<Node> node3 = board.getNode(3, game.getPlayer1());
        Node node2 = node3.get().getNext();
        assertEquals(2, node2.getPosition());
    }
    
    @Test
    void shouldHaveAtLeastOnePOTPositionsByPlayer() {
        //given
        Game game = new Game("gameName", new GamePlayer("1", "name1"), new Board(), new GameConfiguration(2, 3));
        game.addPlayer(new GamePlayer("2", "name2"));
        
        //when
        Game gameFilled = service.fillBoadHoles(game);
        Board board = gameFilled.getBoard();
        
        //then
        Optional<Node> pitNode1 = board.getNode(GameConfigurationConsts.POT_POSITION, game.getPlayer1());
        assertTrue(pitNode1.isPresent());
        assertEquals(HoleType.POT, pitNode1.get().getValue().getType());
        assertEquals(0, pitNode1.get().getValue().getSeeds());
        
        Optional<Node> pitNode2 = board.getNode(GameConfigurationConsts.POT_POSITION, game.getPlayer2());
        assertTrue(pitNode2.isPresent());
        assertEquals(HoleType.POT, pitNode2.get().getValue().getType());
        assertEquals(0, pitNode1.get().getValue().getSeeds());
    }
    
    @ParameterizedTest
    @ValueSource(ints = {2, 3, 4, 5, 6, 7})
    void shouldHaveAllNonPOTPositionsCreatedAsPIT(int position) {
        //given
        Game game = new Game("gameName", new GamePlayer("1", "name1"), new Board(), new GameConfiguration(7, 6));
        game.addPlayer(new GamePlayer("2", "name2"));
        
        //when
        Game gameFilled = service.fillBoadHoles(game);
        Board board = gameFilled.getBoard();
        
        //then
        Optional<Node> pitNode1 = board.getNode(position, game.getPlayer1());
        assertTrue(pitNode1.isPresent());
        assertEquals(HoleType.PIT, pitNode1.get().getValue().getType());
        assertEquals(6, pitNode1.get().getValue().getSeeds());
        
        Optional<Node> pitNode2 = board.getNode(position, game.getPlayer2());
        assertTrue(pitNode2.isPresent());
        assertEquals(HoleType.PIT, pitNode2.get().getValue().getType());
        assertEquals(6, pitNode2.get().getValue().getSeeds());
    }
}
