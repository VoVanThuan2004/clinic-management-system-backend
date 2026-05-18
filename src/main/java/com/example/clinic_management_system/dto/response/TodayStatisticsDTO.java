package com.example.clinic_management_system.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor @NoArgsConstructor
@Builder
public class TodayStatisticsDTO {
    private Long totalPatients;
    private BigDecimal totalRevenue;

}
