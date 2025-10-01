package com.ayanda.HealthEaseApi.repos;


import com.ayanda.HealthEaseApi.entities.Doctor;
import com.ayanda.HealthEaseApi.entities.Patient;
import com.ayanda.HealthEaseApi.entities.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    List<Prescription> findByPatientIdNumber(String idNumber);
//    List<Prescription> findByPatient_id(Long userId);

    boolean existsByDoctor_DoctorIdAndPatient_PatientId(Long doctorId, Long patientId);


}
