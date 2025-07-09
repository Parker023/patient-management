package com.parker.patientservice.service.export.strategy.impl;

import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.parker.patientservice.constants.PatientConstants;
import com.parker.patientservice.dto.PatientResponseDTO;
import com.parker.patientservice.dto.wrapper.BaseExportWrapper;
import com.parker.patientservice.exception.CsvParsingException;
import com.parker.patientservice.exception.DataNotAvailableException;
import com.parker.patientservice.service.export.strategy.ExportStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.StringWriter;
import java.util.ArrayList;

import static com.opencsv.ICSVWriter.NO_QUOTE_CHARACTER;

/**
 * @author shanmukhaanirudhtalluri
 * @date 07/07/25
 */
@Slf4j
@Service("csvStrategy")
public class CsvExportStrategy implements ExportStrategy {

    @Override
    public String getFormat() {
        return PatientConstants.CSV.getValue();
    }

    @Override
    public byte[] export(BaseExportWrapper baseExportWrapper) {
        if (baseExportWrapper.getData() == null || baseExportWrapper.getData().isEmpty()) {
            log.warn("Csv export begin !! No data to export");
            throw new DataNotAvailableException("No Data to export !!");
        }

        var writer = new StringWriter();
        var beanToCsv = new StatefulBeanToCsvBuilder<PatientResponseDTO>(writer)
                .withQuotechar(NO_QUOTE_CHARACTER)
                .build();
        try {
            ArrayList<PatientResponseDTO> patientResponseDTOS = new ArrayList<>(baseExportWrapper.getData());
            beanToCsv.write(patientResponseDTOS);
        } catch (CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e) {
            log.error("Error while exporting data to CSV", e);
            throw new CsvParsingException(e);
        }
        return writer.toString().getBytes();
    }

}
