package com.bol.brenovit.mancala.core.player;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Player {
    
    private String id;
    private String name;
    
    public Player(String id, String name) {
        super();
        this.id = id;
        this.name = name;
    }
    
}
