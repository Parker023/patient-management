package com.parker.patientservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author shanmukhaanirudhtalluri
 * @date 07/07/25
 */
@RestController
@RequestMapping("/api/v1/patient/export/")
@RequiredArgsConstructor
public class ExportController {

    @GetMapping(value = "patients", produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE, "text/csv"})
    public ResponseEntity<byte[]> exportPatients() {
        return ResponseEntity.ok().build();
    }
}
