package com.parker.patientservice.constants;

import lombok.Getter;

/**
 * @author shanmukhaanirudhtalluri
 * @date 24/06/25
 */
@Getter
public enum PatientConstants {
    EMAIL("email"), SMS("sms"), OTP_TTL_SECONDS("300"),OTP("otp");
    private final String value;

    PatientConstants(String value) {
        this.value = value;
    }

}
