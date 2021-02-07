package com.gufli.bookshelf.game.exceptions;

public class PlayerNotInGameException extends RuntimeException {

    public PlayerNotInGameException() {
        super();
    }

    public PlayerNotInGameException(String message) {
        super(message);
    }

    public PlayerNotInGameException(String message, Throwable cause) {
        super(message, cause);
    }

    public PlayerNotInGameException(Throwable cause) {
        super(cause);
    }

    protected PlayerNotInGameException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
