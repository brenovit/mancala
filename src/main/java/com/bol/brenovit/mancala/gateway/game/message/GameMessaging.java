package com.bol.brenovit.mancala.gateway.game.message;

import com.bol.brenovit.mancala.core.game.dto.Game;
import com.bol.brenovit.mancala.core.game.mapper.GameMapper;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class GameMessaging {

    private SimpMessagingTemplate simpMessagingTemplate;

    @Retryable(maxAttempts = 3)
    public void post(Game game) {
        simpMessagingTemplate.convertAndSend("/topic/game-progress/" + game.getId(), GameMapper.map(game));
    }
}
