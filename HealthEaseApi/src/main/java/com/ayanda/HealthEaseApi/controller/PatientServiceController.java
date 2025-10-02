package com.ayanda.HealthEaseApi.controller;

import com.ayanda.HealthEaseApi.dto.dtoObjects.ApiResponseDto;
import com.ayanda.HealthEaseApi.dto.dtoObjects.AppointmentDto;
import com.ayanda.HealthEaseApi.dto.dtoObjects.MedicalHistoryDto;
import com.ayanda.HealthEaseApi.dto.dtoObjects.doctorDtos.DoctorProfileResponseDto;
import com.ayanda.HealthEaseApi.dto.dtoObjects.doctorDtos.helper.exceptions.ResourceNotFoundException;
import com.ayanda.HealthEaseApi.dto.dtoObjects.doctorDtos.helper.exceptions.UnauthorizedException;
import com.ayanda.HealthEaseApi.dto.dtoObjects.request.AppointmentBookingRequest;
import com.ayanda.HealthEaseApi.service.AppointmentService;
import com.ayanda.HealthEaseApi.service.DoctorProfileService;
import com.ayanda.HealthEaseApi.service.MedicalHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/patient/service")
@RequiredArgsConstructor
public class PatientServiceController {


    private final DoctorProfileService doctorServicel;
    private final AppointmentService appointmentService;
    private final MedicalHistoryService medicalHistoryService;

    @GetMapping("{userId}/patient/doctors/get-all")
    public ResponseEntity<ApiResponseDto> getAllDoctors() {
        try {
            List<DoctorProfileResponseDto> doctors = doctorServicel.getAllDoctors();
            return ResponseEntity.ok(new ApiResponseDto(true, "Doctors retrieved successfully", doctors));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new ApiResponseDto(false, e.getMessage()));
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(500).body(new ApiResponseDto(false, "An error occurred"));
        }
    }

    @GetMapping("{userId}/patient/doctors/get-by-specialization/{specialization}")
    public ResponseEntity<ApiResponseDto> getDoctorsBySpecialization(@PathVariable String specialization) {
        try {
            List<DoctorProfileResponseDto> doctors = doctorServicel.getDoctorsBySpecialization(specialization);
            return ResponseEntity.ok(new ApiResponseDto(true, "Doctors retrieved successfully", doctors));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new ApiResponseDto(false, e.getMessage()));
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(500).body(new ApiResponseDto(false, "An error occurred"));
        }
    }

    @GetMapping("{userId}/patient/doctors/get-by-license/{licenseNumber}")
    public ResponseEntity<ApiResponseDto> getByLicenseNumber(@PathVariable String licenseNumber) {
        try {
            DoctorProfileResponseDto doctor = doctorServicel.getByLicenseNumber(licenseNumber);
            return ResponseEntity.ok(new ApiResponseDto(true, "Doctor retrieved successfully", doctor));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new ApiResponseDto(false, e.getMessage()));
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(500).body(new ApiResponseDto(false, "An error occurred"));
        }
    }

    //Appointment endpoints can be added here

    @GetMapping("{userId}/patient/appointments/get-all")
    public ResponseEntity<ApiResponseDto> getAllAppointments(Long userId) {
        try {
            List<AppointmentDto> appointments = appointmentService.getAppointmentsByUserIdSortByDate(userId);
            return ResponseEntity.ok(new ApiResponseDto(true, "Appointments retrieved successfully", appointments));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new ApiResponseDto(false, e.getMessage()));
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(500).body(new ApiResponseDto(false, "An error occurred"));
        }
    }

    @PostMapping("{userId}/patient/appointments/book")
    public ResponseEntity<ApiResponseDto> bookAppointment(Long userId, AppointmentBookingRequest appointmentRequest) {
        try {
            // Assuming appointmentRequest is of the correct type
            Object bookedAppointment = appointmentService.bookAppointment(userId, appointmentRequest.getAppointmentInfo());
            return ResponseEntity.ok(new ApiResponseDto(true, "Appointment booked successfully", bookedAppointment));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new ApiResponseDto(false, e.getMessage()));
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(500).body(new ApiResponseDto(false, "An error occurred"));
        }
    }

    @GetMapping("{userId}/patient/medical-history/get-all")
    public ResponseEntity<ApiResponseDto> getMedicalHistory(@PathVariable Long userId) {
        try {
            List<MedicalHistoryDto> medicalHistories = medicalHistoryService.getAllPatientHistoriesByPatientId(userId);
            return ResponseEntity.ok(new ApiResponseDto(true, "Medical histories retrieved successfully", medicalHistories));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new ApiResponseDto(false, e.getMessage()));
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(500).body(new ApiResponseDto(false, "An error occurred"));
        }
    }



}
