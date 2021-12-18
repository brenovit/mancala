package com.bol.brenovit.mancala.core.game.service.board;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.bol.brenovit.mancala.core.game.dto.Board;
import com.bol.brenovit.mancala.core.game.dto.Hole;
import com.bol.brenovit.mancala.core.game.dto.HoleType;
import com.bol.brenovit.mancala.core.game.dto.GamePlayer;

class HoleServiceTest {
    
    private HoleService service = new HoleService();
    
    @Test
    void shouldBeACompleteTurn() {
        //given
        GamePlayer player1 = new GamePlayer("1", "name1");
        
        Board board = new Board();
        board.addHole(Hole.builder().position(3).type(HoleType.PIT).seeds(5).owner(player1).build());
        board.addHole(Hole.builder().position(2).type(HoleType.PIT).seeds(2).owner(player1).build());
        board.addHole(Hole.builder().position(1).type(HoleType.POT).seeds(2).owner(player1).build());
        board.addHole(Hole.builder().position(3).type(HoleType.PIT).seeds(3).owner(player1).build());
        board.addHole(Hole.builder().position(2).type(HoleType.PIT).seeds(1).owner(player1).build());
        board.addHole(Hole.builder().position(1).type(HoleType.POT).seeds(0).owner(player1).build());
        
        int selectedHolePosition = 3;

        //when
        boolean isCompleteTurn = service.isACompleteTurn(selectedHolePosition, board, player1);
        
        //then
        assertTrue(isCompleteTurn);
    }
    
    @Test
    void shouldNotBeACompleteTurn() {
        //given
        GamePlayer player1 = new GamePlayer("1", "name1");
        
        Board board = new Board();
        board.addHole(Hole.builder().position(3).type(HoleType.PIT).seeds(4).owner(player1).build());
        board.addHole(Hole.builder().position(2).type(HoleType.PIT).seeds(2).owner(player1).build());
        board.addHole(Hole.builder().position(1).type(HoleType.POT).seeds(2).owner(player1).build());
        board.addHole(Hole.builder().position(3).type(HoleType.PIT).seeds(3).owner(player1).build());
        board.addHole(Hole.builder().position(2).type(HoleType.PIT).seeds(1).owner(player1).build());
        board.addHole(Hole.builder().position(1).type(HoleType.POT).seeds(0).owner(player1).build());
        
        int selectedHolePosition = 3;

        //when
        boolean isCompleteTurn = service.isACompleteTurn(selectedHolePosition, board, player1);
        
        //then
        assertFalse(isCompleteTurn);
    }
       
    @Test
    void shouldDistributeSelectedHoleSeedsAcrossBoardHoles() {
        //given
        GamePlayer player1 = new GamePlayer("1", "name1");
        
        Board board = new Board();
        board.addHole(Hole.builder().position(3).type(HoleType.PIT).seeds(2).owner(player1).build());
        board.addHole(Hole.builder().position(2).type(HoleType.PIT).seeds(1).owner(player1).build());
        board.addHole(Hole.builder().position(1).type(HoleType.POT).seeds(0).owner(player1).build());
        
        int selectedHolePosition = 3;

        //when
        Hole lastHole = service.distributeHoleSeedsAcrossBoardHoles(selectedHolePosition, board, player1);
        
        //then
        assertEquals(1, lastHole.getPosition());
        assertEquals(1, lastHole.getSeeds());
        
        Optional<Hole> hole3 = board.getHole(3, player1);
        assertEquals(HoleType.PIT, hole3.get().getType());
        assertEquals(0, hole3.get().getSeeds());
        
        Optional<Hole> hole2 = board.getHole(2, player1);
        assertEquals(HoleType.PIT, hole2.get().getType());
        assertEquals(2, hole2.get().getSeeds());
        
        Optional<Hole> hole1 = board.getHole(1, player1);
        assertEquals(HoleType.POT, hole1.get().getType());
        assertEquals(1, hole1.get().getSeeds());
    }
    
