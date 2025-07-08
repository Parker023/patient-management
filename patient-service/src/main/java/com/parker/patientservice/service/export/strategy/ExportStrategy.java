package com.parker.patientservice.service.export.strategy;

import com.parker.patientservice.dto.wrapper.BaseExportWrapper;
import com.parker.patientservice.exception.XmlSerializationException;

/**
 * @author shanmukhaanirudhtalluri
 * @date 07/07/25
 */
public interface ExportStrategy {
    String getFormat();

    byte[] export(BaseExportWrapper baseExportWrapper);
}
