package com.madhu.PatientMangement.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Link back to a Patient
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @Column(nullable = false)
    private LocalDateTime dateTime;

    @Column(nullable = false)
    private String doctorName;

    @Column(nullable = false)
    private String status;    // e.g. "SCHEDULED", "CANCELLED"

    @Column(length = 1000)
    private String notes;     // optional notes

    // -- Constructors --
    public Appointment() {}

    public Appointment(Patient patient, LocalDateTime dateTime,
                       String doctorName, String status, String notes) {
        this.patient    = patient;
        this.dateTime   = dateTime;
        this.doctorName = doctorName;
        this.status     = status;
        this.notes      = notes;
    }

    // -- Getters & Setters --
    public Long getId()              { return id; }
    public void setId(Long id)       { this.id = id; }

    public Patient getPatient()      { return patient; }
    public void setPatient(Patient p){ this.patient = p; }

    public LocalDateTime getDateTime()            { return dateTime; }
    public void setDateTime(LocalDateTime dt)     { this.dateTime = dt; }

    public String getDoctorName()     { return doctorName; }
    public void setDoctorName(String d){ this.doctorName = d; }

    public String getStatus()         { return status; }
    public void setStatus(String s)   { this.status = s; }

    public String getNotes()          { return notes; }
    public void setNotes(String n)    { this.notes = n; }
}
