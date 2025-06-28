package com.parker.authservice.constants;

import lombok.Getter;

/**
 * @author shanmukhaanirudhtalluri
 * @date 24/06/25
 */
@Getter
public enum AuthConstants {
    EMAIL("email"), SMS("sms"), OTP_TTL_SECONDS("300"), OTP("otp"),
    OTP_VERIFIED("OTP Verified"), INVALID_OTP("Invalid OTP"),
    EMAIL_TEMPLATE("email-otp-template.txt"), MESSAGE("message"),
    PENDING("pending:email"), COLON(":");
    private final String value;

    AuthConstants(String value) {
        this.value = value;
    }

}
