package com.ayanda.HealthEaseApi.dto.dtoObjects;


import com.ayanda.HealthEaseApi.entities.Appointment;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentDto {

    private Long id;

    // Patient and Doctor information (read-only for responses)
    private PatientSummaryDto patient;
    private DoctorSummaryDto doctor;

    // For requests
    private Long patientId;
    private Long doctorId;

    // Appointment scheduling
    @NotNull(message = "Appointment date is required")
    @Future(message = "Appointment date must be in the future")
    private LocalDate appointmentDate;

    @NotNull(message = "Start time is required")
    private LocalTime startTime;

    @NotNull(message = "End time is required")
    private LocalTime endTime;

    private Integer durationMinutes;

    @NotNull(message = "Appointment type is required")
    private Appointment.AppointmentType type;

    private Appointment.AppointmentStatus status;

    // Appointment details
    @NotBlank(message = "Reason for appointment is required")
    @Size(min = 10, max = 500, message = "Reason must be between 10 and 500 characters")
    private String reason;

    private List<String> symptoms;

    private List<AppointmentHistoryDto> history;

    @Size(max = 1000, message = "Notes cannot exceed 1000 characters")
    private String notes;

    @Size(max = 1000, message = "Patient notes cannot exceed 1000 characters")
    private String patientNotes;

    @Size(max = 1000, message = "Doctor notes cannot exceed 1000 characters")
    private String doctorNotes;

    private String patientFirstName;

    private String patientLastName;

    private String patientEmail;

    private String patientPhone;

    private String doctorFirstName;

    private String doctorLastName;

    private String treatmentPlan;

    private String doctorPracticeName;

    // Location and consultation
    private String location;
    private Appointment.ConsultationMode consultationMode;
    private String virtualMeetingLink;
    private String virtualMeetingId;

    // Financial
    @DecimalMin(value = "0.0", message = "Fee must be positive")
    private BigDecimal fee;

    private Appointment.PaymentStatus paymentStatus;
    private String paymentMethod;
    private String paymentReference;

    // Management
    private String confirmationCode;
    private Boolean reminderSent;
    private LocalDateTime reminderSentAt;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;

    // Cancellation and rescheduling
    private String cancellationReason;
    private String cancelledBy;
    private LocalDateTime cancelledAt;
    private Long rescheduledFromId;
    private String rescheduleReason;
    private Integer rescheduleCount;

    // Follow-up
    private Boolean followUpRequired;
    private LocalDate followUpDate;
    private String followUpInstructions;

    // Related data
    private MedicalRecordSummaryDto medicalRecord;
    private List<PrescriptionSummaryDto> prescriptions;

    // System fields
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
    private LocalDateTime confirmedAt;
}
