package com.parker.patientservice.service;

import com.parker.patientservice.EntityDtoMapper;
import com.parker.patientservice.dto.PatientRequestDTO;
import com.parker.patientservice.dto.PatientResponseDTO;
import com.parker.patientservice.exception.EmailAlreadyExistsException;
import com.parker.patientservice.exception.PatientNotFoundException;
import com.parker.patientservice.model.Patient;
import com.parker.patientservice.repository.PatientRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final EntityDtoMapper entityDtoMapper;
    private final PatientRepository patientRepository;
    @PersistenceContext
    private EntityManager entityManager;

    public List<PatientResponseDTO> getAllPatients() {
        return patientRepository
                .findAll()
                .stream()
                .filter(Objects::nonNull)
                .map(patient -> entityDtoMapper.toDto(patient, PatientResponseDTO.class))
                .toList();
    }

    @Transactional
    public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO) {
        if (patientRepository.existsByEmail(patientRequestDTO.getEmail())) {
            throw new EmailAlreadyExistsException("A patient with this email is already exists : " + patientRequestDTO.getEmail());
        }
        Patient patient = entityDtoMapper.toEntity(patientRequestDTO, Patient.class);
        generateIdAndSet(patient);
        setRegistrationDate(patient);
        entityManager.persist(patient);
        return entityDtoMapper.toDto(patient, PatientResponseDTO.class);
    }

    private void setRegistrationDate(Patient patient) {
        patient.setRegisteredDate(LocalDate.now());
    }

    public PatientResponseDTO updatePatient(PatientRequestDTO patientRequestDTO, UUID patientId) {
        Patient patient = patientRepository.findById(patientId).orElseThrow(() -> new PatientNotFoundException("Patient not found with ID : " + patientId));
        if (patientRepository.existsByEmailAndIdNot(patientRequestDTO.getEmail(),patientId)) {
            throw new EmailAlreadyExistsException("A patient with this email is already exists : " + patientRequestDTO.getEmail());
        }
        patient.setName(patientRequestDTO.getName());
        patient.setEmail(patientRequestDTO.getEmail());
        patient.setAddress(patientRequestDTO.getAddress());
        patient.setDateOfBirth(LocalDate.parse(patientRequestDTO.getDateOfBirth()));
        Patient updatedPatient = patientRepository.save(patient);
        return entityDtoMapper.toDto(updatedPatient, PatientResponseDTO.class);
    }

    private void generateIdAndSet(Patient patient) {
        patient.setId(UUID.randomUUID());
    }
    public void deletePatient(UUID patientId) {
        patientRepository.deleteById(patientId);
    }
}
