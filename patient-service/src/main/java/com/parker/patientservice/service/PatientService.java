package com.parker.patientservice.service;

import com.parker.patientservice.EntityDtoMapper;
import com.parker.patientservice.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientService {
    private EntityDtoMapper entityDtoMapper;
    private final PatientRepository patientRepository;

}
