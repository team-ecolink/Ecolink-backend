package com.ecolink.core.common.error.exception;

import com.ecolink.core.common.error.ErrorCode;
import com.ecolink.core.common.error.GeneralException;

public class AccessDeniedException extends GeneralException {
	public AccessDeniedException(ErrorCode errorCode) {
		super(errorCode);
	}
}
