package com.cjyc.common.model.exception;

import java.text.MessageFormat;

public class ServerException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ServerException() {
        super();
    }

    public ServerException(String message) {
        super(message);
    }

    public ServerException(String message, Object... args) {
        super(MessageFormat.format(message, args));
    }

}
