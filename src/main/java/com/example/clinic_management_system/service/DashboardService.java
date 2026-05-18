package com.example.clinic_management_system.service;

import com.example.clinic_management_system.dto.response.RevenueAndProfitStatsDTO;
import com.example.clinic_management_system.dto.response.TodayStatisticsDTO;
import com.example.clinic_management_system.dto.response.TopMedicineDTO;

import java.time.LocalDate;
import java.util.List;

public interface DashboardService {
    TodayStatisticsDTO getTodayStatistics();

    List<RevenueAndProfitStatsDTO> getRevenueAndProfitStatistic(LocalDate start, LocalDate end, String groupBy);

    List<TopMedicineDTO> getTopSellingMedicines(LocalDate start, LocalDate end, int limit);
}
