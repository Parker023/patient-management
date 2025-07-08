package com.parker.patientservice.configuration;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.parker.patientservice.dto.PatientRequestDTO;
import com.parker.patientservice.model.Patient;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.time.LocalDate;

@Configuration
public class AppConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        Converter<String, LocalDate> toLocalDate = ctx -> ctx.getSource() == null
                ? null : LocalDate.parse(ctx.getSource());
        mapper.typeMap(PatientRequestDTO.class, Patient.class)
                .addMappings(m -> m.using(toLocalDate)
                        .map(PatientRequestDTO::getDateOfBirth, Patient::setDateOfBirth));

        return mapper;
    }


    @Bean
    public XmlMapper xmlMapper(Jackson2ObjectMapperBuilder builder) {
        return builder.createXmlMapper(true).build();
    }
}
