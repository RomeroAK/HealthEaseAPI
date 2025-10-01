package com.ayanda.HealthEaseApi.dto.dtoObjects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmergencyContactDto {
    private Long id;
    private String name;
    private String relationship;
    private String phoneNumber;
    private String alternatePhoneNumber;
    private String email;
    private String address;
    private Boolean isPrimary;
    private Boolean canMakeDecisions;
}
