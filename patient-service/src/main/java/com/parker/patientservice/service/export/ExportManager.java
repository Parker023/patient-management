package com.parker.patientservice.service.export;

import com.parker.patientservice.dto.PatientResponseDTO;
import com.parker.patientservice.dto.wrapper.BaseExportWrapper;
import com.parker.patientservice.dto.wrapper.PatientsWrapper;
import com.parker.patientservice.service.PatientService;
import com.parker.patientservice.service.export.strategy.ExportStrategyFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author shanmukhaanirudhtalluri
 * @date 07/07/25
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ExportManager {
    private final PatientService patientService;
    private final ExportStrategyFactory exportStrategyFactory;

    public byte[] export(String format, int num, int size, String sortFields, String order) {
        List<PatientResponseDTO> patients = patientService.getAllPatients(num, size, sortFields, order);
        BaseExportWrapper baseExportWrapper = createBaseExportWrapper(patients, num, size);
        return exportStrategyFactory.getExportStrategy(format)
                .export(baseExportWrapper);
    }

    private BaseExportWrapper createBaseExportWrapper(List<PatientResponseDTO> patients, int num, int size) {
        PatientsWrapper patientsWrapper = new PatientsWrapper();
        patientsWrapper.setPatients(patients);
        patientsWrapper.setPage(num);
        patientsWrapper.setSize(size);
        patientsWrapper.setTotalItems(patients.size());
        log.info("Total Patients : {}", patients.size());
        return patientsWrapper;
    }


}
