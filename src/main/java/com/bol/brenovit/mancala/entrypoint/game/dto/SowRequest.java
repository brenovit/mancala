package com.bol.brenovit.mancala.entrypoint.game.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SowRequest {
    @Min(value = 1)
    @NotNull(message = "Field 'holePosition' cannot be empty for sow")
    private Integer holePosition;
    @NotEmpty(message = "Field 'playerId' cannot be empty for sow")
    private String playerId;
}