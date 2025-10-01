package com.ayanda.HealthEaseApi.repos;


import com.ayanda.HealthEaseApi.entities.Insurance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InsuranceRepository extends JpaRepository<Insurance, Long> {

//    List<Insurance> findByPatient_Id(Long userId);
//    Optional<Insurance> findByPatientId(Long patientId);
}
