// in com.madhu.PatientMangement.dto
package com.madhu.PatientMangement.dto;

import java.time.LocalDateTime;

public class AppointmentRequest {
    private Long patientId;
    private LocalDateTime dateTime;
    private String doctorName;
    private String status;
    private String notes;

    // default ctor, getters & setters
    public AppointmentRequest() {}
    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }
    public LocalDateTime getDateTime() { return dateTime; }
    public void setDateTime(LocalDateTime dateTime) { this.dateTime = dateTime; }
    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
