package com.bol.brenovit.mancala.core.game.dto;

import com.bol.brenovit.mancala.core.player.Player;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameCreateTemplate {
    private String name;
    private Player player;
    private int seeds;
}
