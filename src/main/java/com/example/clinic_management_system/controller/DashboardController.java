package com.example.clinic_management_system.controller;

import com.example.clinic_management_system.dto.response.ApiResponse;
import com.example.clinic_management_system.dto.response.RevenueAndProfitStatsDTO;
import com.example.clinic_management_system.dto.response.TodayStatisticsDTO;
import com.example.clinic_management_system.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/dashboard")
public class DashboardController {
    private final DashboardService dashboardService;

    @GetMapping("/today")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<TodayStatisticsDTO>> getTodayStats() {

        TodayStatisticsDTO todayStatisticsDTO = dashboardService.getTodayStatistics();

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.<TodayStatisticsDTO>builder()
                        .status("success")
                        .code(HttpStatus.OK.value())
                        .message("Thống kê tổng quan hôm nay")
                        .data(todayStatisticsDTO)
                .build());
    }

    @GetMapping("/statistic")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<RevenueAndProfitStatsDTO>>> getRevenueAndProfitStatistic(
            @RequestParam LocalDate start,
            @RequestParam LocalDate end,
            @RequestParam String groupBy
            ) {
        List<RevenueAndProfitStatsDTO> revenueAndProfitStats = dashboardService.getRevenueAndProfitStatistic(start, end, groupBy);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.<List<RevenueAndProfitStatsDTO>>builder()
                        .status("success")
                        .code(HttpStatus.OK.value())
                        .message("Thống kê doanh thu, lợi nhuận theo thời gian")
                        .data(revenueAndProfitStats)
                .build());
    }
}
