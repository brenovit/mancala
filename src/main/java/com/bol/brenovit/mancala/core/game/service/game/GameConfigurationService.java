package com.bol.brenovit.mancala.core.game.service.game;

import org.springframework.stereotype.Component;

import com.bol.brenovit.mancala.core.game.dto.GameConfiguration;

import lombok.Getter;

@Getter
@Component
public class GameConfigurationService {

    private final int maxHolesOnBoard;
    private final int inicialSeedByHole;

    public GameConfigurationService() {
        this.maxHolesOnBoard = 7;
        this.inicialSeedByHole = 6;
    }

    public GameConfiguration getDefaultConfig() {
        return new GameConfiguration(maxHolesOnBoard, inicialSeedByHole);
    }

    public GameConfiguration getDefaultHole(int seeds) {
        return new GameConfiguration(maxHolesOnBoard, seeds);
    }
}
