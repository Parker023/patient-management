package com.parker.patientservice.service;

import billing.BillingResponse;
import com.parker.patientservice.dto.PatientRequestDTO;
import com.parker.patientservice.dto.PatientResponseDTO;
import com.parker.patientservice.exception.EmailAlreadyExistsException;
import com.parker.patientservice.exception.PatientNotFoundException;
import com.parker.patientservice.kafka.KafkaProducer;
import com.parker.patientservice.mapper.EntityDtoMapper;
import com.parker.patientservice.model.Patient;
import com.parker.patientservice.repository.PatientRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PatientService {
    private final EntityDtoMapper entityDtoMapper;
    private final PatientRepository patientRepository;
    private final KafkaProducer kafkaProducer;
    private final EntityManager entityManager;
    private final BillingService billingService;


    /**
     * Retrieves a paginated and sorted list of all patients.
     *
     * @param pageNum       the page number to retrieve, zero-based.
     * @param pageSize      the number of records per page.
     * @param sortFields    the comma-separated string of field names to sort by.
     * @param sortDirection the direction of sorting, either "ASC" for ascending or "DESC" for descending.
     * @return a list of {@link PatientResponseDTO} containing the details of the retrieved patients.
     */
    public List<PatientResponseDTO> getAllPatients(int pageNum, int pageSize, String sortFields, String sortDirection) {
        log.info("Getting all patients with pageNum {} and pageSize {} and sortFields {} and sortDirection {} ", pageNum, pageSize, sortFields, sortDirection);
        Pageable pageRequest = createPageRequest(pageNum, pageSize, sortFields, sortDirection);
        return patientRepository
                .findAll(pageRequest)
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
        BillingResponse billingResponse = billingService.createBillingAccount(patient);
        log.info("Created billing account {} ", billingResponse.toString());
        kafkaProducer.sendEvent(patient);

        return entityDtoMapper.toDto(patient, PatientResponseDTO.class);
    }


    private void setRegistrationDate(Patient patient) {
        patient.setRegisteredDate(LocalDate.now());
    }

    /**
     * Updates an existing patient record identified by the given patient ID with the details provided in the PatientRequestDTO.
     * Performs validation to ensure email uniqueness and the existence of the patient before updating.
     *
     * @param patientRequestDTO the data transfer object containing updated patient details such as name, email, address, and date of birth.
     * @param patientId         the unique identifier of the patient whose details need to be updated.
     * @return a {@link PatientResponseDTO} object containing the updated patient details.
     * @throws PatientNotFoundException    if no patient is found with the specified ID.
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

    public Pageable createPageRequest(int num, int size, String sortFields, String order) {
        Sort.Direction direction = Sort.Direction.fromOptionalString(order).orElse(Sort.Direction.ASC);
        Sort sort = Sort.by(Arrays.stream(sortFields.split(","))
                .map(String::trim)
                .map(field -> new Sort.Order(direction, field))
                .toList());
        return PageRequest.of(num, size, sort);
    }

    public void deletePatient(UUID patientId) {
        patientRepository.deleteById(patientId);
    }
}
