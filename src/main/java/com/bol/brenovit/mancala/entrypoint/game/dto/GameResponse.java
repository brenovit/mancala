package com.bol.brenovit.mancala.entrypoint.game.dto;

import java.util.List;

import com.bol.brenovit.mancala.core.game.dto.GameStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameResponse {
    private String id;
    private String name;
    private GamePlayerResponse player1;
    private GamePlayerResponse player2;
    private BoardResponse board;
    private GameStatus status;
    private GamePlayerResponse turn;
    private List<GamePlayerResponse> watchers;
    private String message;
}