    @Test
    void shouldDistributeSelectedHoleSeedsAcrossBoardHolesJumpingOpponentPOThole() {
        //given
        GamePlayer player1 = new GamePlayer("1", "name1");
        GamePlayer player2 = new GamePlayer("2", "name2");
        
        Board board = new Board();
        board.addHole(Hole.builder().position(3).type(HoleType.PIT).seeds(2).owner(player1).build());
        board.addHole(Hole.builder().position(2).type(HoleType.PIT).seeds(2).owner(player1).build());
        board.addHole(Hole.builder().position(1).type(HoleType.POT).seeds(1).owner(player2).build());
        board.addHole(Hole.builder().position(14).type(HoleType.PIT).seeds(5).owner(player2).build());
        
        int selectedHolePosition = 3;

        //when
        Hole lastHole = service.distributeHoleSeedsAcrossBoardHoles(selectedHolePosition, board, player1);
        
        //then
        assertEquals(14, lastHole.getPosition());
        assertEquals(6, lastHole.getSeeds());
        
        Optional<Hole> hole3 = board.getHole(3, player1);
        assertEquals(HoleType.PIT, hole3.get().getType());
        assertEquals(0, hole3.get().getSeeds());
        
        Optional<Hole> hole2 = board.getHole(2, player1);
        assertEquals(HoleType.PIT, hole2.get().getType());
        assertEquals(3, hole2.get().getSeeds());
        
        Optional<Hole> hole1 = board.getHole(1, player2);
        assertEquals(HoleType.POT, hole1.get().getType());
        assertEquals(1, hole1.get().getSeeds());
        
        Optional<Hole> hole14 = board.getHole(14, player2);
        assertEquals(HoleType.PIT, hole14.get().getType());
        assertEquals(6, hole14.get().getSeeds());
    }
    
    @Test
    void shouldThrownAnErrorWhenDistributeInnexistentHole() {
        //given
        GamePlayer player1 = new GamePlayer("1", "name1");
        GamePlayer player2 = new GamePlayer("2", "name2");
        
        Board board = new Board();
        board.addHole(Hole.builder().position(1).type(HoleType.POT).seeds(2).owner(player1).build());
        board.addHole(Hole.builder().position(2).type(HoleType.PIT).seeds(3).owner(player1).build());
        board.addHole(Hole.builder().position(1).type(HoleType.POT).seeds(4).owner(player2).build());
        board.addHole(Hole.builder().position(2).type(HoleType.PIT).seeds(5).owner(player2).build());
                
        int selectedHolePosition = 3;
        
        //when
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> 
            service.distributeHoleSeedsAcrossBoardHoles(selectedHolePosition, board, player1));
        
