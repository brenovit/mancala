package com.bol.brenovit.mancala.core.game.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GameConfiguration {
    private final int maxHolesOnBoard;
    private final int initialSeedByHole;
}
