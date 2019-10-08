package com.cjyc.salesman.api.exception;

public class ParameterException extends RuntimeException {


    public ParameterException() {
        super();
    }

    public ParameterException(String message) {
        super(message);
    }

    public ParameterException(String message, String... args) {
        super(String.format(message, args));
    }
}
