package com.bol.brenovit.mancala.entrypoint.error.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.bol.brenovit.mancala.core.common.error.ErrorCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
public class EntryPointError {

	private LocalDateTime timestamp;
	private String code;
	private String message;
	private String detail;
	private List<EntryPointError> errors;

	public EntryPointError(ErrorCode errorCode, String detail) {
		this.timestamp = LocalDateTime.now();
		this.code = errorCode.getCode();
		this.message = errorCode.getMessage();
		this.detail = detail;
	}

	public EntryPointError(ErrorCode errorCode) {
		this.timestamp = LocalDateTime.now();
		this.code = errorCode.getCode();
		this.message = errorCode.getMessage();
	}

	public void addErro(EntryPointError error) {
		if (this.errors == null) {
			this.errors = new ArrayList<>();
		}
		this.errors.add(error);
	}
}
