package com.ayanda.HealthEaseApi.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "patients")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "patient_id")
    private Long patientId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "gender")
    private String gender;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "alternate_phone_number")
    private String alternatePhoneNumber;

    @Column(name = "id_number", unique = true)
    private String idNumber;

    @Column(name = "occupation")
    private String occupation;

    @Column(name = "employer")
    private String employer;

    @Column(name = "marital_status")
    private String maritalStatus;

    @Column(name = "preferred_language")
    private String preferredLanguage;

    @Column(name = "profile_picture")
    private String profilePicture;

    // Address relationship
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id", referencedColumnName = "addressId")
    private Address address;

    // Medical history relationship
    @OneToOne(mappedBy = "patient", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private MedicalHistory medicalHistory;

    // Emergency contacts relationship
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<EmergencyContact> emergencyContacts;

    // Insurance relationship
    @OneToOne(mappedBy = "patient", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Insurance insurance;

    // Medical aid number (South Africa specific)
    @Column(name = "medical_aid_number")
    private String medicalAidNumber;

    // Preferences fields
    @Column(name = "preferred_doctor_gender")
    private String preferredDoctorGender;

    // Communication preferences
    @Column(name = "email_reminders")
    private Boolean emailReminders = true;

    @Column(name = "sms_reminders")
    private Boolean smsReminders = true;

    @Column(name = "appointment_confirmations")
    private Boolean appointmentConfirmations = true;

    @Column(name = "test_results")
    private Boolean testResults = true;

    @Column(name = "promotional_emails")
    private Boolean promotionalEmails = false;

    // Privacy settings
    @Column(name = "share_data_with_research")
    private Boolean shareDataWithResearch = false;

    @Column(name = "allow_marketing_communication")
    private Boolean allowMarketingCommunication = false;

    @Column(name = "profile_visibility")
    private String profileVisibility = "private";

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToMany(mappedBy = "patients", fetch = FetchType.LAZY)
    private List<Doctor> doctors;
}


