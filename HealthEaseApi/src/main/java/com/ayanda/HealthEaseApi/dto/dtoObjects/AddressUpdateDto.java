package com.ayanda.HealthEaseApi.dto.dtoObjects;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressUpdateDto {
    @NotBlank(message = "Street address is required")
    private String street;

    @NotBlank(message = "Suburb is required")
    private String suburb;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "Province is required")
    private String province;

    @NotBlank(message = "Postal code is required")
    @Pattern(regexp = "^[0-9]{4}$", message = "Postal code must be 4 digits")
    private String postalCode;

    private String country = "South Africa";
}
