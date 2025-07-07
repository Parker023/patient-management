package com.parker.patientservice.service.export.strategy.impl;

import com.parker.patientservice.constants.PatientConstants;
import com.parker.patientservice.dto.wrapper.BaseExportWrapper;
import com.parker.patientservice.service.export.strategy.ExportStrategy;

/**
 * @author shanmukhaanirudhtalluri
 * @date 07/07/25
 */
public class CsvExportStrategy implements ExportStrategy {

    @Override
    public String getFormat() {
        return PatientConstants.CSV.getValue();
    }

    @Override
    public byte[] export(BaseExportWrapper<?> baseExportWrapper) {
        //TODO write business logic for Csv
        return new byte[0];
    }
}
