package com.bol.brenovit.mancala.core.game.exception;

import com.bol.brenovit.mancala.core.common.error.ErrorCode;
import com.bol.brenovit.mancala.core.common.exception.GenericApplicationException;

public class SowException extends GenericApplicationException {

    private static final long serialVersionUID = 1258899454747237378L;

    public SowException(ErrorCode erroCode) {
        super(erroCode);
    }

}
