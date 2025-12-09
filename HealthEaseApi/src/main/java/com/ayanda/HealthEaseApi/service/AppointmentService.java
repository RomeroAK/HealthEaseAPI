package com.ayanda.HealthEaseApi.service;

import com.ayanda.HealthEaseApi.dto.dtoObjects.AppointmentDto;
import com.ayanda.HealthEaseApi.dto.dtoObjects.DoctorSummaryDto;
import com.ayanda.HealthEaseApi.dto.dtoObjects.PatientSummaryDto;
import com.ayanda.HealthEaseApi.dto.dtoObjects.doctorDtos.helper.exceptions.ResourceNotFoundException;
import com.ayanda.HealthEaseApi.entities.*;
import com.ayanda.HealthEaseApi.repos.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final MedicalHistoryRepository medicalHistoryRepository;

    public List<AppointmentDto> getAllAppointments() {
        return appointmentRepository.findAll().stream()
                .map(this::mapToDto)
                .toList();
    }

    public AppointmentDto bookAppointment(Long patientId, AppointmentDto appointmentDto) {
        User user = userRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + patientId));
        Patient patient = patientRepository.findByUser_id(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient", "userId", patientId));

        MedicalHistory medicalHistory = medicalHistoryRepository.findByPatient_PatientId(patient.getPatientId()).get(0);
        patient.setMedicalHistory(medicalHistory);
        Doctor doctor = doctorRepository.findByUser_Id(appointmentDto.getDoctorId())
                .orElseThrow(() ->new ResourceNotFoundException("Doctor not found", "userId", patientId));
        Appointment appointment = mapToEntity(appointmentDto);
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
       Appointment savedAppointment = appointmentRepository.save(appointment);
       AppointmentDto savedDto = mapToDto(savedAppointment);
       savedDto.setPatientId(patient.getPatientId());
       savedDto.setDoctorId(doctor.getDoctorId());
       linkPatientToDoctor(patient, doctor);
       return savedDto;
    }

    public List<AppointmentDto> getAppointmentsByUserIdSortByDate(Long patientId) {
        User user = userRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + patientId));
        Patient patient = patientRepository.findByUser_id(user.getId())
                .orElseThrow(() -> new RuntimeException("Patient not found for user id: "+user.getId()));
        return appointmentRepository.findByPatient_PatientIdOrderByAppointmentDateTimeDesc(patient.getPatientId()).stream().map(this::mapToDto).toList();
    }

    public List<AppointmentDto> getUpcomingAppointments(Long doctorId){
        User user = userRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + doctorId));

        Doctor doctor = doctorRepository.findByUser_id(user.getId())
                .orElseThrow(() -> new RuntimeException("Doctor not found by user_id: "+user.getId()));
        List<Appointment> allAppointments = appointmentRepository.findByDoctor_DoctorId(doctor.getDoctorId());
        List<AppointmentDto> upcomingAppointments = new ArrayList<>();
        for(Appointment ap: allAppointments){
            if(ap.getStatus().equals(Appointment.AppointmentStatus.CONFIRMED.name().toLowerCase())){
                upcomingAppointments.add(mapToDto(ap));
            }
        }

        return upcomingAppointments;

    }
    public List<AppointmentDto> getUpcomingPatientAppointments(Long patientId){
        User user = userRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + patientId));

        Patient doctor = patientRepository.findByUser_id(user.getId())
                .orElseThrow(() -> new RuntimeException("Doctor not found by user_id: "+user.getId()));
        List<Appointment> allAppointments = appointmentRepository.findByPatient_PatientIdOrderByAppointmentDateTimeDesc(patientId);
        List<AppointmentDto> upcomingAppointments = new ArrayList<>();
        for(Appointment ap: allAppointments){
            if(ap.getStatus().equals(Appointment.AppointmentStatus.CONFIRMED.name().toLowerCase())){
                upcomingAppointments.add(mapToDto(ap));
            }
        }

        return upcomingAppointments;

    }
    public List<AppointmentDto> getAppointmentsByDoctorUserIdSortByDate(Long doctorId){
        User user = userRepository.findById(doctorId).orElseThrow(() -> new RuntimeException("User not found with id: "+doctorId));
        Doctor doctor = doctorRepository.findByUser_id(user.getId())
                .orElseThrow(() -> new RuntimeException("Doctor not found with id: "+doctorId));
        return appointmentRepository.findByDoctor_DoctorIdOrderByAppointmentDateTimeDesc(doctor.getDoctorId()).stream().map(this::mapToDto).toList();
    }

    public AppointmentDto markAppointmentAsCompleted(Long appointmentId, Long userId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + appointmentId));
        if(appointment.getStatus().equals(Appointment.AppointmentStatus.CONFIRMED.toString().toLowerCase())){
        appointment.setStatus(Appointment.AppointmentStatus.COMPLETED.name().toLowerCase());
       Appointment updatedAppointment = appointmentRepository.save(appointment);
       return mapToDto(updatedAppointment);
        }

        return mapToDto(appointment);
    }

    public AppointmentDto markAppointmentAsCancelled(Long appointmentId, Long userId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + appointmentId));
        appointment.setStatus(Appointment.AppointmentStatus.CANCELLED.toString().toLowerCase());
        Appointment savedAppointment = appointmentRepository.save(appointment);
        return mapToDto(savedAppointment);
    }

    public AppointmentDto markAppointmentAsAccepted(Long appointmentId, Long userId){
        Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow(() -> new RuntimeException("Appointment not found with id: "+appointmentId));
        appointment.setStatus(Appointment.AppointmentStatus.CONFIRMED.toString().toLowerCase());
        Appointment savedAppointment = appointmentRepository.save(appointment);
        return mapToDto(savedAppointment);
    }

    public void linkPatientToDoctor(Patient patient, Doctor doctor) {

        // Update both sides of the relationship
        if (!patient.getDoctors().contains(doctor)) {
            patient.getDoctors().add(doctor);
        }

        if (!doctor.getPatients().contains(patient)) {
            doctor.getPatients().add(patient);
        }
        // Save the updated entities
        patientRepository.save(patient);
        doctorRepository.save(doctor);
    }

// In AppointmentService.java
public AppointmentDto mapToDto(Appointment appointment) {
    return AppointmentDto.builder()
            .id(appointment.getAppointmentId())
            .appointmentDateTime(appointment.getAppointmentDateTime())
            .appointmentType(appointment.getAppointmentType())
            .status(appointment.getStatus())
            .reason(appointment.getReason())
            .consultationFee(appointment.getDoctor().getConsultationFee())
            .doctorName(appointment.getDoctor().getFirstName() + " " + appointment.getDoctor().getLastName())
            .build();
}

public Appointment mapToEntity(AppointmentDto dto) {
    return Appointment.builder()
            .appointmentId(dto.getId())
            .appointmentDateTime(dto.getAppointmentDateTime())
            .appointmentType(dto.getAppointmentType())
            .status(dto.getStatus())
            .reason(dto.getReason())
            .build();
}


}
