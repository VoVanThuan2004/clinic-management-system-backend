package com.example.clinic_management_system.utils;

import java.util.Set;

public class AppointmentStatusConstants {
    public static final String SCHEDULED = "scheduled";
    public static final String CHECKED_IN = "checked_in";
    public static final String EXAMINING = "examining";
    public static final String COMPLETED = "completed";
    public static final String CANCELLED = "cancelled";

    public static final Set<String> VALUES = Set.of(
            SCHEDULED,
            CHECKED_IN,
            EXAMINING,
            COMPLETED,
            CANCELLED
    );
}
