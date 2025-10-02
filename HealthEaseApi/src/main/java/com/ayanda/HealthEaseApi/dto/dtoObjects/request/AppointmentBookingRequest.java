package com.ayanda.HealthEaseApi.dto.dtoObjects.request;

import com.ayanda.HealthEaseApi.dto.dtoObjects.AppointmentDto;
import com.ayanda.HealthEaseApi.entities.Appointment;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentBookingRequest {

    private AppointmentDto appointmentInfo;
}
