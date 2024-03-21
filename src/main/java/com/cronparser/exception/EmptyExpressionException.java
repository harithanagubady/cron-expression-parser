package com.cronparser.exception;

public class EmptyExpressionException extends Exception {
    public EmptyExpressionException() {

    }
    public EmptyExpressionException(String message) {
        super(message);
    }

    public EmptyExpressionException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmptyExpressionException(Throwable cause) {
        super(cause);
    }
}
