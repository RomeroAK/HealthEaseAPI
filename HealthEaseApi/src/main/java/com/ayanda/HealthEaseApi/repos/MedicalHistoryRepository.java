package com.ayanda.HealthEaseApi.repos;

import com.ayanda.HealthEaseApi.dto.dtoObjects.MedicalHistoryDto;
import com.ayanda.HealthEaseApi.entities.MedicalHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicalHistoryRepository extends JpaRepository<MedicalHistory, Long> {
    List<MedicalHistory> findByPatient_PatientId(Long patientId);
    List<MedicalHistory> findByDoctor_DoctorId(Long id);
}
