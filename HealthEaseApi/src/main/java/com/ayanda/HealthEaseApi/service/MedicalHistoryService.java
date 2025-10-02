package com.ayanda.HealthEaseApi.service;

import com.ayanda.HealthEaseApi.dto.dtoObjects.*;
import com.ayanda.HealthEaseApi.entities.Doctor;
import com.ayanda.HealthEaseApi.entities.MedicalHistory;
import com.ayanda.HealthEaseApi.entities.Patient;
import com.ayanda.HealthEaseApi.entities.User;
import com.ayanda.HealthEaseApi.repos.DoctorRepository;
import com.ayanda.HealthEaseApi.repos.MedicalHistoryRepository;
import com.ayanda.HealthEaseApi.repos.PatientRepository;
import com.ayanda.HealthEaseApi.repos.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedicalHistoryService {

    private final MedicalHistoryRepository medicalHistoryRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;

    public MedicalHistoryDto createMedicalHistory(MedicalHistoryDto medicalHistoryDto) {
        PatientSummaryDto patient = medicalHistoryDto.getPatient();
        DoctorSummaryDto doctor = medicalHistoryDto.getDoctor();

        Patient entityPatient = patientRepository.findByPatientId(patient.getId())
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + patient.getId()));

        Doctor entityDoctor = doctorRepository.findByDoctorId(doctor.getId())
                .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + doctor.getId()));
        medicalHistoryDto.setDoctor(DoctorSummaryDto.fromEntity(entityDoctor));
        medicalHistoryDto.setPatient(PatientSummaryDto.fromEntity(entityPatient));
        MedicalHistory medicalHistory = mapToEntity(medicalHistoryDto);
        MedicalHistory savedHistory = medicalHistoryRepository.save(medicalHistory);
        return mapToDto(savedHistory);
    }

    public List<MedicalHistoryDto> getAllPatientHistoriesByPatientId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        Patient patient = patientRepository.findByUser_id(user.getId())
                .orElseThrow(() -> new RuntimeException("Patient not found for user id: "+
                        user.getId()));
        return medicalHistoryRepository.findByPatient_PatientId(patient.getPatientId()).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<MedicalHistoryDto> getAllPatientHistoriesByDoctorId(Long userId){
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found:"
         + userId));

        Doctor doctor = doctorRepository.findByUser_id(user.getId()).orElseThrow(() -> new RuntimeException("Doctor Not found"+user.getId()));

        return medicalHistoryRepository.findByDoctor_DoctorId(doctor.getDoctorId()).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // In MedicalHistoryService.java

    public MedicalHistoryDto mapToDto(MedicalHistory medicalHistory) {
        return MedicalHistoryDto.builder()
                .id(medicalHistory.getMedicalHistoryId())
                .bloodType(medicalHistory.getBloodType())
                .height(medicalHistory.getHeight())
                .created(LocalDateTime.now())
                .weight(medicalHistory.getWeight())
                .allergies(parseAllergies(medicalHistory.getAllergies()))
                .chronicConditions(parseConditions(medicalHistory.getChronicConditions()))
                .currentMedications(parseMedications(medicalHistory.getCurrentMedications()))
                .previousSurgeries(parseSurgeries(medicalHistory.getPreviousSurgeries()))
                .familyMedicalHistory(medicalHistory.getFamilyMedicalHistory())
                .smokingStatus(medicalHistory.getSmokingStatus())
                .alcoholConsumption(medicalHistory.getAlcoholConsumption())
                .exerciseFrequency(medicalHistory.getExerciseFrequency())
                .dietaryRestrictions(medicalHistory.getDietaryRestrictions())
                .vaccinationStatus(medicalHistory.getVaccinationStatus())
                .patient(medicalHistory.getPatient() != null ? PatientSummaryDto.fromEntity(medicalHistory.getPatient()) : null)
                .doctor(medicalHistory.getDoctor() != null ? DoctorSummaryDto.fromEntity(medicalHistory.getDoctor()) : null)
                .build();
    }

    public MedicalHistory mapToEntity(MedicalHistoryDto dto) {
        return MedicalHistory.builder()
                .medicalHistoryId(dto.getId())
                .bloodType(dto.getBloodType())
                .createdAt(dto.getCreated())
                .height(dto.getHeight())
                .weight(dto.getWeight())
                .allergies(serializeAllergies(dto.getAllergies()))
                .chronicConditions(serializeConditions(dto.getChronicConditions()))
                .currentMedications(serializeMedications(dto.getCurrentMedications()))
                .previousSurgeries(serializeSurgeries(dto.getPreviousSurgeries()))
                .familyMedicalHistory(dto.getFamilyMedicalHistory())
                .smokingStatus(dto.getSmokingStatus())
                .alcoholConsumption(dto.getAlcoholConsumption())
                .exerciseFrequency(dto.getExerciseFrequency())
                .dietaryRestrictions(dto.getDietaryRestrictions())
                .vaccinationStatus(dto.getVaccinationStatus())
                .build();
    }

    private List<AllergyDto> parseAllergies(String allergiesString) {
        if (allergiesString == null || allergiesString.trim().isEmpty()) {
            return new ArrayList<>();
        }
        List<AllergyDto> allergies = new ArrayList<>();
        String[] allergyArray = allergiesString.split(",");
        for (String allergyStr : allergyArray) {
            String[] parts = allergyStr.split(":", 2);
            AllergyDto allergy = new AllergyDto();
            allergy.setName(parts.length > 0 ? parts[0].trim() : "");
            allergy.setSeverity(parts.length > 1 ? parts[1].trim() : "");
            allergies.add(allergy);
        }
        return allergies;
    }

    private List<ConditionDto> parseConditions(String conditionsString) {
        if (conditionsString == null || conditionsString.trim().isEmpty()) {
            return new ArrayList<>();
        }
        List<ConditionDto> conditions = new ArrayList<>();
        String[] conditionArray = conditionsString.split(",");
        for (String conditionStr : conditionArray) {
            String[] parts = conditionStr.split(":", 2);
            ConditionDto condition = new ConditionDto();
            condition.setName(parts.length > 0 ? parts[0].trim() : "");
            condition.setDescription(parts.length > 1 ? parts[1].trim() : "");
            conditions.add(condition);
        }
        return conditions;
    }

    private List<SurgeryDto> parseSurgeries(String surgeriesString) {
        if (surgeriesString == null || surgeriesString.trim().isEmpty()) {
            return new ArrayList<>();
        }
        List<SurgeryDto> surgeries = new ArrayList<>();
        String[] surgeryArray = surgeriesString.split(",");
        for (String surgeryStr : surgeryArray) {
            String[] parts = surgeryStr.split("\\|", -1); // -1 to keep empty strings
            SurgeryDto surgery = new SurgeryDto();
            surgery.setName(parts.length > 0 ? parts[0].trim() : "");
            surgery.setNotes(parts.length > 1 ? parts[1].trim() : "");
            if (parts.length > 2 && !parts[2].trim().isEmpty()) {
                try {
                    surgery.setStartDate(LocalDate.parse(parts[2].trim()));
                } catch (Exception e) {
                    System.err.println("Failed to parse surgery start date: " + parts[2]);
                }
            }
            if (surgery.getName() != null && !surgery.getName().isEmpty()) {
                surgeries.add(surgery);
            }
        }
        return surgeries;
    }

    private List<MedicationDto> parseMedications(String medicationsString) {
        if (medicationsString == null || medicationsString.trim().isEmpty()) {
            return new ArrayList<>();
        }
        List<MedicationDto> medications = new ArrayList<>();
        String[] medicationArray = medicationsString.split(",");
        for (String medicationStr : medicationArray) {
            String[] parts = medicationStr.split(":", 2);
            MedicationDto medication = new MedicationDto();
            medication.setName(parts.length > 0 ? parts[0].trim() : "");
            medication.setDescription(parts.length > 1 ? parts[1].trim() : "");
            medications.add(medication);
        }
        return medications;
    }

    public String serializeSurgeries(List<SurgeryDto> surgeries) {
        if (surgeries == null || surgeries.isEmpty()) return "";
        return surgeries.stream()
                .map(s -> String.join("|",
                        s.getName() != null ? s.getName() : "",
                        s.getNotes() != null ? s.getNotes() : "",
                        s.getStartDate() != null ? s.getStartDate().toString() : ""))
                .collect(Collectors.joining(","));
    }

    public String serializeAllergies(List<AllergyDto> allergies) {
        if (allergies == null || allergies.isEmpty()) return "";
        return allergies.stream()
                .map(a -> String.join(":",
                        a.getName() != null ? a.getName() : "",
                        a.getSeverity() != null ? a.getSeverity() : ""))
                .collect(Collectors.joining(","));
    }

    public String serializeMedications(List<MedicationDto> medications) {
        if (medications == null || medications.isEmpty()) return "";
        return medications.stream()
                .map(m -> String.join(":",
                        m.getName() != null ? m.getName() : "",
                        m.getDescription() != null ? m.getDescription() : ""))
                .collect(Collectors.joining(","));
    }

    public String serializeConditions(List<ConditionDto> conditions) {
        if (conditions == null || conditions.isEmpty()) return "";
        return conditions.stream()
                .map(c -> String.join(":",
                        c.getName() != null ? c.getName() : "",
                        c.getDescription() != null ? c.getDescription() : ""))
                .collect(Collectors.joining(","));
    }
}
