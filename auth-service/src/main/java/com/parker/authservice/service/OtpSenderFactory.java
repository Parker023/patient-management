package com.parker.authservice.service;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author shanmukhaanirudhtalluri
 * @date 24/06/25
 */
@Component
public class OtpSenderFactory {
    private final Map<String, OtpSender> otpSenderMap;

    public OtpSenderFactory(final List<OtpSender> otpSenderList) {
        otpSenderMap = otpSenderList.stream()
                .collect(Collectors.toMap(OtpSender::getChannel, otpSender -> otpSender));

    }

    public OtpSender getOtpSender(String channel) {
        return Optional.ofNullable(otpSenderMap.get(channel))
                .orElseThrow(() -> new IllegalArgumentException("No OtpSender found for channel: " + channel));
    }
}
