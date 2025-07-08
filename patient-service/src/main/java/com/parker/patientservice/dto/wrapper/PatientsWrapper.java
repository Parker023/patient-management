package com.parker.patientservice.dto.wrapper;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.parker.patientservice.dto.PatientResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author shanmukhaanirudhtalluri
 * @date 06/07/25
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JacksonXmlRootElement(localName = "patientsResponse")
public class PatientsWrapper implements BaseExportWrapper {

    private int page;
    private int size;
    private long totalItems;
    private int totalPages;

    @JacksonXmlElementWrapper(localName = "patients")
    @JacksonXmlProperty(localName = "patient")
    private List<PatientResponseDTO> patients;


    @Override
    public List<PatientResponseDTO> getData() {
        return patients;
    }
}
