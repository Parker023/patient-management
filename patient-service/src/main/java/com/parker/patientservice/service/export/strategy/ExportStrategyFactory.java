package com.parker.patientservice.service.export.strategy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author shanmukhaanirudhtalluri
 * @date 07/07/25
 */
@Component
@Slf4j
public class ExportStrategyFactory {
    private final Map<String, ExportStrategy> strategyMap;

    public ExportStrategyFactory(List<ExportStrategy> exportStrategies) {
        strategyMap = exportStrategies
                .stream()
                .collect(Collectors.toMap(ExportStrategy::getFormat, strategy -> strategy));
    }

    public ExportStrategy getExportStrategy(String format) {
        log.info("Retrieving export strategy for format {}", format);
        return strategyMap.get(format);
    }
}
