package com.bol.brenovit.mancala.core.game.dto;

import java.util.List;
import java.util.Optional;

import com.bol.brenovit.mancala.core.game.linkedlist.CircularLinkedList;
import com.bol.brenovit.mancala.core.game.linkedlist.Node;

public class Board {
    private CircularLinkedList holes;
    
    public Board() {
        this.holes = new CircularLinkedList();
    }
    
    public void addHole(Hole value) {
        holes.addNode(value);
    }
    
    public Optional<Hole> getHole(int position, GamePlayer owner) {
        return holes.getFirstMatchHole(Hole.builder().position(position).owner(owner).build());
    }
    
    public Optional<Hole> getOpponentHole(int position, GamePlayer owner) {
        return holes.getFirstMatchOppositeHole(Hole.builder().position(position).owner(owner).build());
    }
    
    public Optional<Node> getNode(int position, GamePlayer owner) {
        return holes.getFirstMatchNode(Hole.builder().position(position).owner(owner).build());
    }
    
    public List<Hole> getHoles(){
        return holes.getAsList();
    }
    
    public int size() {
        return holes.size();
    }
}
