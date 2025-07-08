package com.parker.patientservice.dto.wrapper;


import com.parker.patientservice.dto.PatientResponseDTO;

import java.util.List;

/**
 * Marker interface for classes that serve as wrappers for exportable data.
 * <p>
 * This interface is intended to be implemented by data wrapper classes
 * that are used for exporting data in various formats such as XML, JSON, or CSV.
 * It allows for a unified interface to be utilized by different export strategies.
 * <p>
 * Classes implementing this interface should encapsulate structured data that can be
 * processed by export services.
 */
public interface BaseExportWrapper {
    public List<PatientResponseDTO> getData();
}
