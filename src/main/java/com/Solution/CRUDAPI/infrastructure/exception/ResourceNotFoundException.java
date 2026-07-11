package com.Solution.CRUDAPI.infrastructure.exception;

// Exception thrown when a requested resource does not exist in the system.
// Used to return HTTP 404 from the API layer.
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
