package com.ecolink.core.common.error;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

	/**
	 * Etc
	 */
	INVALID_TYPE_VALUE(HttpStatus.BAD_REQUEST, "E-001", "잘못된 요청입니다."),

	AVATAR_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "A-001", "주어진 식별자로 아바타를 찾을 수 없습니다."),

	STORE_MAIN_PHOTO_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "S-002", "가게의 메인 이미지를 찾을 수 없습니다.");
	
	private final HttpStatus httpStatus;
	private final String code;
	private final String message;
}
