package com.example.clinic_management_system.service;

import com.example.clinic_management_system.dto.response.RevenueAndProfitStatsDTO;
import com.example.clinic_management_system.dto.response.TodayStatisticsDTO;
import com.example.clinic_management_system.dto.response.TopMedicineDTO;
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

        Long totalPatients =
                dashboardRepository.countTodayPatients(start, end);

        BigDecimal serviceRevenue =
                dashboardRepository.getServiceRevenue(start, end);

        BigDecimal medicineRevenue =
                dashboardRepository.getMedicineRevenue(start, end);

        BigDecimal totalRevenue =
                serviceRevenue.add(medicineRevenue);

        return TodayStatisticsDTO.builder()
                .totalPatients(totalPatients)
                .totalRevenue(totalRevenue)
                .build();
    }

    @Override
    public List<RevenueAndProfitStatsDTO> getRevenueAndProfitStatistic(
            LocalDate startTime,
            LocalDate endTime,
            String groupBy
    ) {

        Instant start = startTime
                .atStartOfDay(ZoneOffset.UTC)
                .toInstant();

        Instant end = endTime
                .atTime(23, 59, 59)
                .atZone(ZoneOffset.UTC)
                .toInstant();

        List<String> allowed = List.of(
                "day",
                "week",
                "month",
                "quarter",
                "year"
        );

        if (!allowed.contains(groupBy)) {
            throw new IllegalArgumentException("Invalid groupBy");
        }

        List<Object[]> revenueRows =
                dashboardRepository.getRevenueStats(start, end, groupBy);

        List<Object[]> profitRows =
                dashboardRepository.getProfitStats(start, end, groupBy);

        Map<String, RevenueAndProfitStatsDTO> map = new TreeMap<>();

        // Revenue
        for (Object[] r : revenueRows) {

            String period = (String) r[0];

            BigDecimal revenue =
                    r[1] != null
                            ? new BigDecimal(r[1].toString())
                            : BigDecimal.ZERO;

            map.put(
                    period,
                    new RevenueAndProfitStatsDTO(
                            period,
                            revenue,
                            BigDecimal.ZERO
                    )
            );
        }

        // Profit
        for (Object[] r : profitRows) {

            String period = (String) r[0];

            BigDecimal profit =
                    r[1] != null
                            ? new BigDecimal(r[1].toString())
                            : BigDecimal.ZERO;

            map.computeIfAbsent(
                    period,
                    p -> new RevenueAndProfitStatsDTO(
                            p,
                            BigDecimal.ZERO,
                            BigDecimal.ZERO
                    )
            ).setProfit(profit);
        }

        return new ArrayList<>(map.values());
    }

    @Override
    public List<TopMedicineDTO> getTopSellingMedicines(LocalDate start, LocalDate end, int limit) {
        Instant startTime = start
                .atStartOfDay(ZoneOffset.UTC)
                .toInstant();

        Instant endTime = end
                .atTime(23, 59, 59)
                .atZone(ZoneOffset.UTC)
                .toInstant();

        if (limit <= 0) {
            limit = 10;
        }

        List<Object[]> rows =
                dashboardRepository.getTopSellingMedicines(startTime, endTime, limit);

        List<TopMedicineDTO> result = new ArrayList<>();

        for (Object[] r : rows) {

            String medicineId =
                    ((String) r[0]);

            String medicineName =
                    (String) r[1];

            Long totalSold =
                    ((Number) r[2]).longValue();

            result.add(
                    new TopMedicineDTO(
                            medicineId,
                            medicineName,
                            totalSold
                    )
            );
        }

        return result;
    }
}
