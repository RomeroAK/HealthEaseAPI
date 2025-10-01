package com.ayanda.HealthEaseApi.dto.dtoObjects;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocationDto {

    private Double latitude;

    private Double longitude;

    private String address;

    private String city;

    private String province;

    @JsonProperty("postal_code")
    private String postalCode;

    private String country;

    @JsonProperty("distance_km")
    private Double distanceKm; // Distance from user (if calculated)

    @JsonProperty("distance_text")
    private String distanceText; // Human-readable distance

    // Helper method to check if location is valid
    public boolean isValid() {
        return latitude != null && longitude != null &&
                latitude >= -90 && latitude <= 90 &&
                longitude >= -180 && longitude <= 180;
    }
}
