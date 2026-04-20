package com.example.clinic_management_system.mapper;

import com.example.clinic_management_system.dto.request.AppointmentRequest;
import com.example.clinic_management_system.dto.response.AppointmentResponse;
import com.example.clinic_management_system.entity.Appointment;
import com.example.clinic_management_system.utils.AppointmentStatusConstants;
import org.springframework.stereotype.Component;

@Component
public class AppointmentMapper {

    public AppointmentResponse toResponse(Appointment appointment) {
        return AppointmentResponse.builder()
                .appointmentId(appointment.getAppointmentId())
                .patientName(appointment.getPatient().getFullName())
                .phoneNumber(appointment.getPatient().getPhoneNumber())
                .avatarUrl(appointment.getDoctor().getAvatarUrl())
                .doctorName(appointment.getDoctor().getFullName())
                .startTime(appointment.getStartTime())
                .status(appointment.getStatus())
                .reason(appointment.getReason())
                .serviceName(appointment.getMedicalServiceEntity().getServiceName())
                .roomName(appointment.getRoom().getRoomName())
                .build();
    }

    public Appointment toEntity(AppointmentRequest appointmentRequest) {
        return Appointment.builder()
                .reason(appointmentRequest.getReason())
                .status(AppointmentStatusConstants.SCHEDULED)
                .startTime(appointmentRequest.getStartTime())
                .durationMinutes(appointmentRequest.getDurationMinutes())
                .build();
    }
}
