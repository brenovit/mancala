package com.bol.brenovit.mancala.core.game.dto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Game {
    private String id;
    private final String name;
    private final GamePlayer player1;
    @Setter(value = AccessLevel.PRIVATE)
    private GamePlayer player2;
    private final Board board;
    @Setter(value = AccessLevel.PRIVATE)
    private GameStatus status;
    private GamePlayer turn;

    private GameConfiguration config;
    private List<GamePlayer> watchers;
    private String message;
    
    public Game(String name, GamePlayer player1, Board board, GameConfiguration gameConfig) {
        this.name = name;
        this.player1 = player1;
        this.player1.setIndex(0);
        this.turn = player1;
        this.board = board;
        this.config = gameConfig;
        this.watchers = new ArrayList<>();
    }

    public boolean isTurn(String playerId) {
        return this.turn.getId().equals(playerId);
    }

    public void swapTurn() {
        if (this.turn.getId().equals(this.player1.getId())) {
            this.turn = this.player2;
        } else {
            this.turn = this.player1;
        }
    }

    public void addPlayer(GamePlayer player) {
        if (player1 != null) {
            this.player2 = player;
            this.player2.setIndex(1);
        }
    }
    public void addWatcher(GamePlayer watcher) {
        if(!hasWatcher(watcher.getId())) {
            this.watchers.add(watcher);
        }
    }

    public void waiting() {
        this.status = GameStatus.WAITING;
    }

    public void finished() {
        this.status = GameStatus.FINISHED;
    }

    public void start() {
        this.status = GameStatus.STARTED;
    }

    public void ready() {
        this.status = GameStatus.READY;
    }

    public boolean isWaiting() {
        return this.status.equals(GameStatus.WAITING);
    }

    public boolean isReady() {
        return this.status.equals(GameStatus.READY);
    }

    public boolean isStarted() {
        return this.status.equals(GameStatus.STARTED);
    }

    public boolean isFinished() {
        return this.status.equals(GameStatus.FINISHED);
    }

    public boolean hasPlayer(String playerId) {
        return Arrays.asList(player1, player2)
                .stream()
                .filter(Objects::nonNull)
                .anyMatch(p -> playerId.equals(p.getId()));
    }
    
    public boolean hasWatcher(String watcherId) {
        return this.watchers
                .stream()
                .anyMatch(w -> w.getId().equals(watcherId));
    }

    public Optional<GamePlayer> findPlayerById(String playerId) {
        return Arrays.asList(player1, player2)
                .stream()
                .filter(Objects::nonNull)
                .filter(p -> p.getId().equals(playerId))
                .findFirst();
    }

    public void removeWatcher(String watcherId) {
        this.watchers.removeIf(w -> w.getId().equals(watcherId));
    }
}
