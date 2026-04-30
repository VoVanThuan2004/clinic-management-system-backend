package com.example.clinic_management_system.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data @Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookedSlotDTO {
    public Instant startTime;
    public Integer durationMinutes;
}
