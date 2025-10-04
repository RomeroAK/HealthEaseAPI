package com.ayanda.HealthEaseApi.controller;

import com.ayanda.HealthEaseApi.dto.dtoObjects.ApiResponseDto;
import com.ayanda.HealthEaseApi.dto.dtoObjects.AppointmentDto;
import com.ayanda.HealthEaseApi.dto.dtoObjects.DoctorSearchRequest;
import com.ayanda.HealthEaseApi.dto.dtoObjects.MedicalHistoryDto;
import com.ayanda.HealthEaseApi.dto.dtoObjects.doctorDtos.DoctorProfileResponseDto;
import com.ayanda.HealthEaseApi.dto.dtoObjects.doctorDtos.helper.exceptions.ResourceNotFoundException;
import com.ayanda.HealthEaseApi.dto.dtoObjects.doctorDtos.helper.exceptions.UnauthorizedException;
import com.ayanda.HealthEaseApi.dto.dtoObjects.request.AppointmentBookingRequest;
import com.ayanda.HealthEaseApi.service.AppointmentService;
import com.ayanda.HealthEaseApi.service.DoctorProfileService;
import com.ayanda.HealthEaseApi.service.MedicalHistoryService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientServiceController {


    private final DoctorProfileService doctorServicel;
    private final AppointmentService appointmentService;
    private final MedicalHistoryService medicalHistoryService;

    @GetMapping("service/{userId}/patient/doctors/get-all")
    public ResponseEntity<ApiResponseDto> getAllDoctors(@PathVariable Long userId) {
        try {
            List<DoctorProfileResponseDto> doctors = doctorServicel.getAllDoctors();
            return ResponseEntity.ok(new ApiResponseDto(true, "Doctors retrieved successfully", doctors));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new ApiResponseDto(false, e.getMessage()));
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(500).body(new ApiResponseDto(false, "An error occurred"));
        }
    }

    @PostMapping("service/{userId}/patient/doctors/search")
    public ResponseEntity<ApiResponseDto> searchDoctors(@PathVariable Long userId, @RequestBody DoctorSearchRequest searchRequest) {
        try {
            // Stub: Replace with actual service call
            List<DoctorProfileResponseDto> doctors = doctorServicel.searchDoctors(searchRequest);
            return ResponseEntity.ok(new ApiResponseDto(true, "Doctors retrieved successfully", doctors));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new ApiResponseDto(false, e.getMessage()));
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(500).body(new ApiResponseDto(false, "An error occurred"));
        }
    }

    @GetMapping("service/{userId}/patient/doctors/get-by-specialization/{specialization}")
    public ResponseEntity<ApiResponseDto> getDoctorsBySpecialization(@PathVariable Long userId, @PathVariable String specialization) {
        try {
            List<DoctorProfileResponseDto> doctors = doctorServicel.getDoctorsBySpecialization(specialization);
            return ResponseEntity.ok(new ApiResponseDto(true, "Doctors retrieved successfully", doctors));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new ApiResponseDto(false, e.getMessage()));
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(500).body(new ApiResponseDto(false, "An error occurred"));
        }
    }

    @GetMapping("service/{userId}/patient/doctors/get-by-consultationfee/{fee}")
    public ResponseEntity<ApiResponseDto> getDoctorsByConsultationFee(@PathVariable Long userId, @PathVariable String fee){

        try {
            BigDecimal consultationFee = new BigDecimal(fee);
            List<DoctorProfileResponseDto> doctors = doctorServicel.getDoctorsByConsultationFee(consultationFee);
            return ResponseEntity.ok(new ApiResponseDto(true, "Doctors retrieved successfully", doctors));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new ApiResponseDto(false, e.getMessage()));
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(500).body(new ApiResponseDto(false, "An error occurred"));
        }
    }

    @GetMapping("service/{userId}/patient/doctors/get-by-license/{licenseNumber}")
    public ResponseEntity<ApiResponseDto> getByLicenseNumber(@PathVariable Long userId, @PathVariable String licenseNumber) {
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

    @GetMapping("service/{userId}/patient/appointments/get-all")
    public ResponseEntity<ApiResponseDto> getAllAppointments(@PathVariable Long userId) {
        try {
            List<AppointmentDto> appointments = appointmentService.getAppointmentsByUserIdSortByDate(userId);
            return ResponseEntity.ok(new ApiResponseDto(true, "Appointments retrieved successfully", appointments));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new ApiResponseDto(false, e.getMessage()));
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(500).body(new ApiResponseDto(false, "An error occurred"));
        }
    }

    @GetMapping("{userId}/patient/appointments/upcoming")
    public ResponseEntity<ApiResponseDto> getUpcomingAppointments(@PathVariable Long userId){
        try {
            List<AppointmentDto> appointments = appointmentService.getUpcomingAppointments(userId);
            return ResponseEntity.ok(new ApiResponseDto(true, "Appointments retrieved successfully", appointments));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new ApiResponseDto(false, e.getMessage()));
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(500).body(new ApiResponseDto(false, "An error occurred"));
        }
    }

    @PostMapping("/{userId}/appointments/book")
    public ResponseEntity<ApiResponseDto> bookAppointment(@PathVariable Long userId, @RequestBody AppointmentDto appointmentRequest) {
        try {
            // Assuming appointmentRequest is of the correct type
            Object bookedAppointment = appointmentService.bookAppointment(userId, appointmentRequest);
            return ResponseEntity.ok(new ApiResponseDto(true, "Appointment booked successfully", bookedAppointment));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new ApiResponseDto(false, e.getMessage()));
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(500).body(new ApiResponseDto(false, "An error occurred"));
        }
    }

    @GetMapping("service/{userId}/patient/medical-history/get-all")
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
