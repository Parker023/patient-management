package com.parker.patientservice.controller;

import com.parker.patientservice.service.export.ExportManager;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author shanmukhaanirudhtalluri
 * @date 07/07/25
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/patient/export/")
@RequiredArgsConstructor
public class ExportController {
    private final ExportManager exportManager;

    @GetMapping(value = "patients", produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE, "text/csv"})
    public ResponseEntity<byte[]> exportPatients(@Valid @RequestParam String format, @RequestParam(defaultValue = "0") int num,
                                                 @RequestParam(defaultValue = "10") int size,
                                                 @RequestParam(defaultValue = "id") String sortFields,
                                                 @RequestParam(defaultValue = "asc") String order) {
        log.info("Exporting Patients in format {}", format);
        byte[] exportData = exportManager.export(format, num, size, sortFields, order);
        return ResponseEntity.ok(exportData);
    }
}
