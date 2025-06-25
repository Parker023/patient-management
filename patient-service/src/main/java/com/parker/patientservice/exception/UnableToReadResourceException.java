package com.parker.patientservice.exception;

/**
 * @author shanmukhaanirudhtalluri
 * @date 25/06/25
 */
public class UnableToReadResourceException extends RuntimeException {
    public UnableToReadResourceException(String message, Exception e) {
        super(message, e);
    }
}
