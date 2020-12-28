package com.gufli.bookshelf.game.exceptions;

public class InvalidPlayerGameStatusException extends RuntimeException {
    public InvalidPlayerGameStatusException() {
        super();
    }

    public InvalidPlayerGameStatusException(String message) {
        super(message);
    }

    public InvalidPlayerGameStatusException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidPlayerGameStatusException(Throwable cause) {
        super(cause);
    }

    protected InvalidPlayerGameStatusException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
