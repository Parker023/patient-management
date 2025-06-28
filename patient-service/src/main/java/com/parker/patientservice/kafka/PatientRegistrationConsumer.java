package com.parker.patientservice.kafka;

import com.google.protobuf.InvalidProtocolBufferException;
import com.parker.patientservice.dto.PatientRequestDTO;
import com.parker.patientservice.mapper.EntityDtoMapper;
import com.parker.patientservice.service.PatientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import patient.events.PatientEvent;
import patient.events.PatientRequest;

/**
 * @author shanmukhaanirudhtalluri
 * @date 28/06/25
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PatientRegistrationConsumer {
    private final EntityDtoMapper entityDtoMapper;
    private final PatientService patientService;

    @KafkaListener(topics = "patient-registration", groupId = "patient-service")
    public void consume(byte[] event) {
        try {
            PatientRequest patientRequest = PatientRequest.parseFrom(event);
            log.info("Received Patient Event from Kafka Consumer : {}", patientRequest.toString());
            PatientRequestDTO patientRequestDTO = translateToPatientRequestDTO(patientRequest);
            patientService.createPatient(patientRequestDTO);
            log.info("Created Patient Event from Kafka Consumer : {}", patientRequest.toString());
        } catch (InvalidProtocolBufferException e) {
            log.error("Error while parsing event", e);
        }

    }

    private PatientRequestDTO translateToPatientRequestDTO(PatientRequest patientRequest) {
        PatientRequestDTO patientRequestDTO = new PatientRequestDTO();
        patientRequestDTO.setName(patientRequest.getName());
        patientRequestDTO.setGender(patientRequest.getGender());
        patientRequestDTO.setEmail(patientRequest.getEmail());
        patientRequestDTO.setDateOfBirth(patientRequest.getDateOfBirth());
        patientRequestDTO.setAddress(patientRequest.getAddress());
        return patientRequestDTO;
    }

}
