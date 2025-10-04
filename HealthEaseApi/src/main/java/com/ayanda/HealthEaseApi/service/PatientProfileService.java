package com.ayanda.HealthEaseApi.service;


import com.ayanda.HealthEaseApi.dto.dtoObjects.*;
import com.ayanda.HealthEaseApi.dto.dtoObjects.doctorDtos.DoctorProfileResponseDto;
import com.ayanda.HealthEaseApi.dto.dtoObjects.doctorDtos.helper.exceptions.ResourceNotFoundException;
import com.ayanda.HealthEaseApi.entities.*;
import com.ayanda.HealthEaseApi.repos.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PatientProfileService {
    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private EmergencyContactsRepository emergencyContactRepository;

    @Autowired
    private InsuranceRepository insuranceRepository;

    @Autowired
    private ObjectMapper objectMapper;

    public PatientProfileResponseDto getPatientProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Patient patient = patientRepository.findByUser_id(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient", "userId", userId));

        return mapPatientToResponseDto(patient, user);
    }

    public PatientProfileResponseDto updateCompleteProfile(Long userId,
                                                           CompleteProfileUpdateDto profileData, MultipartFile profilePicture) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Patient patient = patientRepository.findByUser_id(userId)
                .orElse(new Patient());

        if (patient.getPatientId() == null) {
            patient.setUser(user);
            patient.setCreatedAt(LocalDateTime.now());
        }

        // Update personal information
        updatePersonalInfoFields(patient, user, profileData.getPersonalInfo());

        // Update medical information
        updateMedicalInfoFields(patient, profileData.getMedicalHistory());

        // Update emergency contacts
        updateEmergencyContactsFields(patient, profileData.getEmergencyContacts());

        // Update insurance information
        updateInsuranceInfoFields(patient, profileData.getInsurance());

        // Update preferences
        updatePreferencesFields(patient, profileData.getPreferences());

        patient.setUpdatedAt(LocalDateTime.now());
        Patient savedPatient = patientRepository.save(patient);

        userRepository.profileCompleteToTrue(user.getId());

        return mapPatientToResponseDto(savedPatient, user);
    }

    public List<PatientProfileResponseDto> getPatientByNameAndSurname(String name, String surname){
        List<Patient> patients = patientRepository.findByFirstNameAndLastName(name, surname);
        return patients.stream()
                .map(patient -> mapPatientToResponseDto(patient, patient.getUser()))
                .collect(Collectors.toList());
    }

    public PatientProfileResponseDto getPatientByIdNumber(String idNumber){
        Patient patient = patientRepository.findByIdNumber(idNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Patient", "idNumber", 1L));
        return mapPatientToResponseDto(patient, patient.getUser());
    }

    public PatientProfileResponseDto updatePersonalInfo(Long userId, PersonalInfoUpdateDto personalInfo) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Patient patient = patientRepository.findByUser_id(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient", "userId", userId));

        updatePersonalInfoFields(patient, user, personalInfo);
        patient.setUpdatedAt(LocalDateTime.now());

        Patient savedPatient = patientRepository.save(patient);
        return mapPatientToResponseDto(savedPatient, user);
    }

    public PatientProfileResponseDto updateMedicalInfo(Long userId, MedicalInfoUpdateDto medicalInfo) {
        Patient patient = patientRepository.findByUser_id(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient", "userId", userId));

        updateMedicalInfoFields(patient, medicalInfo);
        patient.setUpdatedAt(LocalDateTime.now());

        Patient savedPatient = patientRepository.save(patient);
        User user = savedPatient.getUser();

        return mapPatientToResponseDto(savedPatient, user);
    }

    public PatientProfileResponseDto updateEmergencyContacts(Long userId,
                                                             EmergencyContactsUpdateDto emergencyContacts) {
        Patient patient = patientRepository.findByUser_id(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient", "userId", userId));

        updateEmergencyContactsFields(patient, emergencyContacts.getEmergencyContacts());
        patient.setUpdatedAt(LocalDateTime.now());

        Patient savedPatient = patientRepository.save(patient);
        User user = savedPatient.getUser();

        return mapPatientToResponseDto(savedPatient, user);
    }

    public PatientProfileResponseDto updateInsuranceInfo(Long userId, InsuranceInfoUpdateDto insuranceInfo) {
        Patient patient = patientRepository.findByUser_id(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient", "userId", userId));

        updateInsuranceInfoFields(patient, insuranceInfo);
        patient.setUpdatedAt(LocalDateTime.now());

        Patient savedPatient = patientRepository.save(patient);
        User user = savedPatient.getUser();

        return mapPatientToResponseDto(savedPatient, user);
    }

    public ProfileCompletionDto getProfileCompletion(Long userId) {
        Patient patient = patientRepository.findByUser_id(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient", "userId", userId));

        return calculateProfileCompletion(patient);
    }


    public IdValidationResponseDto validateSouthAfricanId(String idNumber) {
        IdValidationResponseDto response = new IdValidationResponseDto();

        if (IdNumberValidator.IvalidateId(idNumber)) {
            response.setValid(true);
            response.setMessage("Valid South African ID number");
            response.setExtractedInfo(IdNumberValidator.extractInfoFromId(idNumber));
        } else {
            response.setValid(false);
            response.setMessage("Invalid South African ID number");
        }

        return response;
    }

    public CompleteProfileUpdateDto parseProfileData(String profileDataJson) {
        try {
            return objectMapper.readValue(profileDataJson, CompleteProfileUpdateDto.class);
        } catch (Exception e) {
            throw new RuntimeException("Invalid profile data format: " + e.getMessage());
        }
    }

    public List<DoctorProfileResponseDto> getAllDoctorsLinkedToPatient(Long userId){
        User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("Failed","User not found",userId));

        Patient patient = patientRepository.findByUser_id(user.getId()).orElseThrow(()-> new ResourceNotFoundException("Failed","Patient not found",user.getId()));

        List<Doctor> doctors = patient.getDoctors();

        return doctors.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    // Private helper methods
    private void updatePersonalInfoFields(Patient patient, User user, PersonalInfoUpdateDto personalInfo) {
        patient.setFirstName(personalInfo.getFirstName());
        patient.setLastName(personalInfo.getLastName());
        patient.setDateOfBirth(personalInfo.getDateOfBirth());
        patient.setGender(personalInfo.getGender());
        patient.setPhoneNumber(personalInfo.getPhoneNumber());
        patient.setAlternatePhoneNumber(personalInfo.getAlternatePhoneNumber());
        patient.setIdNumber(personalInfo.getIdNumber());
        patient.setOccupation(personalInfo.getOccupation());
        patient.setEmployer(personalInfo.getEmployer());
        patient.setMaritalStatus(personalInfo.getMaritalStatus());
        patient.setPreferredLanguage(personalInfo.getPreferredLanguage());

        // Update address
        if (personalInfo.getAddress() != null) {
            Address address = patient.getAddress();
            if (address == null) {
                address = new Address();
                patient.setAddress(address);
            }

            address.setStreet(personalInfo.getAddress().getStreet());
            address.setSuburb(personalInfo.getAddress().getSuburb());
            address.setCity(personalInfo.getAddress().getCity());
            address.setProvince(personalInfo.getAddress().getProvince());
            address.setPostalCode(personalInfo.getAddress().getPostalCode());
            address.setCountry(personalInfo.getAddress().getCountry());
        }

        // Update user email if provided
        if (personalInfo.getEmail() != null) {
            user.setEmail(personalInfo.getEmail());
            userRepository.save(user);
        }
    }

    private void updateMedicalInfoFields(Patient patient, MedicalInfoUpdateDto medicalInfo) {
        if (medicalInfo == null) return;

        MedicalHistory medicalHistory = patient.getMedicalHistory();
        if (medicalHistory == null) {
            medicalHistory = new MedicalHistory();
            medicalHistory.setPatient(patient);
            patient.setMedicalHistory(medicalHistory);
        }

        medicalHistory.setBloodType(medicalInfo.getBloodType());
        medicalHistory.setHeight(medicalInfo.getHeight());
        medicalHistory.setWeight(medicalInfo.getWeight());
        medicalHistory.setFamilyMedicalHistory(medicalInfo.getFamilyMedicalHistory());
        medicalHistory.setSmokingStatus(medicalInfo.getSmokingStatus());
        medicalHistory.setAlcoholConsumption(medicalInfo.getAlcoholConsumption());
        medicalHistory.setExerciseFrequency(medicalInfo.getExerciseFrequency());
        medicalHistory.setDietaryRestrictions(medicalInfo.getDietaryRestrictions());

        // Handle allergies
        if (medicalInfo.getAllergies() != null) {
            medicalHistory.setAllergies(String.join(",",
                    medicalInfo.getAllergies().stream()
                            .map(allergy -> allergy.getName() + ":" + allergy.getSeverity())
                            .collect(Collectors.toList())
            ));
        }

        // Handle chronic conditions
        if (medicalInfo.getChronicConditions() != null) {
            medicalHistory.setChronicConditions(String.join(",",
                    medicalInfo.getChronicConditions().stream()
                            .map(condition -> condition.getName() + ":" + condition.getDescription())
                            .collect(Collectors.toList())
            ));
        }

        // Handle current medications
        if (medicalInfo.getCurrentMedications() != null) {
            medicalHistory.setCurrentMedications(String.join(",",
                    medicalInfo.getCurrentMedications().stream()
                            .map(med -> med.getName() + ":" + med.getDescription())
                            .collect(Collectors.toList())
            ));
        }

        // Handle previous surgeries
        if (medicalInfo.getPreviousSurgeries() != null) {
            medicalHistory.setPreviousSurgeries(String.join(",",
                    medicalInfo.getPreviousSurgeries().stream()
                            .map(surgery -> surgery.getName()+"|"+surgery.getNotes()+"|"+
                                    (surgery.getStartDate()!=null ? surgery.getStartDate().toString() : ""))
                            .collect(Collectors.toList())
            ));
        }

        // Handle vaccination status
        if (medicalInfo.getVaccinationStatus() != null) {
            VaccinationStatusUpdateDto vaccinations = medicalInfo.getVaccinationStatus();
            List<String> vaccinationList = new ArrayList<>();

            if (Boolean.TRUE.equals(vaccinations.getCovid19())) vaccinationList.add("COVID-19");
            if (Boolean.TRUE.equals(vaccinations.getFlu())) vaccinationList.add("Flu");
            if (Boolean.TRUE.equals(vaccinations.getHepatitisBs())) vaccinationList.add("Hepatitis B");
            if (Boolean.TRUE.equals(vaccinations.getTetanus())) vaccinationList.add("Tetanus");
            if (vaccinations.getOther() != null && !vaccinations.getOther().trim().isEmpty()) {
                vaccinationList.add(vaccinations.getOther());
            }

            medicalHistory.setVaccinationStatus(String.join(",", vaccinationList));
        }

        medicalHistory.setUpdatedAt(LocalDateTime.now());
    }

    private void updateEmergencyContactsFields(Patient patient, List<EmergencyContactUpdateDto> emergencyContactsDto) {
        if (emergencyContactsDto == null || emergencyContactsDto.isEmpty()) return;

        // Delete existing emergency contacts
        if (patient.getEmergencyContacts() != null) {
            emergencyContactRepository.deleteAll(patient.getEmergencyContacts());
        }

        // Create new emergency contacts
        List<EmergencyContact> emergencyContacts = new ArrayList<>();
        for (EmergencyContactUpdateDto contactDto : emergencyContactsDto) {
            EmergencyContact contact = new EmergencyContact();
            contact.setPatient(patient);
            contact.setName(contactDto.getName());
            contact.setRelationship(contactDto.getRelationship());
            contact.setPhoneNumber(contactDto.getPhoneNumber());
            contact.setAlternatePhoneNumber(contactDto.getAlternatePhoneNumber());
            contact.setEmail(contactDto.getEmail());
            contact.setAddress(contactDto.getAddress());
            contact.setIsPrimary(Boolean.TRUE.equals(contactDto.getIsPrimary()));
            contact.setCanMakeDecisions(Boolean.TRUE.equals(contactDto.getCanMakeDecisions()));
            contact.setCreatedAt(LocalDateTime.now());

            emergencyContacts.add(contact);
        }

        patient.setEmergencyContacts(emergencyContacts);
    }

    private void updateInsuranceInfoFields(Patient patient, InsuranceInfoUpdateDto insuranceInfo) {
        if (insuranceInfo == null) return;

        if (Boolean.TRUE.equals(insuranceInfo.getHasInsurance()) &&
                insuranceInfo.getPrimaryInsurance() != null) {

            Insurance insurance = patient.getInsurance();
            if (insurance == null) {
                insurance = new Insurance();
                insurance.setPatient(patient);
                patient.setInsurance(insurance);
            }

            PrimaryInsuranceUpdateDto primaryInsurance = insuranceInfo.getPrimaryInsurance();
            insurance.setProvider(primaryInsurance.getProvider());
            insurance.setPolicyNumber(primaryInsurance.getPolicyNumber());
            insurance.setGroupNumber(primaryInsurance.getGroupNumber());
            insurance.setPlanName(primaryInsurance.getPlanName());
            insurance.setEffectiveDate(primaryInsurance.getEffectiveDate());
            insurance.setExpirationDate(primaryInsurance.getExpirationDate());
            insurance.setCopayAmount(primaryInsurance.getCopayAmount());
            insurance.setDeductibleAmount(primaryInsurance.getDeductibleAmount());
            insurance.setUpdatedAt(LocalDateTime.now());

            // Handle secondary insurance
            if (insuranceInfo.getSecondaryInsurance() != null &&
                    Boolean.TRUE.equals(insuranceInfo.getSecondaryInsurance().getHasSecondary())) {
                SecondaryInsuranceUpdateDto secondary = insuranceInfo.getSecondaryInsurance();
                insurance.setSecondaryPlanName(secondary.getPlanName());
                insurance.setSecondaryProvider(secondary.getProvider());
                insurance.setSecondaryPolicyNumber(secondary.getPolicyNumber());
                insurance.setSecondaryGroupNumber(secondary.getGroupNumber());
            }

            // Medical aid specific fields
            patient.setMedicalAidNumber(insuranceInfo.getMedicalAidNumber());
            insurance.setDependentCode(insuranceInfo.getDependentCode());
            insurance.setAuthorizationRequired(Boolean.TRUE.equals(insuranceInfo.getAuthorizationRequired()));

        } else {
            // Remove insurance if user indicates they don't have any
            if (patient.getInsurance() != null) {
                insuranceRepository.delete(patient.getInsurance());
                patient.setInsurance(null);
            }
        }
    }

    private void updatePreferencesFields(Patient patient, PreferencesUpdateDto preferences) {
        if (preferences == null) return;

        patient.setPreferredDoctorGender(preferences.getPreferredDoctorGender());

        // Communication preferences
        if (preferences.getCommunicationPreferences() != null) {
            CommunicationPreferencesUpdateDto comm = preferences.getCommunicationPreferences();
            patient.setEmailReminders(Boolean.TRUE.equals(comm.getEmailReminders()));
            patient.setSmsReminders(Boolean.TRUE.equals(comm.getSmsReminders()));
            patient.setAppointmentConfirmations(Boolean.TRUE.equals(comm.getAppointmentConfirmations()));
            patient.setTestResults(Boolean.TRUE.equals(comm.getTestResults()));
            patient.setPromotionalEmails(Boolean.TRUE.equals(comm.getPromotionalEmails()));
        }

        // Privacy settings
        if (preferences.getPrivacySettings() != null) {
            PrivacySettingsUpdateDto privacy = preferences.getPrivacySettings();
            patient.setShareDataWithResearch(Boolean.TRUE.equals(privacy.getShareDataWithResearch()));
            patient.setAllowMarketingCommunication(Boolean.TRUE.equals(privacy.getAllowMarketingCommunication()));
            patient.setProfileVisibility(privacy.getProfileVisibility());
        }
    }

    private PatientProfileResponseDto mapPatientToResponseDto(Patient patient, User user) {
        PatientProfileResponseDto dto = new PatientProfileResponseDto();

        // Basic info
        dto.setId(patient.getPatientId());
        dto.setUserId(user.getId());
        dto.setFirstName(patient.getFirstName());
        dto.setLastName(patient.getLastName());
        dto.setEmail(user.getEmail());
        dto.setDateOfBirth(patient.getDateOfBirth());
        dto.setGender(patient.getGender());
        dto.setPhoneNumber(patient.getPhoneNumber());
        dto.setAlternatePhoneNumber(patient.getAlternatePhoneNumber());
        dto.setIdNumber(patient.getIdNumber());
        dto.setOccupation(patient.getOccupation());
        dto.setEmployer(patient.getEmployer());
        dto.setMaritalStatus(patient.getMaritalStatus());
        dto.setPreferredLanguage(patient.getPreferredLanguage());
        dto.setProfilePicture(patient.getProfilePicture());

        // Address
        if (patient.getAddress() != null) {
            AddressDto addressDto = new AddressDto();
            Address address = patient.getAddress();
            addressDto.setStreet(address.getStreet());
            addressDto.setSuburb(address.getSuburb());
            addressDto.setCity(address.getCity());
            addressDto.setProvince(address.getProvince());
            addressDto.setPostalCode(address.getPostalCode());
            addressDto.setCountry(address.getCountry());
            dto.setAddress(addressDto);
        }

        // Medical history
        if (patient.getMedicalHistory() != null) {
            dto.setMedicalHistory(mapMedicalHistoryToDto(patient.getMedicalHistory()));
        }

        // Emergency contacts
        if (patient.getEmergencyContacts() != null && !patient.getEmergencyContacts().isEmpty()) {
            dto.setEmergencyContacts(patient.getEmergencyContacts().stream()
                    .map(this::mapEmergencyContactToDto)
                    .collect(Collectors.toList()));
        }

        // Insurance
        if (patient.getInsurance() != null) {
            dto.setInsurance(mapInsuranceToDto(patient.getInsurance()));
        }

        // Medical aid number
        dto.setMedicalAidNumber(patient.getMedicalAidNumber());

        // Preferences
        dto.setPreferences(mapPreferencesToDto(patient));

        // Timestamps
        dto.setCreatedAt(patient.getCreatedAt());
        dto.setUpdatedAt(patient.getUpdatedAt());

        return dto;
    }


    private MedicalHistoryDto mapMedicalHistoryToDto(MedicalHistory medicalHistory) {
        MedicalHistoryDto dto = new MedicalHistoryDto();
        dto.setBloodType(medicalHistory.getBloodType());
        dto.setHeight(medicalHistory.getHeight());
        dto.setWeight(medicalHistory.getWeight());
        dto.setFamilyMedicalHistory(medicalHistory.getFamilyMedicalHistory());
        dto.setSmokingStatus(medicalHistory.getSmokingStatus());
        dto.setAlcoholConsumption(medicalHistory.getAlcoholConsumption());
        dto.setExerciseFrequency(medicalHistory.getExerciseFrequency());
        dto.setDietaryRestrictions(medicalHistory.getDietaryRestrictions());
        dto.setVaccinationStatus(medicalHistory.getVaccinationStatus());

        // Parse comma-separated strings back to lists
        if (medicalHistory.getAllergies() != null && !medicalHistory.getAllergies().isEmpty()) {
            dto.setAllergies(parseAllergies(medicalHistory.getAllergies()));
        }

        if (medicalHistory.getChronicConditions() != null && !medicalHistory.getChronicConditions().isEmpty()) {
            dto.setChronicConditions(parseConditions(medicalHistory.getChronicConditions()));
        }

        if (medicalHistory.getCurrentMedications() != null && !medicalHistory.getCurrentMedications().isEmpty()) {
            dto.setCurrentMedications(parseMedications(medicalHistory.getCurrentMedications()));
        }

        if (medicalHistory.getPreviousSurgeries() != null && !medicalHistory.getPreviousSurgeries().isEmpty()) {
            dto.setPreviousSurgeries(parseSurgeries(medicalHistory.getPreviousSurgeries()));
        }

        return dto;
    }

    private List<SurgeryDto> parseSurgeries(String surgeriesString) {
        if (surgeriesString == null || surgeriesString.trim().isEmpty()) {
            return new ArrayList<>();
        }

        List<SurgeryDto> surgeries = new ArrayList<>();
        String[] surgeryArray = surgeriesString.split(",");

        for (String surgeryStr : surgeryArray) {
            surgeryStr = surgeryStr.trim();
            if (!surgeryStr.isEmpty()) {
                SurgeryDto surgery = new SurgeryDto();

                if (surgeryStr.contains("|")) {
                    String[] parts = surgeryStr.split("\\|", -1); // -1 to keep empty strings

                    // Set name (required)
                    surgery.setName(parts.length > 0 ? parts[0].trim() : "");

                    // Set description (optional)
                    if (parts.length > 1 && !parts[1].trim().isEmpty()) {
                        surgery.setDescription(parts[1].trim());
                    }

                    // Set notes (optional)
                    if (parts.length > 2 && !parts[2].trim().isEmpty()) {
                        surgery.setNotes(parts[2].trim());
                    }

                    // Set surgery date (optional)
                    if (parts.length > 3 && !parts[3].trim().isEmpty()) {
                        try {
                            surgery.setStartDate(LocalDate.parse(parts[3].trim()));
                        } catch (Exception e) {
                            System.err.println("Failed to parse surgery date: " + parts[3]);
                        }
                    }
                } else {
                    // Simple format - just the name
                    surgery.setName(surgeryStr);
                }

                // Only add if name is not empty
                if (surgery.getName() != null && !surgery.getName().trim().isEmpty()) {
                    surgeries.add(surgery);
                }
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
            medicationStr = medicationStr.trim();
            if (!medicationStr.isEmpty()) {
                MedicationDto medication = new MedicationDto();

                if (medicationStr.contains(":")) {
                    String[] parts = medicationStr.split(":", -1); // -1 to keep empty strings

                    // Set name (required)
                    medication.setName(parts.length > 0 ? parts[0].trim() : "");

                    // Set description/dosage (optional)
                    if (parts.length > 1 && !parts[1].trim().isEmpty()) {
                        medication.setDescription(parts[1].trim());
                    }

                    // Set notes (optional)
                    if (parts.length > 2 && !parts[2].trim().isEmpty()) {
                        medication.setNotes(parts[2].trim());
                    }

                    // Set start date (optional)
                    if (parts.length > 3 && !parts[3].trim().isEmpty()) {
                        try {
                            medication.setStartDate(LocalDate.parse(parts[3].trim()));
                        } catch (Exception e) {
                            System.err.println("Failed to parse start date for medication: " + parts[3]);
                        }
                    }

                    // Set end date (optional)
                    if (parts.length > 4 && !parts[4].trim().isEmpty()) {
                        try {
                            medication.setEndDate(LocalDate.parse(parts[4].trim()));
                        } catch (Exception e) {
                            System.err.println("Failed to parse end date for medication: " + parts[4]);
                        }
                    }
                } else {
                    // Simple format - just the name
                    medication.setName(medicationStr);
                }

                // Only add if name is not empty
                if (medication.getName() != null && !medication.getName().trim().isEmpty()) {
                    medications.add(medication);
                }
            }
        }

        return medications;
    }

    private List<ConditionDto> parseConditions(String conditionsString) {
        if (conditionsString == null || conditionsString.trim().isEmpty()) {
            return new ArrayList<>();
        }

        List<ConditionDto> conditions = new ArrayList<>();
        String[] conditionArray = conditionsString.split(",");

        for (String conditionStr : conditionArray) {
            conditionStr = conditionStr.trim();
            if (!conditionStr.isEmpty()) {
                ConditionDto condition = new ConditionDto();

                if (conditionStr.contains(":")) {
                    String[] parts = conditionStr.split(":", -1); // -1 to keep empty strings

                    // Set name (required)
                    condition.setName(parts.length > 0 ? parts[0].trim() : "");

                    // Set description (optional)
                    if (parts.length > 1 && !parts[1].trim().isEmpty()) {
                        condition.setDescription(parts[1].trim());
                    }

                    // Set notes (optional)
                    if (parts.length > 2 && !parts[2].trim().isEmpty()) {
                        condition.setNotes(parts[2].trim());
                    }

                    // Set start date (optional)
                    if (parts.length > 3 && !parts[3].trim().isEmpty()) {
                        try {
                            condition.setStartDate(LocalDate.parse(parts[3].trim()));
                        } catch (Exception e) {
                            System.err.println("Failed to parse start date for condition: " + parts[3]);
                        }
                    }

                    // Set end date (optional)
                    if (parts.length > 4 && !parts[4].trim().isEmpty()) {
                        try {
                            condition.setEndDate(LocalDate.parse(parts[4].trim()));
                        } catch (Exception e) {
                            System.err.println("Failed to parse end date for condition: " + parts[4]);
                        }
                    }
                } else {
                    // Simple format - just the name
                    condition.setName(conditionStr);
                }

                // Only add if name is not empty
                if (condition.getName() != null && !condition.getName().trim().isEmpty()) {
                    conditions.add(condition);
                }
            }
        }

        return conditions;
    }

    private List<AllergyDto> parseAllergies(String allergiesString) {
        if (allergiesString == null || allergiesString.trim().isEmpty()) {
            return new ArrayList<>();
        }

        List<AllergyDto> allergies = new ArrayList<>();
        String[] allergyArray = allergiesString.split(",");

        for (String allergyStr : allergyArray) {
            allergyStr = allergyStr.trim();
            if (!allergyStr.isEmpty()) {
                AllergyDto allergy = new AllergyDto();

                if (allergyStr.contains(":")) {
                    String[] parts = allergyStr.split(":", -1); // -1 to keep empty strings

                    // Set name (required)
                    allergy.setName(parts.length > 0 ? parts[0].trim() : "");

                    // Set severity (optional)
                    if (parts.length > 1 && !parts[1].trim().isEmpty()) {
                        allergy.setSeverity(parts[1].trim());
                    }

                    // Set description (optional)
                    if (parts.length > 2 && !parts[2].trim().isEmpty()) {
                        allergy.setDescription(parts[2].trim());
                    }

                    // Set notes (optional)
                    if (parts.length > 3 && !parts[3].trim().isEmpty()) {
                        allergy.setNotes(parts[3].trim());
                    }
                } else {
                    // Simple format - just the name
                    allergy.setName(allergyStr);
                }

                // Only add if name is not empty
                if (allergy.getName() != null && !allergy.getName().trim().isEmpty()) {
                    allergies.add(allergy);
                }
            }
        }

        return allergies;
    }

    private PreferencesDto mapPreferencesToDto(Patient patient) {
        if (patient == null) {
            return new PreferencesDto();
        }

        PreferencesDto preferencesDto = new PreferencesDto();

        // Basic preferences
        preferencesDto.setPreferredDoctorGender(patient.getPreferredDoctorGender());
        preferencesDto.setPreferredLanguage(patient.getPreferredLanguage() != null ?
                patient.getPreferredLanguage() : "English");

        // Communication preferences
        CommunicationPreferencesDto communicationPreferences = new CommunicationPreferencesDto();
        communicationPreferences.setEmailReminders(patient.getEmailReminders() != null ?
                patient.getEmailReminders() : true);
        communicationPreferences.setSmsReminders(patient.getSmsReminders() != null ?
                patient.getSmsReminders() : true);
        communicationPreferences.setAppointmentConfirmations(patient.getAppointmentConfirmations() != null ?
                patient.getAppointmentConfirmations() : true);
        communicationPreferences.setTestResults(patient.getTestResults() != null ?
                patient.getTestResults() : true);
        communicationPreferences.setPromotionalEmails(patient.getPromotionalEmails() != null ?
                patient.getPromotionalEmails() : false);

        preferencesDto.setCommunicationPreferences(communicationPreferences);

        // Privacy settings
        PrivacySettingsDto privacySettings = new PrivacySettingsDto();
        privacySettings.setShareDataWithResearch(patient.getShareDataWithResearch() != null ?
                patient.getShareDataWithResearch() : false);
        privacySettings.setAllowMarketingCommunication(patient.getAllowMarketingCommunication() != null ?
                patient.getAllowMarketingCommunication() : false);
        privacySettings.setProfileVisibility(patient.getProfileVisibility() != null ?
                patient.getProfileVisibility() : "private");

        preferencesDto.setPrivacySettings(privacySettings);

        return preferencesDto;
    }

    private ProfileCompletionDto calculateProfileCompletion(Patient patient) {
        if (patient == null) {
            return new ProfileCompletionDto(0, 0, 0, Arrays.asList("Patient profile not found"),
                    false, false, false, false, false);
        }

        ProfileCompletionDto completion = new ProfileCompletionDto();
        List<String> missingFields = new ArrayList<>();
        int completedFields = 0;
        int totalRequiredFields = 15; // Core required fields
        int totalOptionalFields = 10; // Optional fields that contribute to completion

        // =============================
        // REQUIRED FIELDS (Core Profile)
        // =============================

        // Basic personal information (7 required fields)
        if (isFieldComplete(patient.getFirstName())) {
            completedFields++;
        } else {
            missingFields.add("First Name");
        }

        if (isFieldComplete(patient.getLastName())) {
            completedFields++;
        } else {
            missingFields.add("Last Name");
        }

        if (patient.getDateOfBirth() != null) {
            completedFields++;
        } else {
            missingFields.add("Date of Birth");
        }

        if (isFieldComplete(patient.getGender())) {
            completedFields++;
        } else {
            missingFields.add("Gender");
        }

        if (isFieldComplete(patient.getPhoneNumber())) {
            completedFields++;
        } else {
            missingFields.add("Phone Number");
        }

        if (isFieldComplete(patient.getIdNumber())) {
            completedFields++;
        } else {
            missingFields.add("ID Number");
        }

        // Address validation (1 required field, but comprehensive)
        if (isAddressComplete(patient.getAddress())) {
            completedFields++;
        } else {
            missingFields.add("Complete Address");
        }

        // Emergency contacts (1 required field)
        if (areEmergencyContactsComplete(patient.getEmergencyContacts())) {
            completedFields++;
        } else {
            missingFields.add("Emergency Contacts");
        }

        // =============================
        // MEDICAL INFORMATION (6 fields)
        // =============================

        int medicalFieldsCompleted = 0;
        int totalMedicalFields = 6;

        if (patient.getMedicalHistory() != null) {
            MedicalHistory medicalHistory = patient.getMedicalHistory();

            // Blood type
            if (isFieldComplete(medicalHistory.getBloodType())) {
                medicalFieldsCompleted++;
            } else {
                missingFields.add("Blood Type");
            }

            // Height and weight (counted as one field)
            if (medicalHistory.getHeight() != null && medicalHistory.getWeight() != null &&
                    medicalHistory.getHeight() > 0 && medicalHistory.getWeight() > 0) {
                medicalFieldsCompleted++;
            } else {
                missingFields.add("Height and Weight");
            }

            // Medical conditions/history (any one counts)
            if (hasAnyMedicalHistory(medicalHistory)) {
                medicalFieldsCompleted++;
            } else {
                missingFields.add("Medical History (allergies, conditions, medications, or family history)");
            }

            // Lifestyle information
            if (isFieldComplete(medicalHistory.getSmokingStatus()) ||
                    isFieldComplete(medicalHistory.getAlcoholConsumption()) ||
                    isFieldComplete(medicalHistory.getExerciseFrequency())) {
                medicalFieldsCompleted++;
            } else {
                missingFields.add("Lifestyle Information (smoking, alcohol, exercise)");
            }

            // Vaccination status
            if (isFieldComplete(medicalHistory.getVaccinationStatus())) {
                medicalFieldsCompleted++;
            } else {
                missingFields.add("Vaccination Status");
            }

            // Family medical history
            if (isFieldComplete(medicalHistory.getFamilyMedicalHistory())) {
                medicalFieldsCompleted++;
            } else {
                missingFields.add("Family Medical History");
            }
        } else {
            missingFields.addAll(Arrays.asList("Blood Type", "Height and Weight",
                    "Medical History", "Lifestyle Information", "Vaccination Status",
                    "Family Medical History"));
        }

        // Add medical fields to completed count
        completedFields += medicalFieldsCompleted;

        // =============================
        // OPTIONAL FIELDS (10 fields that improve completion)
        // =============================

        int optionalFieldsCompleted = 0;

        // Profile picture
        if (isFieldComplete(patient.getProfilePicture())) {
            optionalFieldsCompleted++;
        } else {
            missingFields.add("Profile Picture");
        }

        // Occupation
        if (isFieldComplete(patient.getOccupation())) {
            optionalFieldsCompleted++;
        } else {
            missingFields.add("Occupation");
        }

        // Employer
        if (isFieldComplete(patient.getEmployer())) {
            optionalFieldsCompleted++;
        } else {
            missingFields.add("Employer");
        }

        // Marital status
        if (isFieldComplete(patient.getMaritalStatus())) {
            optionalFieldsCompleted++;
        } else {
            missingFields.add("Marital Status");
        }

        // Alternate phone number
        if (isFieldComplete(patient.getAlternatePhoneNumber())) {
            optionalFieldsCompleted++;
        } else {
            missingFields.add("Alternate Phone Number");
        }

        // Insurance information
        if (patient.getInsurance() != null) {
            optionalFieldsCompleted++;
        } else {
            missingFields.add("Insurance Information");
        }

        // Medical aid number (South Africa specific)
        if (isFieldComplete(patient.getMedicalAidNumber())) {
            optionalFieldsCompleted++;
        } else {
            missingFields.add("Medical Aid Number");
        }

        // Communication preferences set
        if (arePreferencesSet(patient)) {
            optionalFieldsCompleted++;
        } else {
            missingFields.add("Communication Preferences");
        }

        // Preferred language
        if (isFieldComplete(patient.getPreferredLanguage())) {
            optionalFieldsCompleted++;
        } else {
            missingFields.add("Preferred Language");
        }

        // Preferred doctor gender
        if (isFieldComplete(patient.getPreferredDoctorGender())) {
            optionalFieldsCompleted++;
        } else {
            missingFields.add("Preferred Doctor Gender");
        }

        // =============================
        // CALCULATE COMPLETION METRICS
        // =============================

        int totalFields = totalRequiredFields + totalOptionalFields;
        int totalCompleted = completedFields + optionalFieldsCompleted;

        // Calculate percentage (required fields weighted more heavily)
        double requiredWeight = 0.7; // 70% weight for required fields
        double optionalWeight = 0.3; // 30% weight for optional fields

        double requiredPercentage = (double) completedFields / totalRequiredFields;
        double optionalPercentage = (double) optionalFieldsCompleted / totalOptionalFields;

        int weightedPercentage = (int) Math.round(
                (requiredPercentage * requiredWeight + optionalPercentage * optionalWeight) * 100);

        // Simple percentage calculation as backup
        int simplePercentage = (int) Math.round((double) totalCompleted / totalFields * 100);

        // Use the higher percentage but cap at 100%
        int finalPercentage = Math.min(Math.max(weightedPercentage, simplePercentage), 100);

        // =============================
        // SECTION-WISE COMPLETION
        // =============================

        boolean personalInfoComplete = isPersonalInfoComplete(patient);
        boolean medicalInfoComplete = isMedicalInfoComplete(patient);
        boolean emergencyContactsComplete = isEmergencyContactsComplete(patient);
        boolean insuranceInfoComplete = isInsuranceInfoComplete(patient);

        // =============================
        // BUILD COMPLETION DTO
        // =============================

        completion.setCompletionPercentage(finalPercentage);
        completion.setCompletedFields(totalCompleted);
        completion.setTotalFields(totalFields);
        completion.setMissingFields(missingFields);
        completion.setComplete(finalPercentage >= 80 && personalInfoComplete && emergencyContactsComplete);

        // Section completion
        completion.setPersonalInfoComplete(personalInfoComplete);
        completion.setMedicalInfoComplete(medicalInfoComplete);
        completion.setEmergencyContactsComplete(emergencyContactsComplete);
        completion.setInsuranceInfoComplete(insuranceInfoComplete);

        return completion;
    }

    private EmergencyContactDto mapEmergencyContactToDto(EmergencyContact emergencyContact) {
        EmergencyContactDto dto = new EmergencyContactDto();
        dto.setId(emergencyContact.getEmergencyContactId());
        dto.setName(emergencyContact.getName());
        dto.setRelationship(emergencyContact.getRelationship());
        dto.setPhoneNumber(emergencyContact.getPhoneNumber());
        dto.setAlternatePhoneNumber(emergencyContact.getAlternatePhoneNumber());
        dto.setEmail(emergencyContact.getEmail());
        dto.setAddress(emergencyContact.getAddress());
        dto.setIsPrimary(emergencyContact.getIsPrimary());
        dto.setCanMakeDecisions(emergencyContact.getCanMakeDecisions());
        return dto;
    }

    private InsuranceDto mapInsuranceToDto(Insurance insurance) {
        InsuranceDto dto = new InsuranceDto();
        dto.setId(insurance.getInsuranceId());
        dto.setProvider(insurance.getProvider());
        dto.setPolicyNumber(insurance.getPolicyNumber());
        dto.setGroupNumber(insurance.getGroupNumber());
        dto.setPlanName(insurance.getPlanName());
        dto.setEffectiveDate(insurance.getEffectiveDate());
        dto.setExpirationDate(insurance.getExpirationDate());
        dto.setCopayAmount(insurance.getCopayAmount());
        dto.setDeductibleAmount(insurance.getDeductibleAmount());

        return dto;

    }

    private boolean isFieldComplete(String field) {
        return field != null && !field.trim().isEmpty() && !field.trim().equalsIgnoreCase("null");
    }

    private boolean isAddressComplete(Address address) {
        if (address == null) return false;

        return isFieldComplete(address.getStreet()) &&
                isFieldComplete(address.getSuburb()) &&
                isFieldComplete(address.getCity()) &&
                isFieldComplete(address.getProvince()) &&
                isFieldComplete(address.getPostalCode());
    }

    /**
     * Check if emergency contacts are complete
     */
    private boolean areEmergencyContactsComplete(List<EmergencyContact> emergencyContacts) {
        if (emergencyContacts == null || emergencyContacts.isEmpty()) {
            return false;
        }

        // At least one complete emergency contact is required
        return emergencyContacts.stream().anyMatch(contact ->
                isFieldComplete(contact.getName()) &&
                        isFieldComplete(contact.getPhoneNumber()) &&
                        isFieldComplete(contact.getRelationship())
        );
    }

    private boolean hasAnyMedicalHistory(MedicalHistory medicalHistory) {
        if (medicalHistory == null) return false;

        return isFieldComplete(medicalHistory.getAllergies()) ||
                isFieldComplete(medicalHistory.getChronicConditions()) ||
                isFieldComplete(medicalHistory.getCurrentMedications()) ||
                isFieldComplete(medicalHistory.getPreviousSurgeries()) ||
                isFieldComplete(medicalHistory.getFamilyMedicalHistory());
    }

    /**
     * Check if communication preferences have been set
     */
    private boolean arePreferencesSet(Patient patient) {
        // Check if any preference has been explicitly set (not default)
        return patient.getEmailReminders() != null ||
                patient.getSmsReminders() != null ||
                patient.getAppointmentConfirmations() != null ||
                patient.getTestResults() != null ||
                patient.getPromotionalEmails() != null ||
                patient.getShareDataWithResearch() != null ||
                patient.getAllowMarketingCommunication() != null ||
                isFieldComplete(patient.getProfileVisibility());
    }

    private boolean isPersonalInfoComplete(Patient patient) {
        if (patient == null) return false;

        return isFieldComplete(patient.getFirstName()) &&
                isFieldComplete(patient.getLastName()) &&
                patient.getDateOfBirth() != null &&
                isFieldComplete(patient.getGender()) &&
                isFieldComplete(patient.getPhoneNumber()) &&
                isFieldComplete(patient.getIdNumber()) &&
                isAddressComplete(patient.getAddress());
    }

    /**
     * Enhanced medical info completion check
     */
    private boolean isMedicalInfoComplete(Patient patient) {
        if (patient == null || patient.getMedicalHistory() == null) return false;

        MedicalHistory medicalHistory = patient.getMedicalHistory();

        // Basic medical info should be present
        boolean hasBasicInfo = isFieldComplete(medicalHistory.getBloodType()) &&
                medicalHistory.getHeight() != null &&
                medicalHistory.getWeight() != null;

        // At least some medical history should be present
        boolean hasMedicalHistory = hasAnyMedicalHistory(medicalHistory);

        return hasBasicInfo || hasMedicalHistory;
    }

    private boolean isEmergencyContactsComplete(Patient patient) {
        if (patient == null) return false;
        return areEmergencyContactsComplete(patient.getEmergencyContacts());
    }

    /**
     * Insurance info completion check (always true since it's optional)
     */
    private boolean isInsuranceInfoComplete(Patient patient) {
        // Insurance is optional, so this section is always considered complete
        // But we can provide more value by checking if insurance info is consistent
        if (patient == null) return true;

        if (patient.getInsurance() != null) {
            Insurance insurance = patient.getInsurance();
            // If insurance exists, check that basic info is complete
            return isFieldComplete(insurance.getProvider()) &&
                    isFieldComplete(insurance.getPolicyNumber());
        }

        return true; // No insurance is also complete
    }

    public void validateMedicalData(MedicalInfoUpdateDto medicalInfo) {
        if(medicalInfo == null){
            throw new RuntimeException("Medical information cannot be null");
        }
    }

    public boolean hasDoctorPatientRelationship(Long doctorUserId, Long patientUserId) {
        try {
            // Get doctor and patient entities
            Doctor doctor = doctorRepository.findByUser_Id(doctorUserId)
                    .orElse(null);
            Patient patient = patientRepository.findByUser_id(patientUserId)
                    .orElse(null);

            if (doctor == null || patient == null) {
                return false;
            }

            Long doctorId = doctor.getDoctorId();
            Long patientId = patient.getPatientId();

            // Check if doctor has any appointments with this patient
            boolean hasAppointments = appointmentRepository.existsByDoctor_DoctorIdAndPatient_PatientId(doctorId, patientId);

            if (hasAppointments) {
                return true;
            }

            // Check if doctor has prescribed any medications to this patient
            boolean hasPrescriptions = prescriptionRepository.existsByDoctor_DoctorIdAndPatient_PatientId(doctorId, patientId);

            if (hasPrescriptions) {
                return true;
            }

        } catch (Exception e) {
            System.err.println("Error checking doctor-patient relationship: " + e.getMessage());
            return false;
        }
        return false;
    }

    public boolean isIdNumberAvailable(String idNumber, Long userId) {

        return patientRepository.existsByIdNumber(idNumber);
    }

//    public byte[] generateProfilePdf(Long userId) {
//    }

    private static class IdNumberValidator {
        // Basic validation: length and numeric check

        public static boolean IvalidateId(String idNumber) {
            if (idNumber == null || idNumber.length() != 13 || !idNumber.matches("\\d+")) {
                return false;
            }

            // Extract date of birth
            String dobPart = idNumber.substring(0, 6);
            int year = Integer.parseInt(dobPart.substring(0, 2));
            int month = Integer.parseInt(dobPart.substring(2, 4));
            int day = Integer.parseInt(dobPart.substring(4, 6));

            // Determine century
            year += (year >= 0 && year <= LocalDateTime.now().getYear() % 100) ? 2000 : 1900;

            // Validate date
            try {
                LocalDateTime.of(year, month, day, 0, 0);
            } catch (Exception e) {
                return false;
            }

            // Luhn algorithm for checksum validation
            int sum = 0;
            for (int i = 0; i < idNumber.length() - 1; i++) {
                int digit = Character.getNumericValue(idNumber.charAt(i));
                if (i % 2 == 1) {
                    digit *= 2;
                    if (digit > 9) digit -= 9;
                }
                sum += digit;
            }
            int checkDigit = (10 - (sum % 10)) % 10;

            return checkDigit == Character.getNumericValue(idNumber.charAt(idNumber.length() - 1));
        }

        public static IdExtractedInfoDto extractInfoFromId(String idNumber) {

            IdExtractedInfoDto info = new IdExtractedInfoDto();
            String dobPart = idNumber.substring(0, 6);
            int year = Integer.parseInt(dobPart.substring(0, 2));
            int month = Integer.parseInt(dobPart.substring(2, 4));
            int day = Integer.parseInt(dobPart.substring(4, 6));

            info.setAge(LocalDateTime.now().getYear() - (year + ((year >= 0 && year <= LocalDateTime.now().getYear() % 100) ? 2000 : 1900)));
            info.setDateOfBirth(LocalDateTime.of(year, month, day, 0, 0).toLocalDate());
            info.setGender(idNumber.charAt(6) >= 5 ? "Male" : "Female");
            info.setCitizenship(Character.getNumericValue(idNumber.charAt(10)) == 0 ? "SA Citizen" : "Permanent Resident");
            return info;
        }
    }

    private DoctorProfileResponseDto mapToDto(Doctor doctor) {
        DoctorProfileResponseDto dto = new DoctorProfileResponseDto();
        dto.setId(doctor.getDoctorId());
        dto.setUserId(doctor.getUser().getId());
        dto.setFirstName(doctor.getFirstName());
        dto.setLastName(doctor.getLastName());
        dto.setFullName(doctor.getFullName());
        dto.setLicenseNumber(doctor.getMedicalLicenseNumber());
        dto.setEmail(doctor.getEmail());
        dto.setGender(doctor.getGender().name());
        dto.setBio(doctor.getBio());
        dto.setPhoneNumber(doctor.getPhoneNumber());
        dto.setAcceptedInsurance(doctor.getAcceptedInsuranceProviders());
        dto.setSpecialization(doctor.getSpecialization());
        if (doctor.getClinicAddress() != null) {
            AddressDto clinicAddressDto = new AddressDto();
            Address address = doctor.getClinicAddress();
            clinicAddressDto.setStreet(address.getStreet());
            clinicAddressDto.setSuburb(address.getSuburb());
            clinicAddressDto.setCity(address.getCity());
            clinicAddressDto.setProvince(address.getProvince());
            clinicAddressDto.setPostalCode(address.getPostalCode());
            clinicAddressDto.setCountry(address.getCountry());
            dto.setClinicAddress(clinicAddressDto);
            dto.setPrivatePractice(true);
            dto.setPracticeName(doctor.getPracticeName());
        }

        if (doctor.getHospitalAddress() != null) {
            AddressDto hospitalAddressDto = new AddressDto();
            Address address = doctor.getHospitalAddress();
            hospitalAddressDto.setStreet(address.getStreet());
            hospitalAddressDto.setSuburb(address.getSuburb());
            hospitalAddressDto.setCity(address.getCity());
            hospitalAddressDto.setProvince(address.getProvince());
            hospitalAddressDto.setPostalCode(address.getPostalCode());
            hospitalAddressDto.setCountry(address.getCountry());
            dto.setHospitalAddress(hospitalAddressDto);
            dto.setPrivatePractice(false);
            dto.setPracticeName(doctor.getPracticeName());
        }
        dto.setCreatedAt(doctor.getCreatedAt());
        dto.setUpdatedAt(doctor.getUpdatedAt());
        dto.setActive(doctor.getIsActive());
        return dto;
    }
}
