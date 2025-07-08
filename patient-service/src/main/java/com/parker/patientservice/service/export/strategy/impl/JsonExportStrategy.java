package com.parker.patientservice.service.export.strategy.impl;

import com.parker.patientservice.constants.PatientConstants;
import com.parker.patientservice.dto.wrapper.BaseExportWrapper;
import com.parker.patientservice.service.export.strategy.ExportStrategy;
import org.springframework.stereotype.Service;

/**
 * @author shanmukhaanirudhtalluri
 * @date 07/07/25
 */
@Service("jsonStrategy")
public class JsonExportStrategy implements ExportStrategy {
    @Override
    public String getFormat() {
        return PatientConstants.JSON.getValue();
    }

    @Override
    public byte[] export(BaseExportWrapper baseExportWrapper) {
        //TODO write business logic for json
        return new byte[0];
    }
}
