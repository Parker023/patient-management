package com.parker.patientservice.service;

import billing.BillingResponse;
import com.parker.patientservice.grpc.BillingServiceGrpcClient;
import com.parker.patientservice.kafka.KafkaProducer;
import com.parker.patientservice.mapper.EntityDtoMapper;
import com.parker.patientservice.dto.PatientRequestDTO;
import com.parker.patientservice.dto.PatientResponseDTO;
import com.parker.patientservice.exception.EmailAlreadyExistsException;
import com.parker.patientservice.exception.PatientNotFoundException;
import com.parker.patientservice.model.Patient;
import com.parker.patientservice.repository.PatientRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PatientService {
    private final EntityDtoMapper entityDtoMapper;
    private final BillingServiceGrpcClient billingServiceGrpcClient;
    private final PatientRepository patientRepository;
    private final KafkaProducer kafkaProducer;
    private final EntityManager entityManager;

    /**
     * Retrieves a list of all patients.
     *
     * @return a list of {@link PatientResponseDTO} objects representing all patients in the system.
     */
    public List<PatientResponseDTO> getAllPatients() {
        return patientRepository
                .findAll()
                .stream()
                .filter(Objects::nonNull)
                .map(patient -> entityDtoMapper.toDto(patient, PatientResponseDTO.class))
                .toList();
    }

    /**
     * Creates a new patient record, generates a unique identifier, sets the registration date,
     * and sends an event to kafka after creating a billing account.
     *
     * @param patientRequestDTO the data transfer object containing patient details needed to create a new patient.
     * @return a {@link PatientResponseDTO} containing the details of the newly created patient.
     * @throws EmailAlreadyExistsException if a patient with the same email address already exists.
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO) {
        if (patientRepository.existsByEmail(patientRequestDTO.getEmail())) {
            throw new EmailAlreadyExistsException("A patient with this email is already exists : " + patientRequestDTO.getEmail());
        }
        Patient patient = entityDtoMapper.toEntity(patientRequestDTO, Patient.class);
        generateIdAndSet(patient);
        setRegistrationDate(patient);
        entityManager.persist(patient);
        BillingResponse billingResponse = createBillingAccount(patient);
        log.info("Created billing account {} ", billingResponse.toString());
        kafkaProducer.sendEvent(patient);

        return entityDtoMapper.toDto(patient, PatientResponseDTO.class);
    }

    /**
     * Creates a billing account for the specified patient by sending their details
     * to the billing service.
     *
     * @param patient the patient object containing the ID, name, and email to be used
     *                for creating the billing account.
     * @return a {@link BillingResponse} object containing information about the
     *         created billing account.
     */
    private BillingResponse createBillingAccount(Patient patient) {
        return billingServiceGrpcClient.createBillingAccount(patient.getId().toString(), patient.getName(), patient.getEmail());
    }

    private void setRegistrationDate(Patient patient) {
        patient.setRegisteredDate(LocalDate.now());
    }

    /**
     * Updates an existing patient record identified by the given patient ID with the details provided in the PatientRequestDTO.
     * Performs validation to ensure email uniqueness and the existence of the patient before updating.
     *
     * @param patientRequestDTO the data transfer object containing updated patient details such as name, email, address, and date of birth.
     * @param patientId the unique identifier of the patient whose details need to be updated.
     * @return a {@link PatientResponseDTO} object containing the updated patient details.
     * @throws PatientNotFoundException if no patient is found with the specified ID.
     * @throws EmailAlreadyExistsException if another patient with the same email already exists.
     */
    public PatientResponseDTO updatePatient(PatientRequestDTO patientRequestDTO, UUID patientId) {
        Patient patient = patientRepository.findById(patientId).orElseThrow(() -> new PatientNotFoundException("Patient not found with ID : " + patientId));
        if (patientRepository.existsByEmailAndIdNot(patientRequestDTO.getEmail(), patientId)) {
            throw new EmailAlreadyExistsException("A patient with this email is already exists : " + patientRequestDTO.getEmail());
        }
        patient.setName(patientRequestDTO.getName());
        patient.setEmail(patientRequestDTO.getEmail());
        patient.setAddress(patientRequestDTO.getAddress());
        patient.setDateOfBirth(LocalDate.parse(patientRequestDTO.getDateOfBirth()));
        Patient updatedPatient = patientRepository.save(patient);
        return entityDtoMapper.toDto(updatedPatient, PatientResponseDTO.class);
    }

    /**
     * Generates a unique identifier using UUID and assigns it to the provided patient object.
     *
     * @param patient the patient object for which the unique identifier will be generated and set
     */
    private void generateIdAndSet(Patient patient) {
        patient.setId(UUID.randomUUID());
    }

    public void deletePatient(UUID patientId) {
        patientRepository.deleteById(patientId);
    }
}
