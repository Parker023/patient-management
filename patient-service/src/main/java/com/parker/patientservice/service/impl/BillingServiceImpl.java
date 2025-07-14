package com.parker.patientservice.service.impl;

import billing.BillingResponse;
import com.parker.patientservice.grpc.BillingServiceGrpcClient;
import com.parker.patientservice.model.Patient;
import com.parker.patientservice.service.BillingService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Shanmukha Anirudh
 * @date 14/07/25
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class BillingServiceImpl implements BillingService {
    private final BillingServiceGrpcClient billingServiceGrpcClient;

    @CircuitBreaker(name = "billingCB", fallbackMethod = "fallbackBilling")
    @Override
    public BillingResponse createBillingAccount(Patient patient) {
        return billingServiceGrpcClient.createBillingAccount(patient.getId().toString(), patient.getName(), patient.getEmail());
    }

    public String fallbackBilling(String patientId, Throwable ex) {
        return "Billing service unavailable";
    }
}
