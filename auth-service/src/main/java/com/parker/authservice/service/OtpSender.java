package com.parker.authservice.service;

/**
 * @author shanmukhaanirudhtalluri
 * @date 24/06/25
 */
public interface OtpSender {
    public String getChannel();

    public void sendOtp(String destination, String otp);
}
