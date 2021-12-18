package com.bol.brenovit.mancala.core.game.exception;

import com.bol.brenovit.mancala.core.common.error.ErrorCode;
import com.bol.brenovit.mancala.core.common.exception.GenericApplicationException;

public class GameErrorException extends GenericApplicationException {

    private static final long serialVersionUID = 2115981815946269282L;

    public GameErrorException(ErrorCode errorCode) {
        super(errorCode);
    }
    
    public GameErrorException(ErrorCode errorCode, Object ... args) {
        super(errorCode, args);
    }

}
