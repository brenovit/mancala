package com.bol.brenovit.mancala.entrypoint.game.facade;

import java.util.List;

import javax.validation.Valid;

import org.springframework.stereotype.Component;

import com.bol.brenovit.mancala.core.common.mapper.CustomObjectMapper;
import com.bol.brenovit.mancala.core.game.dto.Game;
import com.bol.brenovit.mancala.core.game.dto.GameCreateTemplate;
import com.bol.brenovit.mancala.core.game.mapper.GameMapper;
import com.bol.brenovit.mancala.core.game.service.game.GameService;
import com.bol.brenovit.mancala.core.player.Player;
import com.bol.brenovit.mancala.entrypoint.game.dto.GameRequest;
import com.bol.brenovit.mancala.entrypoint.game.dto.GameResponse;
import com.bol.brenovit.mancala.entrypoint.game.dto.PlayerRequest;
import com.bol.brenovit.mancala.entrypoint.game.dto.SowRequest;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class GameControllerFacade {

    private GameService service;

    public List<GameResponse> list() {
        List<Game> games = service.findAll();
        return GameMapper.map(games);
    }

    public GameResponse findById(String id) {
        Game game = service.findById(id);
        return GameMapper.map(game);
    }

    public GameResponse create(GameRequest request) {
        GameCreateTemplate gameTemplate = CustomObjectMapper.map(request, GameCreateTemplate.class);
        Game game = service.create(gameTemplate);
        return GameMapper.map(game);
    }

    public GameResponse join(String id, PlayerRequest request) {
        Player player = CustomObjectMapper.map(request, Player.class);
        Game game = service.join(id, player);
        return GameMapper.map(game);
    }

    public GameResponse start(@Valid String gameId) {
        Game game = service.start(gameId);
        return GameMapper.map(game);
    }

    public GameResponse spectate(String id, PlayerRequest request) {
        Player player = CustomObjectMapper.map(request, Player.class);
        Game game = service.spectate(id, player);
        return GameMapper.map(game);
    }

    public GameResponse sow(String id, SowRequest request) {
        Game game = service.makeAMove(id, request.getHolePosition(), request.getPlayerId());
        return GameMapper.map(game);
    }

    public void abandon(String id, @Valid PlayerRequest request) {
        service.abandon(id, request.getId());
    }

}
