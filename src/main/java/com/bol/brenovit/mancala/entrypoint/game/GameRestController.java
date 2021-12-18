package com.bol.brenovit.mancala.entrypoint.game;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bol.brenovit.mancala.entrypoint.game.dto.GameRequest;
import com.bol.brenovit.mancala.entrypoint.game.dto.GameResponse;
import com.bol.brenovit.mancala.entrypoint.game.dto.PlayerRequest;
import com.bol.brenovit.mancala.entrypoint.game.dto.SowRequest;
import com.bol.brenovit.mancala.entrypoint.game.facade.GameControllerFacade;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/games")
@AllArgsConstructor
public class GameRestController {

    private GameControllerFacade facade;

    @GetMapping("")
    public ResponseEntity<List<GameResponse>> list() {
        return ResponseEntity.ok(facade.list());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GameResponse> findById(@PathVariable String id) {
        return ResponseEntity.ok(facade.findById(id));
    }

    @PostMapping("")
    public ResponseEntity<GameResponse> create(
            @Valid @RequestBody GameRequest request) {
        return ResponseEntity.ok(facade.create(request));
    }

    @PostMapping("/join/{id}")
    public ResponseEntity<GameResponse> join(
            @PathVariable String id,
            @Valid @RequestBody PlayerRequest request) {
        return ResponseEntity.ok(facade.join(id, request));
    }

    @PostMapping("/start/{id}")
    public ResponseEntity<GameResponse> start(
            @PathVariable String id) {
        return ResponseEntity.ok(facade.start(id));
    }

    @PostMapping("/sow/{id}")
    public ResponseEntity<GameResponse> sow(
            @PathVariable String id,
            @Valid @RequestBody SowRequest request) {
        return ResponseEntity.ok(facade.sow(id, request));
    }

    @PostMapping("/spectate/{id}")
    public ResponseEntity<GameResponse> spectate(
            @PathVariable String id,
            @Valid @RequestBody PlayerRequest request) {
        return ResponseEntity.ok(facade.spectate(id, request));
    }

    @PostMapping("/abandon/{id}")
    public ResponseEntity<Object> abandon(
            @PathVariable String id,
            @Valid @RequestBody PlayerRequest request) {
        facade.abandon(id, request);
        return ResponseEntity.noContent().build();
    }
}
