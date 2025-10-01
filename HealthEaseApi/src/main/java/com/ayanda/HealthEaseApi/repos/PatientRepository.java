package com.ayanda.HealthEaseApi.repos;


import com.ayanda.HealthEaseApi.entities.Patient;
import com.ayanda.HealthEaseApi.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient,Long> {
    Optional<Patient> findByUser(User user);
    Optional<Patient> findByPatientId(Long id);

    Optional<Patient> findByUser_id(Long userId);

    Optional<Patient> findByIdNumber(String idNumber);

    @Query("SELECT p FROM Patient p LEFT JOIN FETCH p.address LEFT JOIN FETCH p.medicalHistory " +
            "LEFT JOIN FETCH p.emergencyContacts LEFT JOIN FETCH p.insurance WHERE p.user.id = :userId")
    Optional<Patient> findByUserIdWithDetails(@Param("userId") Long userId);

    boolean existsByIdNumber(String idNumber);

    List<Patient> findByFirstNameAndLastName(String name, String surname);
}
