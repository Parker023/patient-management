package com.parker.patientservice.controller;

import com.parker.patientservice.dto.PatientRequestDTO;
import com.parker.patientservice.dto.PatientResponseDTO;
import com.parker.patientservice.exception.EmailAlreadyExistsException;
import com.parker.patientservice.service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/patient/")
@RequiredArgsConstructor
public class PatientController {
    private final PatientService patientService;
    @GetMapping("all")
    public ResponseEntity<List<PatientResponseDTO>> getAllPatients(){
        List<PatientResponseDTO> allPatients = patientService.getAllPatients();
        return ResponseEntity.ok(allPatients);
    }
    @PostMapping("create")
    public ResponseEntity<PatientResponseDTO> createPatient(@Valid @RequestBody PatientRequestDTO patientRequestDTO) throws EmailAlreadyExistsException {
        PatientResponseDTO patientResponseDTO = patientService.createPatient(patientRequestDTO);
        return ResponseEntity.ok(patientResponseDTO);
    }
}
