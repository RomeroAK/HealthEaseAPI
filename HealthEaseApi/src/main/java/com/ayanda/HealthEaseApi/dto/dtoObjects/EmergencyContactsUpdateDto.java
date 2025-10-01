package com.ayanda.HealthEaseApi.dto.dtoObjects;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmergencyContactsUpdateDto {
    @NotEmpty(message = "At least one emergency contact is required")
    @Valid
    private List<EmergencyContactUpdateDto> emergencyContacts;
}
