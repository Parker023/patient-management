package com.parker.patientservice.dto.wrapper;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.parker.patientservice.dto.PatientResponseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author shanmukhaanirudhtalluri
 * @date 06/07/25
 */
@EqualsAndHashCode(callSuper = true)
@Data
@JacksonXmlRootElement(localName = "patientsResponse")
public class PatientsWrapper extends BaseExportWrapper<PatientResponseDTO> {

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
