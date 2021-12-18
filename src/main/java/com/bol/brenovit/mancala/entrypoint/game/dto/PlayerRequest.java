package com.bol.brenovit.mancala.entrypoint.game.dto;

import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerRequest {
    @NotEmpty(message = "Field 'id' cannot be empty for player")
    private String id;
    @NotEmpty(message = "Field 'name' cannot be empty for player")
    private String name;
}
