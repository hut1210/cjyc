package com.cjyc.salesman.api.exception;

import java.text.MessageFormat;

public class ParameterException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ParameterException() {
        super();
    }

    public ParameterException(String message) {
        super(message);
    }

    public ParameterException(String message, Object... args) {
        super(MessageFormat.format(message, args));
    }

}
