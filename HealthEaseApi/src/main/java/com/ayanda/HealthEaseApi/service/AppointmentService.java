package com.ayanda.HealthEaseApi.service;

import com.ayanda.HealthEaseApi.dto.dtoObjects.AppointmentDto;
import com.ayanda.HealthEaseApi.dto.dtoObjects.DoctorSummaryDto;
import com.ayanda.HealthEaseApi.dto.dtoObjects.PatientSummaryDto;
import com.ayanda.HealthEaseApi.entities.Appointment;
import com.ayanda.HealthEaseApi.entities.Doctor;
import com.ayanda.HealthEaseApi.entities.Patient;
import com.ayanda.HealthEaseApi.entities.User;
import com.ayanda.HealthEaseApi.repos.AppointmentRepository;
import com.ayanda.HealthEaseApi.repos.DoctorRepository;
import com.ayanda.HealthEaseApi.repos.PatientRepository;
import com.ayanda.HealthEaseApi.repos.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    public List<AppointmentDto> getAllAppointments() {
        return appointmentRepository.findAll().stream()
                .map(this::mapToDto)
                .toList();
    }

    public AppointmentDto bookAppointment(Long patientId, AppointmentDto appointmentDto) {
        User user = userRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + patientId));
        Patient patient = patientRepository.findByUser_id(user.getId())
                .orElseThrow(() -> new RuntimeException("Patient not found for user id: "+user.getId()));

        DoctorSummaryDto dto = appointmentDto.getDoctor();
        Doctor doctor = doctorRepository.findByUser_Id(dto.getId())
                .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + dto.getId()));
        Appointment appointment = mapToEntity(appointmentDto);
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
       Appointment savedAppointment = appointmentRepository.save(appointment);
       return mapToDto(savedAppointment);
    }

    public List<AppointmentDto> getAppointmentsByUserIdSortByDate(Long patientId) {
        User user = userRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + patientId));
        Patient patient = patientRepository.findByUser_id(user.getId())
                .orElseThrow(() -> new RuntimeException("Patient not found for user id: "+user.getId()));
        return appointmentRepository.findByPatient_PatientIdOrderByAppointmentDateDesc(patient.getPatientId()).stream().map(this::mapToDto).toList();
    }

    public AppointmentDto markAppointmentAsCompleted(Long appointmentId, Long userId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + appointmentId));
        appointment.setStatus(Appointment.AppointmentStatus.COMPLETED);
       Appointment updatedAppointment = appointmentRepository.save(appointment);
       return mapToDto(updatedAppointment);
    }

    public AppointmentDto markAppointmentAsCancelled(Long appointmentId, Long userId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + appointmentId));
        appointment.setStatus(Appointment.AppointmentStatus.CANCELLED);
        Appointment savedAppointment = appointmentRepository.save(appointment);
        return mapToDto(savedAppointment);
    }

// In AppointmentService.java

public AppointmentDto mapToDto(Appointment appointment) {
    return AppointmentDto.builder()
            .id(appointment.getAppointmentId())
            .appointmentDate(appointment.getAppointmentDate())
            .appointmentType(appointment.getAppointmentType())
            .status(appointment.getStatus())
            .reason(appointment.getReason())
            .patient(PatientSummaryDto.fromEntity(appointment.getPatient()))
            .doctor(DoctorSummaryDto.fromEntity(appointment.getDoctor()))
            .build();
}

public Appointment mapToEntity(AppointmentDto dto) {
    return Appointment.builder()
            .appointmentId(dto.getId())
            .appointmentDate(dto.getAppointmentDate())
            .appointmentType(dto.getAppointmentType())
            .status(dto.getStatus())
            .reason(dto.getReason())
            .build();
}
}
