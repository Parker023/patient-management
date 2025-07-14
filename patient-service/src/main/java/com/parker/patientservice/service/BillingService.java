package com.parker.patientservice.service;

import billing.BillingResponse;
import com.parker.patientservice.model.Patient;

/**
 * @author Shanmukha Anirudh
 * @date 14/07/25
 */
public interface BillingService {
    public BillingResponse createBillingAccount(Patient patient);
}
