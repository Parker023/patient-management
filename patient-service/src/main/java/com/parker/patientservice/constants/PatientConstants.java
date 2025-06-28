package com.parker.patientservice.constants;

import lombok.Getter;

/**
 * @author shanmukhaanirudhtalluri
 * @date 24/06/25
 */
@Getter
public enum PatientConstants {
    PATIENT("patient"), MESSAGE("message");
    private final String value;


    PatientConstants(String value) {
        this.value = value;
    }

}
