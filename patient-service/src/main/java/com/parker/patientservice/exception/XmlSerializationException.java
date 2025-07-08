package com.parker.patientservice.exception;

import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * @author Shanmukha Anirudh
 * @date 08/07/25
 */
public class XmlSerializationException extends RuntimeException {
    public XmlSerializationException(JsonProcessingException e) {
    }
}
