package com.bol.brenovit.mancala.core.common.exception;

import java.text.MessageFormat;

import com.bol.brenovit.mancala.core.common.error.ErrorCode;

import lombok.Getter;

@Getter
public abstract class GenericApplicationException extends RuntimeException {
	
    private static final long serialVersionUID = 551874421782700255L;
        
	private final String code;	
	private String detail;
	private ErrorCode error;
			
	protected GenericApplicationException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.code = errorCode.getCode();
		this.error = errorCode;
	}
	
	protected GenericApplicationException(ErrorCode errorCode, Throwable ex) {
		super(errorCode.getMessage(), ex);
		this.code = errorCode.getCode();
		this.detail = ex.getMessage();
        this.error = errorCode;
	}
	
	protected GenericApplicationException(ErrorCode errorCode, Object ... extraMsg) {
		super(MessageFormat.format(errorCode.getMessage(),	extraMsg));
		this.code = errorCode.getCode();
        this.error = errorCode;
	}
		
	protected GenericApplicationException(ErrorCode errorCode, Throwable ex, Object extraMsg) {
		super(MessageFormat.format(errorCode.getMessage(),	extraMsg), ex);
		this.code = errorCode.getCode();
		this.detail = ex.getMessage();
        this.error = errorCode;
	}
	
	protected GenericApplicationException(ErrorCode errorCode, Throwable ex, String extraMsg) {
		super(MessageFormat.format(errorCode.getMessage(),	extraMsg), ex);
		this.code = errorCode.getCode();
		this.detail = ex.getMessage();
        this.error = errorCode;
	}
	
	@Override
	public String toString() {
		return String.format("%s : %s", this.code, super.getMessage());
	}
}
