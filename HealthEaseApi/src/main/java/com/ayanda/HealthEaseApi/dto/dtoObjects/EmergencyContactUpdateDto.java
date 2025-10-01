package com.ayanda.HealthEaseApi.dto.dtoObjects;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmergencyContactUpdateDto {
    @NotBlank(message = "Emergency contact name is required")
    private String name;

    @NotBlank(message = "Relationship is required")
    private String relationship;

    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    private String alternatePhoneNumber;

    @Email(message = "Invalid email format")
    private String email;

    private String address;
    private Boolean isPrimary = false;
    private Boolean canMakeDecisions = false;
}
