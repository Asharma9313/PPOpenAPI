package com.pulsepoint.commons.exception;

public class InvalidDataException extends RuntimeException{
    public InvalidDataException(String errorMsg){
        super(errorMsg);
        System.out.println(errorMsg);
    }
}
