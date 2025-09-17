package com.sys.stm.global.exception;

import lombok.Getter;
/** 작성자: 배지원 */
@Getter
public class ForbiddenException extends RuntimeException {
    private final ExceptionMessage exceptionMessage;

    public ForbiddenException(ExceptionMessage exceptionMessage) {
        super(exceptionMessage.getMessage());
        this.exceptionMessage = exceptionMessage;
    }
}


