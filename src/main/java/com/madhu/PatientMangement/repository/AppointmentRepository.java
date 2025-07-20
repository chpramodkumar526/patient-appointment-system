package com.madhu.PatientMangement.repository;

import com.madhu.PatientMangement.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    // find all by patient
    List<Appointment> findByPatientId(Long patientId);

    // optional: detect double-booking
    boolean existsByPatientIdAndDateTime(Long patientId, LocalDateTime dateTime);
}
