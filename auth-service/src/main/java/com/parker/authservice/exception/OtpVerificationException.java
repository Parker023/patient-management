package com.parker.authservice.exception;

/**
 * @author shanmukhaanirudhtalluri
 * @date 29/06/25
 */
public class OtpVerificationException extends RuntimeException {
    public OtpVerificationException(String message) {
        super(message);
    }

    public OtpVerificationException(String message, Exception e) {
        super(message, e);
    }
}
