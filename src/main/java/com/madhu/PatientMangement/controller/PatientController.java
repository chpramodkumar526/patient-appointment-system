package com.madhu.PatientMangement.controller;

import com.madhu.PatientMangement.model.Patient;
import com.madhu.PatientMangement.repository.PatientRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private final PatientRepository patientRepository;

    public PatientController(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @GetMapping
    public ResponseEntity<?> getAllPatients(Principal principal, HttpServletRequest request) {
        if (principal == null) {
            HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.WWW_AUTHENTICATE, "Basic realm=\"PatientAPI\"");
            return new ResponseEntity<>(headers, HttpStatus.UNAUTHORIZED);
        }
        List<Patient> patients = patientRepository.findAll();
        return ResponseEntity.ok(patients);
    }

    @PostMapping
    public ResponseEntity<?> createPatient(
            Principal principal,
            @RequestBody Patient patient
    ) {
        if (principal == null) {
            HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.WWW_AUTHENTICATE, "Basic realm=\"PatientAPI\"");
            return new ResponseEntity<>(headers, HttpStatus.UNAUTHORIZED);
        }
        Patient saved = patientRepository.save(patient);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(saved);
    }
}
