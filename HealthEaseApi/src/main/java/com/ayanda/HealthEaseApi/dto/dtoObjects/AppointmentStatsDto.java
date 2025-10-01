package com.ayanda.HealthEaseApi.dto.dtoObjects;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentStatsDto {

    // Date range for the statistics
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    // Basic counts
    private Long totalAppointments;
    private Long completedAppointments;
    private Long cancelledAppointments;
    private Long pendingAppointments;
    private Long confirmedAppointments;
    private Long noShowAppointments;

    // Financial statistics
    private BigDecimal totalRevenue;
    private BigDecimal pendingRevenue;
    private BigDecimal refundedAmount;
    private BigDecimal averageAppointmentValue;

    // Time-based statistics
    private Double averageConsultationDuration; // in minutes
    private Integer totalConsultationHours;
    private Long onTimeAppointments;
    private Long lateAppointments;

    // Consultation mode breakdown
    private Long inPersonAppointments;
    private Long telemedicineAppointments;
    private Long homeVisitAppointments;

    // Patient statistics
    private Long uniquePatients;
    private Long newPatients;
    private Long returningPatients;

    // Efficiency metrics
    private Double cancellationRate; // percentage
    private Double noShowRate; // percentage
    private Double completionRate; // percentage
    private Double utilizationRate; // percentage of available slots booked

    // Daily statistics (for charts/graphs)
    private Map<LocalDate, Long> dailyAppointmentCounts;
    private Map<LocalDate, BigDecimal> dailyRevenue;

    // Popular time slots
    private Map<String, Long> timeSlotDistribution; // "09:00" -> count

    // Reason for visit statistics
    private Map<String, Long> reasonForVisitCounts;

    // Patient demographics (age groups)
    private Map<String, Long> ageGroupDistribution;

    // Monthly comparison (if applicable)
    private BigDecimal revenueGrowth; // percentage compared to previous period
    private Long appointmentGrowth; // count difference compared to previous period

    // Helper methods for calculated percentages
    public Double getCompletionPercentage() {
        if (totalAppointments == 0) return 0.0;
        return (completedAppointments * 100.0) / totalAppointments;
    }

    public Double getCancellationPercentage() {
        if (totalAppointments == 0) return 0.0;
        return (cancelledAppointments * 100.0) / totalAppointments;
    }

    public Double getNoShowPercentage() {
        if (totalAppointments == 0) return 0.0;
        return (noShowAppointments * 100.0) / totalAppointments;
    }

    public Double getTelemedicinePercentage() {
        if (totalAppointments == 0) return 0.0;
        return (telemedicineAppointments * 100.0) / totalAppointments;
    }

    public Double getNewPatientPercentage() {
        if (uniquePatients == 0) return 0.0;
        return (newPatients * 100.0) / uniquePatients;
    }

    // Helper method to check if stats show improvement
    public Boolean isPerformingWell() {
        return completionRate > 80.0 &&
                cancellationRate < 15.0 &&
                noShowRate < 10.0 &&
                utilizationRate > 70.0;
    }

    // Get the most popular consultation mode
    public String getMostPopularConsultationMode() {
        long maxCount = Math.max(inPersonAppointments,
                Math.max(telemedicineAppointments, homeVisitAppointments));

        if (maxCount == inPersonAppointments) return "In-Person";
        if (maxCount == telemedicineAppointments) return "Telemedicine";
        if (maxCount == homeVisitAppointments) return "Home Visit";
        return "N/A";
    }

    // Get busiest day of the period
    public LocalDate getBusiestDay() {
        if (dailyAppointmentCounts == null || dailyAppointmentCounts.isEmpty()) {
            return null;
        }

        return dailyAppointmentCounts.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    // Get most profitable day
    public LocalDate getMostProfitableDay() {
        if (dailyRevenue == null || dailyRevenue.isEmpty()) {
            return null;
        }

        return dailyRevenue.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }
}
