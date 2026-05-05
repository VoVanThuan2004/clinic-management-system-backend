package com.example.clinic_management_system.service;

import com.example.clinic_management_system.dto.request.AppointmentRequest;
import com.example.clinic_management_system.dto.response.AppointmentDetailPDFResponse;
import com.example.clinic_management_system.dto.response.AppointmentDetailResponse;
import com.example.clinic_management_system.dto.response.AppointmentResponse;
import com.example.clinic_management_system.dto.response.BookedSlotDTO;
import org.springframework.data.domain.Page;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

public interface AppointmentService {
    void createAppointment(AppointmentRequest appointmentRequest);

    void updateAppointment(String appointmentId, AppointmentRequest appointmentRequest);

    void changeAppointmentStatus(String appointmentId, String status);

    Page<AppointmentResponse> getAllAppointments(String search, LocalDate date, String doctorId, int page, int size);

    AppointmentDetailPDFResponse getAppointmentDetailPDF(String appointmentId);

    List<BookedSlotDTO> getBookedSlots(String doctorId, LocalDate date, String roomId);

    List<AppointmentResponse> getAllAppointmentsOfDoctor(String doctorId, Instant startTime, Instant endTime);

    AppointmentDetailResponse getAppointmentDetail(String appointmentId);
}
