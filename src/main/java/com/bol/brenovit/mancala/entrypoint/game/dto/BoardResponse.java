package com.bol.brenovit.mancala.entrypoint.game.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardResponse {
    private List<HoleResponse> holes;
}