        //then
        assertEquals("You can't sow a innexistent pit", thrown.getMessage());
    }
    
    @Test
    void shouldThrownAnErrorWhenDistributeEmptyHole() {
        //given
        GamePlayer player1 = new GamePlayer("1", "name1");
        GamePlayer player2 = new GamePlayer("2", "name2");
        
        Board board = new Board();
        board.addHole(Hole.builder().position(1).type(HoleType.POT).seeds(2).owner(player1).build());
        board.addHole(Hole.builder().position(2).type(HoleType.PIT).seeds(0).owner(player1).build());
        board.addHole(Hole.builder().position(1).type(HoleType.POT).seeds(4).owner(player2).build());
        board.addHole(Hole.builder().position(2).type(HoleType.PIT).seeds(5).owner(player2).build());
                
        int selectedHolePosition = 2;
        
        //when
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> 
            service.distributeHoleSeedsAcrossBoardHoles(selectedHolePosition, board, player1));
        
        //then
        assertEquals("You can't sow from an empty pit", thrown.getMessage());
    }
    
    @Test
    void shouldThrownAnErrorWhenDistributeAPotHole() {
        //given
        GamePlayer player1 = new GamePlayer("1", "name1");
        GamePlayer player2 = new GamePlayer("2", "name2");
        
        Board board = new Board();
        board.addHole(Hole.builder().position(1).type(HoleType.POT).seeds(2).owner(player1).build());
        board.addHole(Hole.builder().position(2).type(HoleType.PIT).seeds(3).owner(player1).build());
        board.addHole(Hole.builder().position(1).type(HoleType.POT).seeds(4).owner(player2).build());
        board.addHole(Hole.builder().position(2).type(HoleType.PIT).seeds(5).owner(player2).build());
                
        int selectedHolePosition = 1;
        
        //when
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> 
            service.distributeHoleSeedsAcrossBoardHoles(selectedHolePosition, board, player1));
        
        //then
        assertEquals("You can't sow the pot", thrown.getMessage());
    }
    
    @Test
    void shouldReturnTrueForAnySideAllHolesEmpty() {
        //given
        GamePlayer player1 = new GamePlayer("1", "name1");
        GamePlayer player2 = new GamePlayer("2", "name2");
        
        Board board = new Board();
        board.addHole(Hole.builder().position(1).type(HoleType.POT).seeds(5).owner(player1).build());
        board.addHole(Hole.builder().position(2).type(HoleType.PIT).seeds(0).owner(player1).build());
        board.addHole(Hole.builder().position(3).type(HoleType.PIT).seeds(0).owner(player1).build());
        board.addHole(Hole.builder().position(1).type(HoleType.POT).seeds(0).owner(player2).build());
        board.addHole(Hole.builder().position(2).type(HoleType.PIT).seeds(1).owner(player2).build());
        board.addHole(Hole.builder().position(3).type(HoleType.PIT).seeds(0).owner(player2).build());
                
        //when
        boolean anySideEmpty = service.isAnySideWithAllPitHolesEmpty(board);
        
        assertTrue(anySideEmpty);
    }
    
    @Test
    void shouldReturnFalseForAnySideAllHolesEmpty() {
        //given
        GamePlayer player1 = new GamePlayer("1", "name1");
        GamePlayer player2 = new GamePlayer("2", "name2");
        
        Board board = new Board();
        board.addHole(Hole.builder().position(1).type(HoleType.POT).seeds(0).owner(player1).build());
        board.addHole(Hole.builder().position(2).type(HoleType.PIT).seeds(0).owner(player1).build());
        board.addHole(Hole.builder().position(3).type(HoleType.PIT).seeds(1).owner(player1).build());
        board.addHole(Hole.builder().position(1).type(HoleType.POT).seeds(0).owner(player2).build());
        board.addHole(Hole.builder().position(2).type(HoleType.PIT).seeds(1).owner(player2).build());
        board.addHole(Hole.builder().position(3).type(HoleType.PIT).seeds(0).owner(player2).build());
                
        //when
        boolean anySideEmpty = service.isAnySideWithAllPitHolesEmpty(board);
        
        assertFalse(anySideEmpty);
    }

    @Test
    void shouldCaptureOppositeHoleAndCleanIt() {
        //given
        GamePlayer player1 = new GamePlayer("1", "name1");
        GamePlayer player2 = new GamePlayer("2", "name2");
        
        Board board = new Board();
        board.addHole(Hole.builder().position(1).type(HoleType.POT).seeds(2).owner(player1).build());
        board.addHole(Hole.builder().position(2).type(HoleType.PIT).seeds(3).owner(player1).build());
        board.addHole(Hole.builder().position(1).type(HoleType.POT).seeds(4).owner(player2).build());
        board.addHole(Hole.builder().position(2).type(HoleType.PIT).seeds(5).owner(player2).build());
                
        //when
        service.captureOpponentOppositeHoleSeeds(2, board, player1);
        
        Optional<Hole> potHole = board.getHole(1, player1);
        Optional<Hole> pitHole1 = board.getHole(2, player1);
        Optional<Hole> pitHole2 = board.getHole(2, player2);
        
        //then
        assertEquals(HoleType.POT, potHole.get().getType());
        assertEquals(7, potHole.get().getSeeds());
        
        assertEquals(pitHole1.get().getPosition(), pitHole2.get().getPosition());
        assertFalse(pitHole1.get().isOwner(pitHole2.get().getOwner()));
        
        assertEquals(3, pitHole1.get().getSeeds());
        assertEquals(0, pitHole2.get().getSeeds());
   }
    
   @Test
   void shouldMoveAllHoleSeedsFromPitToPot() {
        //given
        GamePlayer player1 = new GamePlayer("1", "name1");
        GamePlayer player2 = new GamePlayer("2", "name2");
        
        Board board = new Board();
        board.addHole(Hole.builder().position(1).type(HoleType.POT).seeds(2).owner(player1).build());
        board.addHole(Hole.builder().position(2).type(HoleType.PIT).seeds(1).owner(player1).build());
        board.addHole(Hole.builder().position(3).type(HoleType.POT).seeds(4).owner(player2).build());
                
        //when         
        service.moveAllHoleSeedsFromPitToPot(2, board, player1);
        
        //then
        Optional<Hole> potHole = board.getHole(1, player1);
        Optional<Hole> pitHole1 = board.getHole(2, player1);

        assertEquals(HoleType.POT, potHole.get().getType());
        assertEquals(3, potHole.get().getSeeds());      

        assertEquals(HoleType.POT, potHole.get().getType());
        assertEquals(0, pitHole1.get().getSeeds());
   }
   
   @Test
   void shouldMoveAllOpponentSeedsFromPitToPot() {
        //given
        GamePlayer player1 = new GamePlayer("1", "name1");
        GamePlayer player2 = new GamePlayer("2", "name2");
        GamePlayer player3 = new GamePlayer("3", "name3");
        
        Board board = new Board();
        board.addHole(Hole.builder().position(1).type(HoleType.POT).seeds(1).owner(player1).build());
        board.addHole(Hole.builder().position(2).type(HoleType.PIT).seeds(2).owner(player1).build());        
        board.addHole(Hole.builder().position(1).type(HoleType.POT).seeds(2).owner(player2).build());
        board.addHole(Hole.builder().position(2).type(HoleType.PIT).seeds(3).owner(player2).build());        
        board.addHole(Hole.builder().position(1).type(HoleType.POT).seeds(3).owner(player3).build());        
        board.addHole(Hole.builder().position(2).type(HoleType.PIT).seeds(4).owner(player3).build());
                
        //when
        service.moveAllOpponentSeedsFromPitToPot(board, player1);
        
        Optional<Hole> potHole1 = board.getHole(1, player1);
        Optional<Hole> potHole2 = board.getHole(1, player2);
        Optional<Hole> potHole3 = board.getHole(1, player3);
        
        Optional<Hole> pitHole1 = board.getHole(2, player1);
        Optional<Hole> pitHole2 = board.getHole(2, player2);
        Optional<Hole> pitHole3 = board.getHole(2, player3);

        assertEquals(HoleType.POT, potHole1.get().getType());
        assertEquals(1, potHole1.get().getSeeds());

        assertEquals(HoleType.PIT, pitHole1.get().getType());
        assertEquals(2, pitHole1.get().getSeeds());
        
        assertEquals(HoleType.POT, potHole2.get().getType());
        assertEquals(5, potHole2.get().getSeeds());

        assertEquals(HoleType.PIT, pitHole2.get().getType());
        assertEquals(0, pitHole2.get().getSeeds());
        
        assertEquals(HoleType.POT, potHole3.get().getType());
        assertEquals(7, potHole3.get().getSeeds());      

        assertEquals(HoleType.PIT, pitHole3.get().getType());
        assertEquals(0, pitHole3.get().getSeeds());

   }
   
   @Test
   void shouldReturn1PotWithLargestsAmountOfSeeds() {
        //given
        GamePlayer player1 = new GamePlayer("1", "name1");
        GamePlayer player2 = new GamePlayer("2", "name2");
        GamePlayer player3 = new GamePlayer("3", "name3");
        
        Board board = new Board();
        board.addHole(Hole.builder().position(1).type(HoleType.POT).seeds(2).owner(player1).build());
        board.addHole(Hole.builder().position(2).type(HoleType.PIT).seeds(2).owner(player1).build());        
        board.addHole(Hole.builder().position(1).type(HoleType.POT).seeds(3).owner(player2).build());
        board.addHole(Hole.builder().position(2).type(HoleType.PIT).seeds(3).owner(player2).build());        
        board.addHole(Hole.builder().position(1).type(HoleType.POT).seeds(2).owner(player3).build());        
        board.addHole(Hole.builder().position(2).type(HoleType.PIT).seeds(4).owner(player3).build());
                
        //when
        List<Hole> holes = service.getPotsWithLargestsAmountOfSeeds(board);
        
        assertEquals(1, holes.size());
        assertEquals(3, holes.get(0).getSeeds());
        assertEquals(HoleType.POT, holes.get(0).getType());
        assertEquals(player2, holes.get(0).getOwner());
   }
   
   @Test
   void shouldReturnAllPotWithLargestsAmountOfSeeds() {
        //given
        GamePlayer player1 = new GamePlayer("1", "name1");
        GamePlayer player2 = new GamePlayer("2", "name2");
        GamePlayer player3 = new GamePlayer("3", "name3");
        
        Board board = new Board();
        board.addHole(Hole.builder().position(1).type(HoleType.POT).seeds(3).owner(player1).build());
        board.addHole(Hole.builder().position(2).type(HoleType.PIT).seeds(2).owner(player1).build());        
        board.addHole(Hole.builder().position(1).type(HoleType.POT).seeds(2).owner(player2).build());
        board.addHole(Hole.builder().position(2).type(HoleType.PIT).seeds(3).owner(player2).build());        
        board.addHole(Hole.builder().position(1).type(HoleType.POT).seeds(3).owner(player3).build());        
        board.addHole(Hole.builder().position(2).type(HoleType.PIT).seeds(4).owner(player3).build());
                
        //when
        List<Hole> holes = service.getPotsWithLargestsAmountOfSeeds(board);
        
        assertEquals(2, holes.size());
        assertEquals(3, holes.get(0).getSeeds());
        assertEquals(HoleType.POT, holes.get(0).getType());
        assertEquals(3, holes.get(1).getSeeds());
        assertEquals(HoleType.POT, holes.get(1).getType());
   }
}
