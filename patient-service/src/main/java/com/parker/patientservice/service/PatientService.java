package com.parker.patientservice.service;

import com.parker.patientservice.EntityDtoMapper;
import com.parker.patientservice.dto.PatientRequestDTO;
import com.parker.patientservice.dto.PatientResponseDTO;
import com.parker.patientservice.exception.EmailAlreadyExistsException;
import com.parker.patientservice.model.Patient;
import com.parker.patientservice.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final EntityDtoMapper entityDtoMapper;
    private final PatientRepository patientRepository;

    public List<PatientResponseDTO> getAllPatients() {
        return patientRepository
                .findAll()
                .stream()
                .filter(Objects::nonNull)
                .map(patient -> entityDtoMapper.toDto(patient, PatientResponseDTO.class))
                .toList();
    }

    public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO) throws EmailAlreadyExistsException {
        if(patientRepository.existsByEmail(patientRequestDTO.getEmail())) {
            throw new EmailAlreadyExistsException("A patient with this email is already exists : "+ patientRequestDTO.getEmail());
        }
        Patient patient = entityDtoMapper.toEntity(patientRequestDTO, Patient.class);
        generateIdAndSet(patient);
        setRegistrationDate(patient);
        patientRepository.save(patient);
        return entityDtoMapper.toDto(patient, PatientResponseDTO.class);
    }

    private void setRegistrationDate(Patient patient) {
        patient.setRegisteredDate(LocalDate.now());
    }

    private void generateIdAndSet(Patient patient) {
        patient.setId(UUID.randomUUID());
    }
}
