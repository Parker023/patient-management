package com.parker.patientservice.service;

import billing.BillingResponse;
import com.parker.patientservice.dto.PatientRequestDTO;
import com.parker.patientservice.dto.PatientResponseDTO;
import com.parker.patientservice.exception.EmailAlreadyExistsException;
import com.parker.patientservice.grpc.BillingServiceGrpcClient;
import com.parker.patientservice.kafka.KafkaProducer;
import com.parker.patientservice.mapper.EntityDtoMapper;
import com.parker.patientservice.model.Patient;
import com.parker.patientservice.repository.PatientRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author shanmukhaanirudhtalluri
 * @date 05/07/25
 */
@ExtendWith(MockitoExtension.class)
class PatientServiceTest {
    @Mock
    private EntityDtoMapper entityDtoMapper;
    @Mock
    private BillingServiceGrpcClient billingServiceGrpcClient;
    @Mock
    private PatientRepository patientRepository;
    @Mock
    private KafkaProducer kafkaProducer;
    @Mock
    private EntityManager entityManager;
    @InjectMocks
    PatientService patientService;

    Patient patient;
    PatientResponseDTO patientResponseDTO;
    PatientRequestDTO patientRequestDTO;

    @BeforeEach
    void setUp() {
        UUID generatedId = UUID.randomUUID();
        patient = new Patient();
        patient.setId(generatedId);
        patient.setEmail("abc@gmail.com");
        patient.setName("abc");
        patient.setAddress("bangalore");
        patient.setDateOfBirth(LocalDate.parse("1999-11-23"));


        patientResponseDTO = new PatientResponseDTO();
        patientResponseDTO.setId(generatedId.toString());
        patientResponseDTO.setName("abc");
        patientResponseDTO.setAddress("bangalore");
        patientResponseDTO.setEmail("abc@gmail.com");
        patientResponseDTO.setDateOfBirth("1999-11-23");


        patientRequestDTO = new PatientRequestDTO();
        patientRequestDTO.setName("abc");
        patientRequestDTO.setAddress("bangalore");
        patientRequestDTO.setEmail("abc@gmail.com");
        patientRequestDTO.setDateOfBirth("1999-11-23");
        patientRequestDTO.setGender("Male");
    }


    @Test
    void getAllPatients() {


        when(entityDtoMapper.toDto(patient, PatientResponseDTO.class)).thenReturn(patientResponseDTO);
        when(patientRepository.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(patient)));
        List<PatientResponseDTO> result = patientService.getAllPatients(0, 10, "id", "asc");

        assertEquals(1, result.size());
        PatientResponseDTO actual = result.getFirst();
        assertEquals(patientResponseDTO.getName(), actual.getName());
        assertEquals(patientResponseDTO.getEmail(), actual.getEmail());
        assertEquals(patientResponseDTO.getAddress(), actual.getAddress());
        assertEquals(patientResponseDTO.getDateOfBirth(), actual.getDateOfBirth());
        assertEquals(patientResponseDTO.getId(), actual.getId());

    }

    @Test
    void createPatient_success() {

        when(patientRepository.existsByEmail(anyString())).thenReturn(false);
        when(entityDtoMapper.toEntity(patientRequestDTO, Patient.class)).thenReturn(patient);
        when(entityDtoMapper.toDto(patient, PatientResponseDTO.class)).thenReturn(patientResponseDTO);
        when(billingServiceGrpcClient.createBillingAccount(anyString(), anyString(), anyString()))
                .thenReturn(BillingResponse.newBuilder().setAccountId("ABC").setStatus("Active").build());


        PatientResponseDTO actual = patientService.createPatient(patientRequestDTO);

        ArgumentCaptor<Patient> patientCaptor = ArgumentCaptor.forClass(Patient.class);
        verify(entityManager).persist(patientCaptor.capture());
        Patient captured = patientCaptor.getValue();

        verify(kafkaProducer).sendEvent(patient);
        verify(billingServiceGrpcClient).createBillingAccount(patient.getId().toString(), patient.getName(), patient.getEmail());

        assertAll(
                () -> assertNotNull(captured.getId(), "Expected ID to be set before persisting"),
                () -> assertNotNull(captured.getRegisteredDate(), "Expected registration date to be set before persisting"),
                () -> assertNotNull(actual),
                () -> assertNotNull(actual.getId()),
                () -> assertEquals(patientResponseDTO.getName(), actual.getName()),
                () -> assertEquals(patientResponseDTO.getEmail(), actual.getEmail()),
                () -> assertEquals(patientResponseDTO.getAddress(), actual.getAddress()),
                () -> assertEquals(patientResponseDTO.getDateOfBirth(), actual.getDateOfBirth()),
                () -> assertEquals(patientResponseDTO.getId(), actual.getId())
        );


    }

    @Test
    void createPatient_fail() {
        when(patientRepository.existsByEmail(anyString()))
                .thenReturn(Boolean.TRUE);
        EmailAlreadyExistsException emailAlreadyExistsException =
                assertThrows(EmailAlreadyExistsException.class, () -> patientService.createPatient(patientRequestDTO));
        assertEquals("A patient with this email is already exists : " + patientRequestDTO.getEmail()
                , emailAlreadyExistsException.getMessage());
    }

    @Test
    void updatePatient() {
        assertTrue(true);
    }

    @Test
    void deletePatient_success() {

        patientService.deletePatient(patient.getId());

        verify(patientRepository).deleteById(patient.getId());
    }

}