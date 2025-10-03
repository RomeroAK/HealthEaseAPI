package com.ayanda.HealthEaseApi.repos;


import com.ayanda.HealthEaseApi.entities.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
//    List<Doctor> findByPracticeName(String practiceName);
//    List<Doctor> findByFirstNameAndLastName(String firstName, String lastName);
//    List<Doctor> findByConsultationFeeLessThanEqual(double maxFee);
//    List<Doctor> findByFieldOfExpertise(String fieldOfExpertise);

    Optional<Doctor> findByUser_Id(Long doctorUserId);
    Optional<Doctor> findByDoctorId(Long id);

    Optional<Doctor> findByIdNumber(String idNumber);


    Optional<Doctor> findByMedicalLicenseNumber(String medicalLicenseNumber);

    List<Doctor> findByConsultationFeeLessThanEqual(BigDecimal consultationFee);

    Optional<Doctor> findByEmail(String email);


    boolean existsByUserId(Long userId);

    List<Doctor> findBySpecialization(String specialization);

    Optional<Doctor> findByUser_id(Long id);


    Optional<Doctor> findByFirstNameIgnoreCaseContainingAndLastNameIgnoreCaseContaining(String doctorName, String doctorName1);

    List<Doctor> findByFirstNameAndLastName(String name, String surname);
}
