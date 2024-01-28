package com.casseautomatiche.gdo.utilities.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyRuntimeException extends RuntimeException{

    private final int errorCode;

    public MyRuntimeException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

}
