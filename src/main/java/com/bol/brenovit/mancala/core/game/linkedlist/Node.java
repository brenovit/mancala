package com.bol.brenovit.mancala.core.game.linkedlist;

import com.bol.brenovit.mancala.core.game.dto.Hole;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Node {
    private Hole value;
    @Setter(value = AccessLevel.PROTECTED)
    private Node next;
    private int index;

    public Node(Hole value) {
        super();
        this.value = value;
    }

    public int getPosition() {
        return this.value.getPosition();
    }

    protected void updateIndex(int index) {
        this.index = index;
        this.value.setIndex(index);
    }
}
