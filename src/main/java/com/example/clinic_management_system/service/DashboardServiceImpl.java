package com.example.clinic_management_system.service;

import com.example.clinic_management_system.dto.response.RevenueAndProfitStatsDTO;
import com.example.clinic_management_system.dto.response.TodayStatisticsDTO;
import com.example.clinic_management_system.repository.DashboardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {
    private final DashboardRepository dashboardRepository;

    @Override
    public TodayStatisticsDTO getTodayStatistics() {

        // Nếu dữ liệu trong DB lưu theo UTC
        LocalDate todayUTC = LocalDate.now(ZoneOffset.UTC);

        Instant start = todayUTC.atStartOfDay(ZoneOffset.UTC).toInstant();
        Instant end = todayUTC.plusDays(1).atStartOfDay(ZoneOffset.UTC).toInstant();

        Object[] totalStats = dashboardRepository.getTodayStatisticsRaw(start, end);

        return TodayStatisticsDTO.builder()
                .totalPatients(((Number) totalStats[0]).longValue())
                .totalRevenue((BigDecimal) totalStats[1])
                .build();
    }

    @Override
    public List<RevenueAndProfitStatsDTO> getRevenueAndProfitStatistic(LocalDate startTime, LocalDate endTime, String groupBy) {
        // Convert time sang UTC
        Instant start = startTime
                .atStartOfDay(ZoneOffset.UTC)
                .toInstant();

        Instant end = endTime
                .atTime(23,59,59)
                .atZone(ZoneOffset.UTC)
                .toInstant();

        // Lấy data trả về
        List<Object[]> revenueRows = dashboardRepository.getRevenueStats(start, end, groupBy);
        List<Object[]> profitRows = dashboardRepository.getProfitStats(start, end, groupBy);

        Map<String, RevenueAndProfitStatsDTO> map = new TreeMap<>();

        // Duyệt revenues
        for (Object[] r: revenueRows) {
            String period = (String) r[0];
            BigDecimal revenue = (BigDecimal) r[1];

            map.put(period, new RevenueAndProfitStatsDTO(period, revenue, BigDecimal.ZERO));
        }

        // Duyệt profits
        for (Object[] r: profitRows) {
            String period = (String) r[0];
            BigDecimal profit = (BigDecimal) r[1];

            map.computeIfAbsent(period, p -> new RevenueAndProfitStatsDTO(period, BigDecimal.ZERO, BigDecimal.ZERO))
                    .setProfit(profit);
        }


        return new ArrayList<>(map.values());
    }
}
