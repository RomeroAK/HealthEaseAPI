package com.ayanda.HealthEaseApi.controller;

import com.ayanda.HealthEaseApi.dto.dtoObjects.ApiResponseDto;
import com.ayanda.HealthEaseApi.dto.dtoObjects.AppointmentDto;
import com.ayanda.HealthEaseApi.dto.dtoObjects.MedicalHistoryDto;
import com.ayanda.HealthEaseApi.dto.dtoObjects.MedicalRecordInfo;
import com.ayanda.HealthEaseApi.dto.dtoObjects.doctorDtos.helper.exceptions.ResourceNotFoundException;
import com.ayanda.HealthEaseApi.dto.dtoObjects.doctorDtos.helper.exceptions.UnauthorizedException;
import com.ayanda.HealthEaseApi.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/doctor/service")
@RequiredArgsConstructor
public class DoctorServiceController {

    private final DoctorService doctorService;

    @PostMapping("{userId}/doctor/medical-history/create")
    public ResponseEntity<ApiResponseDto> createPatientMedical(@PathVariable Long userId, MedicalRecordInfo recordData){
        try {
            MedicalHistoryDto savedMH = doctorService.createMedicalHistory(recordData.getMedicalHistoryInfo());
            return ResponseEntity.ok(new ApiResponseDto(true, "Medical history saved successfully", savedMH));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new ApiResponseDto(false, e.getMessage()));
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(500).body(new ApiResponseDto(false, "An error occurred"));
        }

    }

    @GetMapping("{userId}/doctor/medical-history/get")
    public ResponseEntity<ApiResponseDto> getPatientMedicalHistory(@PathVariable Long userId){
        try {
            List<MedicalHistoryDto> medicalHistoryDtoList = doctorService.getPatientMedicalHistoriesByDoctorId(userId);
            return ResponseEntity.ok(new ApiResponseDto(true, "Medical history saved successfully", medicalHistoryDtoList));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new ApiResponseDto(false, e.getMessage()));
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(500).body(new ApiResponseDto(false, "An error occurred"));
        }
    }

    @GetMapping("{userId}/doctor/appointments/get-all")
    public ResponseEntity<ApiResponseDto> getAppointmentsByIdOrderByDate(@PathVariable Long userId){
        try{
            List<AppointmentDto> appointmentDtoList = doctorService.getAppointmentsByDoctorID(userId);
            return ResponseEntity.status(200).body(new ApiResponseDto(true,"Success got appointments",appointmentDtoList));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new ApiResponseDto(false, e.getMessage()));
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(500).body(new ApiResponseDto(false, "An error occurred"));
        }
    }
}
