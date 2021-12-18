package com.bol.brenovit.mancala.gateway.game.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.bol.brenovit.mancala.core.game.dto.Game;

@Repository
public class GameRepository {

    private Map<String, Game> games;

    public GameRepository() {
        games = new HashMap<>();
    }

    public List<Game> findAll() {
        return games.entrySet().stream().map(e -> e.getValue()).collect(Collectors.toList());
    }

    public Game save(Game game) {
        if (!StringUtils.hasText(game.getId())) {
            game.setId(UUID.randomUUID().toString());
        }

        synchronized (games) {
            games.put(game.getId(), game);
        }
        if (this.games.size() > 10) {
            this.games.entrySet()
                    .stream()
                    .map(e -> e.getValue())
                    .filter(e -> e.isFinished())
                    .forEach(e -> this.remove(e.getId()));
        }
        return game;
    }

    public Optional<Game> findById(String gameId) {
        synchronized (games) {
            return Optional.ofNullable(games.get(gameId));
        }
    }

    public void remove(String gameId) {
        synchronized (games) {
            games.remove(gameId);
        }
    }
}
