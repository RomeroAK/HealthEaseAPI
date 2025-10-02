package com.ayanda.HealthEaseApi.service;

import com.ayanda.HealthEaseApi.dto.dtoObjects.MedicalHistoryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final MedicalHistoryService medicalHistoryService;

    public MedicalHistoryDto createMedicalHistory(MedicalHistoryDto medicalHistoryDto){
        return medicalHistoryService.createMedicalHistory(medicalHistoryDto);
    }

    public List<MedicalHistoryDto> getPatientMedicalHistoriesByDoctorId(Long userId){
        return medicalHistoryService.getAllPatientHistoriesByDoctorId(userId);
    }
}
