package com.bol.brenovit.mancala.core.common.error;

import java.text.MessageFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    RESOURCE_NOT_FOUND("101","Resource not found"),
    INVALID_ARGUMENT("102", "Invalid argument informed"),
    INTERNAL_SERVER_ERROR("103","Internal application error"),
    GAME_FULL("201", "The game is already full! Join as spector or create a new one!"),
    GAME_FINISH_WINNER("202","{0} won the game with {1} seeds!"),
    GAME_FINISH_DRAWN("203","{0} drawn the game with {1} seeds!"),
    GAME_NOT_PLAYER("204","You are not a player of this game!"),
    GAME_NOT_TURN("205","It's not your turn, wait until the opponent move!"),
    GAME_NOT_READY("206", "The game is not ready!"),
    GAME_NOT_STARTED("207","The game was not started yet!"),
    GAME_ABANDONED("208", "The player {0} abandoned the game!"),
    GAME_NOT_FOUND("209","Game not found"),
    HOLE_INNEXISTENT("301","You can't sow a innexistent pit"),
    HOLE_EMPTY("302","You can't sow from an empty pit"),
    HOLE_POT("303","You can't sow the pot"),
    WATCHER_CANNOT_SOW("401","Watchers cannot sow");
	
	private String code;
	private String message;
	
	public String getFormatedMessage(Object ... args) {
	    return MessageFormat.format(this.getMessage(), args);
	}
}
