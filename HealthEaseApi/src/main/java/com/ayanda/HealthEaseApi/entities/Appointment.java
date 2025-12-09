package com.ayanda.HealthEaseApi.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long appointmentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @Column(name = "appointment_date", nullable = false)
    private LocalDateTime appointmentDateTime; // Changed from LocalDate to LocalDateTime

    @Column(name = "appointment_type", nullable = false)
    private String appointmentType;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "reason", nullable = false, length = 500)
    private String reason;

    public enum AppointmentType {
        INITIAL_CONSULTATION,
        FOLLOW_UP,
        EMERGENCY,
        ROUTINE_CHECKUP,
        VIRTUAL_CONSULTATION,
        SECOND_OPINION,
        PROCEDURE,
        VACCINATION,
        HEALTH_SCREENING,
        IN_PERSON;

        public static AppointmentType getAppointmentType(String type) {
            for (AppointmentType appointmentType : AppointmentType.values()) {
                if (appointmentType.name().equalsIgnoreCase(type)) {
                    return appointmentType;
                }
            }
            throw new IllegalArgumentException("Invalid appointment type: " + type);
        }
    }

    public enum AppointmentStatus {
        PENDING,
        CONFIRMED,
        CANCELLED,
        COMPLETED,
        NO_SHOW,
        RESCHEDULED,
        IN_PROGRESS,
        CHECKED_IN,
        BLOCKED,
        WAITING
    }
}
