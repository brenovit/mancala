package com.bol.brenovit.mancala.core.game.linkedlist;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.bol.brenovit.mancala.core.game.dto.Hole;
import com.bol.brenovit.mancala.core.game.dto.GamePlayer;

class CircularLinkedListTest {

    @Test
    void shouldHaveNotFirstNode() {
        //given
        CircularLinkedList linkedList = new CircularLinkedList();
        
        //then
        assertNull(linkedList.getFirst());
    }
    
    @Test
    void shouldHaveSize0() {
        //given
        CircularLinkedList linkedList = new CircularLinkedList();
        
        //then
        assertEquals(0, linkedList.size());
    }
    
    @Test
    void shouldHaveFirstNode() {
        //given
        CircularLinkedList linkedList = new CircularLinkedList();
        GamePlayer player = new GamePlayer("1", "name1");
        
        //when
        linkedList.addNode(Hole.builder().position(1).owner(player).build());
        
        //then
        assertNotNull(linkedList.getFirst());
    }
    
    @Test
    void shouldThrowErrorWhenAddingNodeWithoutOwner() {
        //given
        CircularLinkedList linkedList = new CircularLinkedList();
        
        //when
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> 
            linkedList.addNode(Hole.builder().position(1).build()));
        
        //then
        assertEquals("Cannot add a Hole node without its owner", thrown.getMessage());
    }
    
    @Test
    void shouldNextNodeBeFirst() {
        //given
        CircularLinkedList linkedList = new CircularLinkedList();
        GamePlayer player = new GamePlayer("1", "name1");
        
        //when
        linkedList.addNode(Hole.builder().position(1).owner(player).build());        
        
        //then
        Node head = linkedList.getFirst();
        assertNotNull(linkedList.getFirst());
        assertEquals(head, linkedList.getFirst().getNext());
        assertEquals(0, head.getIndex());
    }
    
    @Test
    void shouldNextNodeReturnToFirst() {
        //given
        CircularLinkedList linkedList = new CircularLinkedList();
        GamePlayer player = new GamePlayer("1", "name1");
        
        //when
        linkedList.addNode(Hole.builder().position(1).owner(player).build());
        linkedList.addNode(Hole.builder().position(2).owner(player).build());
        
        //then
        Node head = linkedList.getFirst();
        assertEquals(1, head.getValue().getPosition());
        assertEquals(0, head.getIndex());
        Node nextFirst = head.getNext();
        assertEquals(2, nextFirst.getValue().getPosition());
        assertEquals(1, nextFirst.getIndex());
        Node nextNextFirst = nextFirst.getNext();
        assertEquals(1, nextNextFirst.getValue().getPosition());
        assertEquals(0, nextNextFirst.getIndex());
    }
    
    @Test
    void shouldHaveSize3() {
        //given
        CircularLinkedList linkedList = new CircularLinkedList();
        GamePlayer player = new GamePlayer("1", "name1");
        
        //when
        linkedList.addNode(Hole.builder().position(1).owner(player).build());
        linkedList.addNode(Hole.builder().position(2).owner(player).build());
        linkedList.addNode(Hole.builder().position(3).owner(player).build());
        
        //then
        assertEquals(3, linkedList.size());
    }
    
    @Test
    void shouldReturnHoleByPosition() {
        //given
        CircularLinkedList linkedList = new CircularLinkedList();
                
        linkedList.addNode(Hole.builder().position(1).owner(new GamePlayer("1", "name1")).build());
        linkedList.addNode(Hole.builder().position(2).owner(new GamePlayer("2", "name2")).build());
        linkedList.addNode(Hole.builder().position(3).owner(new GamePlayer("3", "name3")).build());
        
        //when
        Optional<Hole> node = linkedList.getFirstMatchHole(Hole.builder().position(2).owner(new GamePlayer("2", "name2")).build());
        
        //then
        assertTrue(node.isPresent());
        assertEquals(2, node.get().getPosition());
        assertEquals("name2", node.get().getOwner().getName());
    }
    
    @Test
    void shouldReturnEmptyHoleWhenOwnerNotInformed() {
        //given
        CircularLinkedList linkedList = new CircularLinkedList();
                
        linkedList.addNode(Hole.builder().position(1).owner(new GamePlayer("1", "name1")).build());
        linkedList.addNode(Hole.builder().position(2).owner(new GamePlayer("2", "name2")).build());
        linkedList.addNode(Hole.builder().position(3).owner(new GamePlayer("3", "name3")).build());
        
        //when
        Optional<Hole> node = linkedList.getFirstMatchHole(Hole.builder().position(2).build());
        
        //then
        assertFalse(node.isPresent());        
    }
    
    @Test
    void shouldReturnEmptyHoleWhenOwnerInformedIsWrong() {
        //given
        CircularLinkedList linkedList = new CircularLinkedList();
        GamePlayer player = new GamePlayer("2", "name2");
                
        linkedList.addNode(Hole.builder().position(1).owner(new GamePlayer("1", "name1")).build());
        linkedList.addNode(Hole.builder().position(2).owner(new GamePlayer("2", "name2")).build());
        linkedList.addNode(Hole.builder().position(3).owner(new GamePlayer("3", "name3")).build());
        
        //when
        Optional<Hole> node = linkedList.getFirstMatchHole(Hole.builder().position(3).owner(player).build());
        
        //then
        assertFalse(node.isPresent());        
    }
    
    @Test
    void shouldReturnHoleByTheFirstPositionMatch() {
        //given
        CircularLinkedList linkedList = new CircularLinkedList();
        
        linkedList.addNode(Hole.builder().position(1).owner(new GamePlayer("1", "name1")).build());
        linkedList.addNode(Hole.builder().position(2).owner(new GamePlayer("2", "name2")).build());
        linkedList.addNode(Hole.builder().position(3).owner(new GamePlayer("3", "name3")).build());
        linkedList.addNode(Hole.builder().position(2).owner(new GamePlayer("2", "name4")).build());

        //when
        Optional<Hole> node = linkedList.getFirstMatchHole(Hole.builder().position(2).owner(new GamePlayer("2", "name2")).build());

        //then
        assertTrue(node.isPresent());
        assertEquals(2, node.get().getPosition());
        assertEquals("name2", node.get().getOwner().getName());
        assertNotEquals("name4", node.get().getOwner().getName());
    }
    
    @Test
    void shouldReturnEmptyHoleWhenThereIsNoNode() {
        //given
        CircularLinkedList linkedList = new CircularLinkedList();
        
        //when
        Optional<Hole> node = linkedList.getFirstMatchHole(Hole.builder().position(2).owner(new GamePlayer("2", "name2")).build());

        //then
        assertFalse(node.isPresent());
    }
    
    @Test
    void shouldReturnEmptyHoleWhenNodeNotFound() {
        //given
        CircularLinkedList linkedList = new CircularLinkedList();
        
        linkedList.addNode(Hole.builder().position(1).owner(new GamePlayer("1", "name1")).build());
        linkedList.addNode(Hole.builder().position(2).owner(new GamePlayer("2", "name2")).build());
        
        //when
        Optional<Hole> node = linkedList.getFirstMatchHole(Hole.builder().position(3).owner(new GamePlayer("1", "name1")).build());

        //then
        assertFalse(node.isPresent());
    }
    
    @Test
    void shouldReturnFirstMatchOppositeHole() {
        //given
        CircularLinkedList linkedList = new CircularLinkedList();
        
        GamePlayer player1 = new GamePlayer("1", "name1");
        
        linkedList.addNode(Hole.builder().position(1).owner(player1).build());
        linkedList.addNode(Hole.builder().position(1).owner(new GamePlayer("2", "name2")).build());
        
        //when
        Optional<Hole> node = linkedList.getFirstMatchOppositeHole(Hole.builder().position(1).owner(player1).build());

        //then
        assertTrue(node.isPresent());
        assertFalse(node.get().isOwner(player1));
        assertEquals(1, node.get().getPosition());
    }
    
    @Test
    void shouldReturnEmptyNodeWhenThereisNoOppositeHole() {
        //given
        CircularLinkedList linkedList = new CircularLinkedList();
        
        GamePlayer player1 = new GamePlayer("1", "name1");
        
        linkedList.addNode(Hole.builder().position(1).owner(player1).build());
        linkedList.addNode(Hole.builder().position(2).owner(new GamePlayer("2", "name2")).build());
        
        //when
        Optional<Hole> node = linkedList.getFirstMatchOppositeHole(Hole.builder().position(1).owner(player1).build());

        //then
        assertFalse(node.isPresent());
    }
    
    @Test
    void shouldReturnEmptyNodeWhenTheListIsEmpty() {
        //given
        CircularLinkedList linkedList = new CircularLinkedList();
        
        GamePlayer player1 = new GamePlayer("1", "name1");        
        
        //when
        Optional<Hole> node = linkedList.getFirstMatchOppositeHole(Hole.builder().position(1).owner(player1).build());

        //then
        assertFalse(node.isPresent());
    }
    
    @Test
    void shouldReturnEmptyNodeWhenSearchedHolerOwnerNotInformed() {
        //given
        CircularLinkedList linkedList = new CircularLinkedList();
        
        GamePlayer player1 = new GamePlayer("1", "name1");
        
        linkedList.addNode(Hole.builder().position(1).owner(player1).build());
        linkedList.addNode(Hole.builder().position(2).owner(new GamePlayer("2", "name2")).build());
        
        //when
        Optional<Hole> node = linkedList.getFirstMatchOppositeHole(Hole.builder().position(1).build());

        //then
        assertFalse(node.isPresent());
    }
    
    @Test
    void shouldReturnNodeByPosition() {
        //given
        CircularLinkedList linkedList = new CircularLinkedList();
                
        linkedList.addNode(Hole.builder().position(1).owner(new GamePlayer("1", "name1")).build());
        linkedList.addNode(Hole.builder().position(2).owner(new GamePlayer("2", "name2")).build());
        linkedList.addNode(Hole.builder().position(3).owner(new GamePlayer("3", "name3")).build());
        
        //when
        Optional<Node> node = linkedList.getFirstMatchNode(Hole.builder().position(2).owner(new GamePlayer("2", "name2")).build());
        
        //then
        assertTrue(node.isPresent());
        assertEquals(2, node.get().getPosition());
        assertEquals("name2", node.get().getValue().getOwnerName());
    }
    
    @Test
    void shouldReturnEmptyNodeWhenOwnerNotInformed() {
        //given
        CircularLinkedList linkedList = new CircularLinkedList();
                
        linkedList.addNode(Hole.builder().position(1).owner(new GamePlayer("1", "name1")).build());
        linkedList.addNode(Hole.builder().position(2).owner(new GamePlayer("2", "name2")).build());
        linkedList.addNode(Hole.builder().position(3).owner(new GamePlayer("3", "name3")).build());
        
        //when
        Optional<Node> node = linkedList.getFirstMatchNode(Hole.builder().position(2).build());
        
        //then
        assertFalse(node.isPresent());        
    }
    
    @Test
    void shouldReturnEmptyNodeWhenOwnerInformedIsWrong() {
        //given
        CircularLinkedList linkedList = new CircularLinkedList();
        GamePlayer player = new GamePlayer("2", "name2");
                
        linkedList.addNode(Hole.builder().position(1).owner(new GamePlayer("1", "name1")).build());
        linkedList.addNode(Hole.builder().position(2).owner(new GamePlayer("2", "name2")).build());
        linkedList.addNode(Hole.builder().position(3).owner(new GamePlayer("3", "name3")).build());
        
        //when
        Optional<Node> node = linkedList.getFirstMatchNode(Hole.builder().position(3).owner(player).build());
        
        //then
        assertFalse(node.isPresent());        
    }
    
    @Test
    void shouldReturnEmptyNodeWhenThereIsNoNode() {
        //given
        CircularLinkedList linkedList = new CircularLinkedList();
        
        //when
        Optional<Node> node = linkedList.getFirstMatchNode(Hole.builder().position(2).owner(new GamePlayer("2", "name2")).build());

        //then
        assertFalse(node.isPresent());
    }
    
    @Test
    void shouldReturnCircularListAsList() {
        //given
        CircularLinkedList linkedList = new CircularLinkedList();
        
        GamePlayer player1 = new GamePlayer("1", "name1");
        
        linkedList.addNode(Hole.builder().position(1).owner(player1).build());
        linkedList.addNode(Hole.builder().position(2).owner(player1).build());
        
        //when
        List<Hole> holes = linkedList.getAsList();
        
        //then
        assertEquals(2, holes.size());
    }
    
    @Test
    void shouldReturnEmptyListWhenCircularListIsEmpty() {
        //given
        CircularLinkedList linkedList = new CircularLinkedList();
        
        //when
        List<Hole> holes = linkedList.getAsList();
        
        //then
        assertTrue(holes.isEmpty());
    }

}
