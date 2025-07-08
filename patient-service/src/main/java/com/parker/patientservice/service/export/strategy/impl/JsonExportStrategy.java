package com.parker.patientservice.service.export.strategy.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.parker.patientservice.constants.PatientConstants;
import com.parker.patientservice.dto.wrapper.BaseExportWrapper;
import com.parker.patientservice.exception.JsonParseException;
import com.parker.patientservice.service.export.strategy.ExportStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author shanmukhaanirudhtalluri
 * @date 07/07/25
 */
@Slf4j
@Service("jsonStrategy")
public class JsonExportStrategy implements ExportStrategy {
    @Override
    public String getFormat() {
        return PatientConstants.JSON.getValue();
    }

    @Override
    public byte[] export(BaseExportWrapper baseExportWrapper) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsBytes(baseExportWrapper);
        } catch (JsonProcessingException e) {
            log.error("Error while serializing data to JSON", e);
            throw new JsonParseException(e);
        }
    }
}
