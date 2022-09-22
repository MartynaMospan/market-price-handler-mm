package com.example.marketpricehandler.exception;

import lombok.Getter;

@Getter
public class AppException extends RuntimeException{

    public AppException(String message) {
        super(message);
    }
}
