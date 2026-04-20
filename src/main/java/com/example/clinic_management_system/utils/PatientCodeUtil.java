package com.example.clinic_management_system.utils;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

public class PatientCodeUtil {
    private static final String PREFIX = "BN";

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
                    .withZone(ZoneId.of("Asia/Ho_Chi_Minh"));

    public static String generatePatientCode() {
        String timestamp = FORMATTER.format(Instant.now());

        int random = ThreadLocalRandom.current().nextInt(10, 99);

        return PREFIX + timestamp + random;
    }
}
