package com.ayanda.HealthEaseApi.repos;


import com.ayanda.HealthEaseApi.entities.EmergencyContact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmergencyContactsRepository extends JpaRepository<EmergencyContact, Long> {

//    public List<EmergencyContact> findByPatient_Id(Long userId);
//    List<EmergencyContact> findByPatientId(Long patientId);
//    void deleteByPatientId(Long patientId);
}
