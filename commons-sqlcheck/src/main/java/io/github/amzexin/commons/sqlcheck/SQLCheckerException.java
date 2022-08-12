package io.github.amzexin.commons.sqlcheck;

public class SQLCheckerException extends Exception {

    public SQLCheckerException() {
    }

    public SQLCheckerException(String message) {
        super(message);
    }

    public SQLCheckerException(String message, Throwable cause) {
        super(message, cause);
    }

    public SQLCheckerException(Throwable cause) {
        super(cause);
    }
}
