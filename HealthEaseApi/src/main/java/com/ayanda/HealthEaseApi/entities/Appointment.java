package com.ayanda.HealthEaseApi.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

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

    // Patient and Doctor relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    // Appointment Details
    @Column(name = "appointment_date", nullable = false)
    private LocalDate appointmentDate;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "duration_minutes")
    private Integer durationMinutes;

    @Enumerated(EnumType.STRING)
    @Column(name = "appointment_type", nullable = false)
    private AppointmentType type;


    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AppointmentStatus status;

    // Appointment Information
    @NotBlank(message = "Reason for appointment is required")
    @Column(name = "reason", nullable = false, length = 500)
    private String reason;

    @Column(name = "treatment_plan")
    private String TreatmentPlan;

    @Column(name = "rescheduled_at")
    private LocalDateTime RescheduledAt;

    @ElementCollection
    @CollectionTable(name = "appointment_symptoms", joinColumns = @JoinColumn(name = "symptom_id"))
    @Column(name = "symptom")
    @Builder.Default
    private List<String> symptoms = new ArrayList<>();

    @Column(name = "notes", length = 1000)
    private String notes;

    @Column(name = "patient_notes", length = 1000)
    private String patientNotes;

    @Column(name = "doctor_notes", length = 1000)
    private String doctorNotes;

    // Location and Consultation Details
    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "consultation_mode")
    @Enumerated(EnumType.STRING)
    private ConsultationMode consultationMode;

    @Column(name = "virtual_meeting_link")
    private String virtualMeetingLink;

    @Column(name = "virtual_meeting_id")
    private String virtualMeetingId;

    // Financial Information
    @Column(name = "fee", precision = 8, scale = 2, nullable = false)
    private BigDecimal fee;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    private PaymentStatus paymentStatus;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "confirmed_at")
    private LocalDateTime ConfirmedAt;

    @Column(name = "payment_reference")
    private String paymentReference;

    // Appointment Management
    @Column(name = "confirmation_code", unique = true)
    private String confirmationCode;

    @Column(name = "reminder_sent")
    @Builder.Default
    private Boolean reminderSent = false;

    @Column(name = "reminder_sent_at")
    private LocalDateTime reminderSentAt;

    @Column(name = "check_in_time")
    private LocalDateTime checkInTime;

    @Column(name = "check_out_time")
    private LocalDateTime checkOutTime;

    // Cancellation and Rescheduling
    @Column(name = "cancellation_reason")
    private String cancellationReason;

    @Column(name = "cancelled_by")
    private String cancelledBy; // 'patient', 'doctor', 'system', 'admin'

    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;

    @Column(name = "rescheduled_from_id")
    private Long rescheduledFromId;

    @Column(name = "reschedule_reason")
    private String rescheduleReason;

    @Column(name = "reschedule_count")
    @Builder.Default
    private Integer rescheduleCount = 0;

    // Follow-up Information
    @Column(name = "follow_up_required")
    @Builder.Default
    private Boolean followUpRequired = false;

    @Column(name = "follow_up_date")
    private LocalDate followUpDate;

    @Column(name = "follow_up_instructions")
    private String followUpInstructions;

    // Prescriptions issued during this appointment
    @OneToMany(mappedBy = "appointment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Prescription> prescriptions = new ArrayList<>();

    // Appointment history and audit
    @OneToMany(mappedBy = "appointment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<AppointmentHistory> history = new ArrayList<>();

    // System fields
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;

    // Enums
    public enum AppointmentType {
        INITIAL_CONSULTATION,
        FOLLOW_UP,
        EMERGENCY,
        ROUTINE_CHECKUP,
        VIRTUAL_CONSULTATION,
        SECOND_OPINION,
        PROCEDURE,
        VACCINATION,
        HEALTH_SCREENING
    }

    public enum AppointmentStatus {
        PENDING,           // Waiting for doctor confirmation
        CONFIRMED,         // Confirmed by doctor
        CANCELLED,         // Cancelled by patient/doctor
        COMPLETED,         // Appointment completed
        NO_SHOW,          // Patient didn't show up
        RESCHEDULED,      // Rescheduled to another time
        IN_PROGRESS,      // Currently ongoing
        CHECKED_IN,       // Patient checked in
        BLOCKED, WAITING           // Patient waiting for doctor
    }

    public enum ConsultationMode {
        IN_PERSON,
        VIRTUAL_VIDEO,
        VIRTUAL_PHONE,
        HYBRID,
        TELEMEDICINE,
        BLOCKED, HOME_VISIT
    }

    public enum PaymentStatus {
        PENDING,
        PAID,
        PARTIALLY_PAID,
        REFUNDED,
        FAILED,
        REFUND_PENDING, CANCELLED
    }
}

