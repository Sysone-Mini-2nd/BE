package com.sys.stm.global.exception;

import lombok.Getter;
/** 작성자: 배지원 */
@Getter
public class AuthenticationException extends RuntimeException {

    private final ExceptionMessage exceptionMessage;

    public AuthenticationException(ExceptionMessage exceptionMessage) {
        super(exceptionMessage.getMessage());
        this.exceptionMessage = exceptionMessage;
    }

}
