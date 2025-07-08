package com.parker.patientservice.service.export.strategy.impl;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.parker.patientservice.constants.PatientConstants;
import com.parker.patientservice.dto.wrapper.BaseExportWrapper;
import com.parker.patientservice.service.export.strategy.ExportStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author shanmukhaanirudhtalluri
 * @date 07/07/25
 */
@Slf4j
@Service("xmlStrategy")
@RequiredArgsConstructor
public class XmlExportStrategy implements ExportStrategy {
    private final XmlMapper xmlMapper;

    @Override
    public String getFormat() {
        return PatientConstants.XML.getValue();
    }

    @Override
    public byte[] export(BaseExportWrapper baseExportWrapper) {
        log.info("XML export begin !!");
        return new byte[0];
    }
}
