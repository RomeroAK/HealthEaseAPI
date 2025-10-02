package com.ayanda.HealthEaseApi.repos;


import com.ayanda.HealthEaseApi.dto.dtoObjects.AppointmentDto;
import com.ayanda.HealthEaseApi.dto.dtoObjects.AppointmentSearchCriteria;
import com.ayanda.HealthEaseApi.dto.dtoObjects.AppointmentStatsDto;
import com.ayanda.HealthEaseApi.entities.Appointment;
import com.ayanda.HealthEaseApi.entities.Doctor;
import com.ayanda.HealthEaseApi.entities.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    boolean existsByDoctor_DoctorIdAndPatient_PatientId(Long doctorId, Long patientId);


    List<Appointment> findByPatient_PatientIdOrderByAppointmentDateDesc(Long patientId);
}
