package com.parker.authservice.exception.handler;

import com.parker.authservice.constants.AuthConstants;
import com.parker.authservice.exception.OtpVerificationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * @author shanmukhaanirudhtalluri
 * @date 29/06/25
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = OtpVerificationException.class)
    public ResponseEntity<Map<String, String>> handleOtpVerificationException(OtpVerificationException e) {
        Map<String, String> message = new HashMap<>();
        message.put(AuthConstants.MESSAGE.getValue(), e.getMessage());
        return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
