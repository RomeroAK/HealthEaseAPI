package com.ayanda.HealthEaseApi.service;

import com.ayanda.HealthEaseApi.dto.dtoObjects.AppointmentDto;
import com.ayanda.HealthEaseApi.dto.dtoObjects.MedicalHistoryDto;
import com.ayanda.HealthEaseApi.dto.dtoObjects.PatientProfileResponseDto;
import com.ayanda.HealthEaseApi.entities.Doctor;
import com.ayanda.HealthEaseApi.repos.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final MedicalHistoryService medicalHistoryService;
    private final AppointmentService appointmentService;
    private final PatientProfileService profileService;
    private final DoctorRepository doctorRepository;

    public MedicalHistoryDto createMedicalHistory(MedicalHistoryDto medicalHistoryDto){
        return medicalHistoryService.createMedicalHistory(medicalHistoryDto);
    }

    public List<MedicalHistoryDto> getPatientMedicalHistoriesByDoctorId(Long userId){
        return medicalHistoryService.getAllPatientHistoriesByDoctorId(userId);
    }

    public List<AppointmentDto> getAppointmentsByDoctorID(Long doctorId){
        return appointmentService.getAppointmentsByDoctorUserIdSortByDate(doctorId);
    }

    public List<PatientProfileResponseDto> getAllPatientsLinkedTpDoctor(Long userId){
        return profileService.getAllPatientsLinkedToDoctor(userId);
    }

    // Chatbot methods for finding doctors
    public List<Doctor> findDoctorsBySpecialization(String specialization) {
        return doctorRepository.findBySpecializationContainingIgnoreCase(specialization);
    }

    public List<Doctor> findDoctors(String specialization) {
        List<Doctor> doctors;

        if (specialization != null) {
            // Find by specialization first, then filter by location
            doctors = findDoctorsBySpecialization(specialization);
        }  else {
            doctors = doctorRepository.findAll();
        }

        return doctors;
    }
}
