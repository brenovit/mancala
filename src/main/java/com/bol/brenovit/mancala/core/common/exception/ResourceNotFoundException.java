package com.bol.brenovit.mancala.core.common.exception;

import com.bol.brenovit.mancala.core.common.error.ErrorCode;

public class ResourceNotFoundException extends GenericApplicationException{

    private static final long serialVersionUID = -3311882857230495387L;

    public ResourceNotFoundException() {
		super(ErrorCode.RESOURCE_NOT_FOUND);
	}
    
    public ResourceNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

}
