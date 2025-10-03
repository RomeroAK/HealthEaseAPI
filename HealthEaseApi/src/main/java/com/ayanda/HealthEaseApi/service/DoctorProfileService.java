package com.ayanda.HealthEaseApi.service;

import com.ayanda.HealthEaseApi.dto.dtoObjects.*;
import com.ayanda.HealthEaseApi.dto.dtoObjects.doctorDtos.DoctorInfoUpdateDto;
import com.ayanda.HealthEaseApi.dto.dtoObjects.doctorDtos.DoctorProfileDto;
import com.ayanda.HealthEaseApi.dto.dtoObjects.doctorDtos.DoctorProfileResponseDto;
import com.ayanda.HealthEaseApi.dto.dtoObjects.doctorDtos.helper.exceptions.DuplicateResourceException;
import com.ayanda.HealthEaseApi.dto.dtoObjects.doctorDtos.helper.exceptions.ResourceNotFoundException;
import com.ayanda.HealthEaseApi.entities.*;
import com.ayanda.HealthEaseApi.repos.DoctorRepository;
import com.ayanda.HealthEaseApi.repos.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class DoctorProfileService {


    private final UserRepository userRepository;

    private final DoctorRepository doctorRepository;


    private final ObjectMapper objectMapper;

    public DoctorProfileResponseDto updateDoctorProfile(Long userId, CompleteProfileUpdateDto profileData){

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Doctor doctor = doctorRepository.findByUser_id(user.getId())
                .orElse(new Doctor());

        if(doctor.getDoctorId()==null){
            // New doctor profile
            doctor.setUser(user);
            doctor.setCreatedAt(LocalDateTime.now());
        }

        updateProfileInfo(doctor, user, profileData.getDoctorInfo());

       Doctor savedDoctor = doctorRepository.save(doctor);
       user.setProfileCompleted(true);

       userRepository.profileCompleteToTrue(user.getId());

       return mapToDto(savedDoctor);

    }


    public DoctorProfileResponseDto getDoctorProfile(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Doctor doctor = doctorRepository.findByUser_id(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor profile for user id", "id", userId));

        return mapToDto(doctor);
    }

    public List<DoctorProfileResponseDto> getDoctorsByNameAndSurname(String name, String surname){
        List<Doctor> doctors = doctorRepository.findByFirstNameAndLastName(name, surname);
        return doctors.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<DoctorProfileResponseDto> getAllDoctors(){
        List<Doctor> doctors = doctorRepository.findAll();
        return doctors.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<DoctorProfileResponseDto> getDoctorsBySpecialization(String specialization){
        List<Doctor> doctors = doctorRepository.findBySpecialization(specialization);
        return doctors.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public DoctorProfileResponseDto getByLicenseNumber(String licenseNumber) {
        Doctor doctor = doctorRepository.findByMedicalLicenseNumber(licenseNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", "license number", 0L));

        return mapToDto(doctor);
    }

    public List<DoctorProfileResponseDto> getDoctorsByConsultationFee(BigDecimal consultationFee){
        List<Doctor> doctors = doctorRepository.findByConsultationFeeLessThanEqual(consultationFee);
        return doctors.stream().map(this::mapToDto).toList();
    }

    public List<PatientProfileResponseDto> getPatientsLinkedToDoctor(Long doctorUserId){
        User user = userRepository.findById(doctorUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", doctorUserId));

        Doctor doctor = doctorRepository.findByUser_Id(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", "user id", user.getId()));

        List<Patient> patients = doctor.getPatients();

        return patients.stream()
                .map(patient -> mapPatientToResponseDto(patient, patient.getUser()))
                .collect(Collectors.toList());
    }

    private void updateProfileInfo(Doctor doctor, User user, DoctorInfoUpdateDto info){

        doctor.setUserId(user.getId());
        doctor.setFirstName(info.getFirstName());
        doctor.setLastName(info.getLastName());
        doctor.setEmail(info.getEmail());
        doctor.setPhoneNumber(info.getTelephone());
        doctor.setFullName(info.getFirstName() + " " + info.getLastName());
        doctor.setSpecialization(info.getSpecialization());
        doctor.setGender(info.getGender().equals("Female")? Doctor.Gender.FEMALE: Doctor.Gender.MALE);
        doctor.setBio(info.getBio());
        doctor.setMedicalLicenseNumber(info.getLicenseNumber());
        doctor.setIsActive(info.isActive());
        List<String> insuranceList = List.of(info.getAcceptedInsurance().split(","));
        doctor.getAcceptedInsuranceProviders().addAll(insuranceList);

        if (info.isPrivatePractice()) {
            if(info.getClinicAddress()!=null){
                Address clinicAddress = doctor.getClinicAddress();
                if(clinicAddress==null){
                    clinicAddress = new Address();
                    doctor.setClinicAddress(clinicAddress);
            }

                clinicAddress.setStreet(info.getClinicAddress().getStreet());
                clinicAddress.setSuburb(info.getClinicAddress().getSuburb());
                clinicAddress.setCity(info.getClinicAddress().getCity());
                clinicAddress.setProvince(info.getClinicAddress().getProvince());
                clinicAddress.setPostalCode(info.getClinicAddress().getPostalCode());
                clinicAddress.setCountry(info.getClinicAddress().getCountry());
            }

            doctor.setPracticeName(info.getPracticeName());

        }else{
            if(info.getHospitalAddress()!=null){
                Address hospitalAddress = doctor.getHospitalAddress();
                if(hospitalAddress==null){
                    hospitalAddress = new Address();
                    doctor.setHospitalAddress(hospitalAddress);
                }

                hospitalAddress.setStreet(info.getHospitalAddress().getStreet());
                hospitalAddress.setSuburb(info.getHospitalAddress().getSuburb());
                hospitalAddress.setCity(info.getHospitalAddress().getCity());
                hospitalAddress.setProvince(info.getHospitalAddress().getProvince());
                hospitalAddress.setPostalCode(info.getHospitalAddress().getPostalCode());
                hospitalAddress.setCountry(info.getHospitalAddress().getCountry());
            }

            doctor.setPracticeName(info.getHospitalName());
        }
        doctor.setEmail(user.getEmail());
        doctor.setConsultationFee(info.getConsultationFee());

    }

    private DoctorProfileResponseDto mapToDto(Doctor doctor) {
        DoctorProfileResponseDto dto = new DoctorProfileResponseDto();
        dto.setId(doctor.getDoctorId());
        dto.setUserId(doctor.getUser().getId());
        dto.setFirstName(doctor.getFirstName());
        dto.setLastName(doctor.getLastName());
        dto.setFullName(doctor.getFullName());
        dto.setLicenseNumber(doctor.getMedicalLicenseNumber());
        dto.setConsultationFee(doctor.getConsultationFee());
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

                if (surgeryStr.contains(":")) {
                    String[] parts = surgeryStr.split(":", -1); // -1 to keep empty strings

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


}
