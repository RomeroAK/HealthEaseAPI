package com.ayanda.HealthEaseApi.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "appointment_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long appointmentHistoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id", nullable = false)
    private Appointment appointment;

    @Column(name = "action", nullable = false)
    private String action; // CREATED, CONFIRMED, CANCELLED, RESCHEDULED, etc.

    @Column(name = "old_status")
    @Enumerated(EnumType.STRING)
    private Appointment.AppointmentStatus oldStatus;

    @Column(name = "new_status")
    @Enumerated(EnumType.STRING)
    private Appointment.AppointmentStatus newStatus;

    @Column(name = "old_date")
    private LocalDate oldDate;

    @Column(name = "new_date")
    private LocalDate newDate;

    @Column(name = "old_time")
    private LocalTime oldTime;

    @Column(name = "new_time")
    private LocalTime newTime;

    @Column(name = "reason", length = 500)
    private String reason;

    @Column(name = "performed_by", nullable = false)
    private String performedBy; // user ID or role

    @Column(name = "notes", length = 1000)
    private String notes;

    @Column(name = "updated_by")
    private String updatedBy;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
