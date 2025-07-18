package com.parker.patientservice.service.export.strategy.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.parker.patientservice.constants.PatientConstants;
import com.parker.patientservice.dto.wrapper.BaseExportWrapper;
import com.parker.patientservice.exception.XmlSerializationException;
import com.parker.patientservice.service.export.strategy.ExportStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author shanmukhaanirudhtalluri
 * @date 07/07/25
 */
@Slf4j
@Service("xmlStrategy")
@RequiredArgsConstructor
public class XmlExportStrategy implements ExportStrategy {
    private final XmlMapper xmlMapper;

    /**
     * Retrieves the format in which data will be exported.
     *
     * @return a string value representing the format, such as "xml".
     */
    @Override
    public String getFormat() {
        return PatientConstants.XML.getValue();
    }

    /**
     * Exports the provided data in XML format using the given wrapper.
     *
     * @param baseExportWrapper the wrapper containing the data to be exported
     * @return a byte array representing the XML-serialized data
     * @throws XmlSerializationException if an error occurs during XML serialization
     */
    @Override

    public byte[] export(BaseExportWrapper baseExportWrapper) {
        log.info("XML export begin !!");
        try {
            return xmlMapper.writeValueAsBytes(baseExportWrapper);
        } catch (JsonProcessingException e) {
            log.error("Error while serializing data to XML", e);
            throw new XmlSerializationException(e);
        }
    }
}
