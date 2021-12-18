package com.bol.brenovit.mancala.core.game.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GamePlayer {
    private int index;
    private String id;
    private String name;
    
    public GamePlayer(String id, String name) {
        super();
        this.id = id;
        this.name = name;
    }
    
}
