package com.madhu.PatientMangement.controller;

import com.madhu.PatientMangement.dto.AppointmentDTO;
import com.madhu.PatientMangement.model.Appointment;
import com.madhu.PatientMangement.model.Patient;
import com.madhu.PatientMangement.repository.AppointmentRepository;
import com.madhu.PatientMangement.repository.PatientRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository   patientRepository;

    public AppointmentController(
        AppointmentRepository appointmentRepository,
        PatientRepository     patientRepository
    ) {
        this.appointmentRepository = appointmentRepository;
        this.patientRepository     = patientRepository;
    }

    // ——————————————————————————————————————————————————————————————
    // helper: always issue a 401 + Basic challenge

    //this is pramod
    private ResponseEntity<?> unauthorized() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.WWW_AUTHENTICATE, "Basic realm=\"PatientAPI\"");
        return new ResponseEntity<>(headers, HttpStatus.UNAUTHORIZED);
    }

    // ——————————————————————————————————————————————————————————————
    // map JPA → DTO
    private AppointmentDTO toDto(Appointment appt) {
        AppointmentDTO dto = new AppointmentDTO();
        dto.setId(appt.getId());
        dto.setPatientId(appt.getPatient().getId());
        dto.setDateTime(appt.getDateTime());
        dto.setDoctorName(appt.getDoctorName());
        dto.setStatus(appt.getStatus());
        dto.setNotes(appt.getNotes());
        return dto;
    }

    // map DTO → JPA (for POST)
    private Appointment fromDto(AppointmentDTO dto) {
        Patient patient = patientRepository.findById(dto.getPatientId())
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "No patient found with id=" + dto.getPatientId()
            ));

        Appointment appt = new Appointment();
        appt.setPatient(patient);
        appt.setDateTime(dto.getDateTime());
        appt.setDoctorName(dto.getDoctorName());
        appt.setStatus(dto.getStatus());
        appt.setNotes(dto.getNotes());
        return appt;
    }

    // ——————————————————————————————————————————————————————————————
    // GET /api/appointments
    @GetMapping
    public ResponseEntity<?> getAllAppointments(Principal principal) {
        if (principal == null) return unauthorized();

        List<AppointmentDTO> dtos = appointmentRepository.findAll()
            .stream()
            .map(this::toDto)
            .toList();
        return ResponseEntity.ok(dtos);
    }

    // GET /api/appointments/patient/{patientId}
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<?> getByPatient(
        Principal principal,
        @PathVariable Long patientId
    ) {
        if (principal == null) return unauthorized();

        List<AppointmentDTO> dtos = appointmentRepository.findByPatientId(patientId)
            .stream()
            .map(this::toDto)
            .toList();
        return ResponseEntity.ok(dtos);
    }

    // POST /api/appointments
    @PostMapping
    public ResponseEntity<?> createAppointment(
        Principal        principal,
        @RequestBody      AppointmentDTO dto
    ) {
        if (principal == null) return unauthorized();

        Appointment toSave = fromDto(dto);
        Appointment saved  = appointmentRepository.save(toSave);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(toDto(saved));
    }

    // DELETE /api/appointments/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAppointment(
        Principal principal,
        @PathVariable Long id
    ) {
        if (principal == null) return unauthorized();

        appointmentRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // PATCH /api/appointments/{id}/status
    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(
        Principal    principal,
        @PathVariable Long id,
        @RequestBody String newStatus
    ) {
        if (principal == null) return unauthorized();

        Appointment appt = appointmentRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "No appointment with id=" + id
            ));
        appt.setStatus(newStatus);
        Appointment updated = appointmentRepository.save(appt);
        return ResponseEntity.ok(toDto(updated));
    }
}
