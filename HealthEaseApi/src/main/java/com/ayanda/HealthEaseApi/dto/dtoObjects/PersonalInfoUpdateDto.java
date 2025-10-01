package com.ayanda.HealthEaseApi.dto.dtoObjects;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonalInfoUpdateDto {
    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lastName;

    @Email(message = "Invalid email format")
    private String email;

    @NotNull(message = "Date of birth is required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    @NotBlank(message = "Gender is required")
    private String gender;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^(\\+27|0)[6-8][0-9]{8}$", message = "Invalid South African phone number")
    private String phoneNumber;

    @Pattern(regexp = "^(\\+27|0)[6-8][0-9]{8}$", message = "Invalid South African phone number")
    private String alternatePhoneNumber;

    @NotBlank(message = "ID number is required")
    @Pattern(regexp = "^[0-9]{13}$", message = "ID number must be 13 digits")
    private String idNumber;

    private String occupation;
    private String employer;
    private String maritalStatus;
    private String preferredLanguage;

    @NotNull(message = "Address is required")
    private AddressUpdateDto address;
}
