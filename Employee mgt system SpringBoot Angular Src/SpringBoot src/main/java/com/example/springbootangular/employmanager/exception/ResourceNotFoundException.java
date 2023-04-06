package com.example.springbootangular.employmanager.exception;

public class ResourceNotFoundException extends RuntimeException{

    public ResourceNotFoundException (String message) {
        super(message);
    }
}
