package com.bol.brenovit.mancala.entrypoint.game.dto;

import com.bol.brenovit.mancala.core.game.dto.HoleType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HoleResponse {
    private int position;
    private HoleType type;
    private int seeds;
    private GamePlayerResponse owner;
    private int index;
}
