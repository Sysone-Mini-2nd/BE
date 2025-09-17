package com.sys.stm.global.exception;
/** 작성자: 배지원 */
public class BadRequestException extends RuntimeException {
    private final ExceptionMessage exceptionMessage;

    public BadRequestException(ExceptionMessage exceptionMessage) {
        super(exceptionMessage.getMessage());
        this.exceptionMessage = exceptionMessage;
    }
}
