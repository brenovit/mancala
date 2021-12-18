package com.bol.brenovit.mancala.core.game.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class Hole {
    private int position;
    private HoleType type;
    private int seeds;
    private GamePlayer owner;
    private int index;

    public void addSeeds(int quantity) {
        this.seeds += quantity;
    }

    public void clean() {
        this.seeds = 0;
    }

    public boolean isPot() {
        return this.type == HoleType.POT;
    }

    public boolean isPit() {
        return this.type == HoleType.PIT;
    }

    public boolean isOwner(GamePlayer player) {
        return player.getId().equals(this.owner.getId());
    }

    public String getOwnerName() {
        return this.owner.getName();
    }
}
