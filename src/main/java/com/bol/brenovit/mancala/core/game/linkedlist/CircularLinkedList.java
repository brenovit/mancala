package com.bol.brenovit.mancala.core.game.linkedlist;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.lang.NonNull;

import com.bol.brenovit.mancala.core.game.dto.Hole;

import lombok.Getter;

public class CircularLinkedList {
    @Getter
    private Node first;
    private Node last;

    public void addNode(@NonNull Hole value) {
        if (value.getOwner() == null) {
            throw new RuntimeException("Cannot add a Hole node without its owner");
        }
        Node newNode = new Node(value);

        if (this.first == null) {
            this.first = newNode;
        } else {
            newNode.updateIndex(this.last.getIndex() + 1);
            this.last.setNext(newNode);
        }

        this.last = newNode;
        this.last.setNext(first);
    }

    public Optional<Hole> getFirstMatchOppositeHole(Hole searchValue) {
        if (this.first == null || searchValue.getOwner() == null) {
            return Optional.empty();
        }
        return getAsList()
                .stream()
                .filter(h -> h.getPosition() == searchValue.getPosition() &&
                        !h.isOwner(searchValue.getOwner()))
                .findFirst();
    }

    public Optional<Hole> getFirstMatchHole(Hole searchValue) {
        if (this.first == null || searchValue.getOwner() == null) {
            return Optional.empty();
        }
        return getAsList()
                .stream()
                .filter(h -> h.getPosition() == searchValue.getPosition() &&
                        h.isOwner(searchValue.getOwner()))
                .findFirst();
    }

    public Optional<Node> getFirstMatchNode(Hole searchValue) {
        Node currentNode = this.first;

        if (this.first == null || searchValue.getOwner() == null) {
            return Optional.empty();
        }

        do {
            if (currentNode.getPosition() == searchValue.getPosition()
                    && currentNode.getValue().isOwner(searchValue.getOwner())) {
                return Optional.of(currentNode);
            }
            currentNode = currentNode.getNext();
        } while (currentNode.getIndex() != this.first.getIndex());

        return Optional.empty();
    }

    public List<Hole> getAsList() {
        List<Hole> holes = new ArrayList<Hole>();

        Node currentNode = this.first;

        if (this.first == null) {
            return holes;
        }

        do {
            holes.add(currentNode.getValue());
            currentNode = currentNode.getNext();
        } while (currentNode.getIndex() != this.first.getIndex());

        return holes;
    }

    public int size() {
        return this.last != null ? this.last.getIndex() + 1 : 0;
    }
}
