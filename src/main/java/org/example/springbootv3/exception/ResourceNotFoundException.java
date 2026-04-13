package org.example.springbootv3.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    private String resourceName;
    private String fieldName;
    private Object fieldValue;

    // Konstruktor główny
    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s nie znaleziono z %s : '%s'", resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    // Konstruktor pomocniczy - tylko wiadomość
    public ResourceNotFoundException(String message) {
        super(message);
    }

    // Konstruktor pomocniczy - wiadomość i przyczyna
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    // Gettery
    public String getResourceName() {
        return resourceName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Object getFieldValue() {
        return fieldValue;
    }
}