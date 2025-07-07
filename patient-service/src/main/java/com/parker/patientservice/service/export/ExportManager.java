package com.parker.patientservice.service.export;

import com.parker.patientservice.dto.wrapper.BaseExportWrapper;
import com.parker.patientservice.service.export.strategy.ExportStrategyFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author shanmukhaanirudhtalluri
 * @date 07/07/25
 */
@Service
@RequiredArgsConstructor
public class ExportManager {
    private final ExportStrategyFactory exportStrategyFactory;

    public byte[] export(String format, BaseExportWrapper<?> baseExportWrapper) {
        return exportStrategyFactory.getExportStrategy(format).export(baseExportWrapper);
    }
}
