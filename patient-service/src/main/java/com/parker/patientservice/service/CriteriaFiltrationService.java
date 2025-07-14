package com.parker.patientservice.service;

import com.parker.patientservice.model.Patient;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

/**
 * @author Shanmukha Anirudh
 * @date 09/07/25
 */
@Service
public class CriteriaFiltrationService {
    public Specification<Patient> emailEquals(String email) {
        return (root, cq, cb) ->
                cb.equal(root.get("email"), email);
    }

}
