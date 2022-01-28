package com.lofi.studentrest.repository;

public class ElementAlreadyExistException extends RuntimeException {
    public ElementAlreadyExistException(String message) {
        super(message);
    }
}
