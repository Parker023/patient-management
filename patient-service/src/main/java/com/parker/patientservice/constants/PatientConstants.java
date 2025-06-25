package com.parker.patientservice.constants;

import lombok.Getter;

/**
 * @author shanmukhaanirudhtalluri
 * @date 24/06/25
 */
@Getter
public enum PatientConstants {
    EMAIL("email"), SMS("sms"), OTP_TTL_SECONDS("300"), OTP("otp"),
    OTP_VERIFIED("OTP Verified"), INVALID_OTP("Invalid OTP"),
    EMAIL_TEMPLATE("email-otp-template.txt");
    private final String value;

    PatientConstants(String value) {
        this.value = value;
    }

}
