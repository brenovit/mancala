package com.bol.brenovit.mancala.entrypoint.game.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameRequest {
    @NotEmpty(message = "Field 'name' cannot be empty for game")
    private String name;
    @NotNull(message = "Field 'player' cannot be empty for game")
    private PlayerRequest player;
    private int seeds;
}
