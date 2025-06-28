package com.parker.authservice.kafka;

import auth.events.PatientEvent;
import auth.events.PatientRequest;
import com.parker.authservice.dto.RegistrationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

/**
 * @author shanmukhaanirudhtalluri
 * @date 28/06/25
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendToPatient(RegistrationRequest registrationRequest) {
        PatientRequest request = PatientRequest.newBuilder()
                .setEmail(registrationRequest.getEmail())
                .setName(registrationRequest.getName())
                .setGender(registrationRequest.getGender())
                .setAddress(registrationRequest.getAddress())
                .setDateOfBirth(registrationRequest.getDateOfBirth().format(DateTimeFormatter.ISO_DATE))
                .build();
        try {
            kafkaTemplate.send("patient-registration", request.toString());
        } catch (Exception e) {
            log.error("Error sending event to kafka : {}", request);
        }


    }
}
