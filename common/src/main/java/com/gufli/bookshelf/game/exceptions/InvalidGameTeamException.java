package com.gufli.bookshelf.game.exceptions;

public class InvalidGameTeamException extends RuntimeException {
    public InvalidGameTeamException() {
        super();
    }

    public InvalidGameTeamException(String message) {
        super(message);
    }

    public InvalidGameTeamException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidGameTeamException(Throwable cause) {
        super(cause);
    }

    protected InvalidGameTeamException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
