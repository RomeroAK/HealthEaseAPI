package com.ayanda.HealthEaseApi.service;

import com.ayanda.HealthEaseApi.entities.Doctor;
import com.ayanda.HealthEaseApi.repos.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientService {

    public final DoctorRepository doctorRepository;

    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    public List<Doctor> getDoctorsBySpecialization(String specialization) {
        return doctorRepository.findBySpecialization(specialization);
    }

    public Doctor getByLicenseNumber(String licenseNumber) {
        return doctorRepository.findByMedicalLicenseNumber(licenseNumber)
                .orElseThrow(() -> new RuntimeException("Doctor with license number " + licenseNumber + " not found"));
    }
}
