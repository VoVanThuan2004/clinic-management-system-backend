package com.example.clinic_management_system.service;

import com.example.clinic_management_system.dto.request.AppointmentRequest;
import com.example.clinic_management_system.dto.response.AppointmentDetailResponse;
import com.example.clinic_management_system.dto.response.AppointmentResponse;
import com.example.clinic_management_system.entity.*;
import com.example.clinic_management_system.exception.BadRequestException;
import com.example.clinic_management_system.exception.ResourceNotFoundException;
import com.example.clinic_management_system.mapper.AppointmentMapper;
import com.example.clinic_management_system.repository.*;
import com.example.clinic_management_system.utils.AppointmentStatusConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final UserRepository userRepository;
    private final MedicalServiceRepository medicalServiceRepository;
    private final RoomRepository roomRepository;
    private final AppointmentMapper appointmentMapper;

    @Override
    public void createAppointment(AppointmentRequest appointmentRequest) {
        if (appointmentRequest.getPatientId() == null || appointmentRequest.getDoctorId() == null
            || appointmentRequest.getEmployeeId() == null || appointmentRequest.getRoomId() == null
                || appointmentRequest.getServiceId() == null || appointmentRequest.getReason() == null
                || appointmentRequest.getStartTime() == null || appointmentRequest.getDurationMinutes() == null
        ) {
            throw new BadRequestException("Vui lòng nhập đầy đủ thông tin");
        }

        // 1. Kiểm tra bệnh nhân có hợp lệ
        Optional<Patient> patientExist = patientRepository.findById(appointmentRequest.getPatientId());
        if (patientExist.isEmpty()) {
            throw new ResourceNotFoundException("Bệnh nhân không tồn tại");
        }
        // 2. Kiểm tra bác sĩ có hợp lệ
        Optional<User> doctorExist = userRepository.findById(appointmentRequest.getDoctorId());
        if (doctorExist.isEmpty()) {
            throw new ResourceNotFoundException("Bác sĩ không tồn tại");
        }
        // 3. Kiểm tra nhân viên có hợp lệ
        Optional<User> employeeExist = userRepository.findById(appointmentRequest.getEmployeeId());
        if (employeeExist.isEmpty()) {
            throw new ResourceNotFoundException("Nhân viên không tồn tại");
        }

        // 4. Kiểm tra phòng có hợp lệ
        Optional<MedicalServiceEntity> serviceExist = medicalServiceRepository.findById(appointmentRequest.getServiceId());
        if (serviceExist.isEmpty()) {
            throw new ResourceNotFoundException("Dịch vụ không tồn tại");
        }

        // 5. Kiểm tra phòng khám có hợp lệ
        Optional<Room> roomExist = roomRepository.findById(appointmentRequest.getRoomId());
        if (roomExist.isEmpty()) {
            throw new ResourceNotFoundException("Phòng khám không tồn tại");
        }

        // 6. Ngày khám không được ở quá khứ
        if (appointmentRequest.getStartTime().isBefore(Instant.now())) {
            throw new BadRequestException("Ngày khám ở trong quá khứ");
        }

        // 7. Kiểm tra duration minutes
        if (appointmentRequest.getDurationMinutes() < 0) {
            throw new BadRequestException("Thời lượng khám phải là số dương");
        }

        // 8. Tạo lịch hẹn
        Appointment appointment = appointmentMapper.toEntity(appointmentRequest);
        appointment.setPatient(patientExist.get());
        appointment.setDoctor(doctorExist.get());
        appointment.setEmployee(employeeExist.get());
        appointment.setRoom(roomExist.get());
        appointment.setMedicalServiceEntity(serviceExist.get());

        appointmentRepository.save(appointment);
    }

    @Override
    public void updateAppointment(String appointmentId, AppointmentRequest appointmentRequest) {
        // 1. Kiểm tra lịch hẹn
        Optional<Appointment> appointment = appointmentRepository.findById(appointmentId);
        if (appointment.isEmpty()) {
            throw new ResourceNotFoundException("Lịch hẹn không tồn tại");
        }

        // 2. Kiểm tra thông tin cập nhật
        if (appointmentRequest.getPatientId() == null || appointmentRequest.getDoctorId() == null
                || appointmentRequest.getEmployeeId() == null || appointmentRequest.getRoomId() == null
                || appointmentRequest.getServiceId() == null || appointmentRequest.getReason() == null
                || appointmentRequest.getStartTime() == null || appointmentRequest.getDurationMinutes() == null
        ) {
            throw new BadRequestException("Vui lòng nhập đầy đủ thông tin");
        }

        // 3. Kiểm tra bệnh nhân có hợp lệ
        Optional<Patient> patientExist = patientRepository.findById(appointmentRequest.getPatientId());
        if (patientExist.isEmpty()) {
            throw new ResourceNotFoundException("Bệnh nhân không tồn tại");
        }
        // 4. Kiểm tra bác sĩ có hợp lệ
        Optional<User> doctorExist = userRepository.findById(appointmentRequest.getDoctorId());
        if (doctorExist.isEmpty()) {
            throw new ResourceNotFoundException("Bác sĩ không tồn tại");
        }
        // 5. Kiểm tra nhân viên có hợp lệ
        Optional<User> employeeExist = userRepository.findById(appointmentRequest.getEmployeeId());
        if (employeeExist.isEmpty()) {
            throw new ResourceNotFoundException("Nhân viên không tồn tại");
        }

        // 6. Kiểm tra phòng có hợp lệ
        Optional<MedicalServiceEntity> serviceExist = medicalServiceRepository.findById(appointmentRequest.getServiceId());
        if (serviceExist.isEmpty()) {
            throw new ResourceNotFoundException("Dịch vụ không tồn tại");
        }

        // 7. Kiểm tra phòng khám có hợp lệ
        Optional<Room> roomExist = roomRepository.findById(appointmentRequest.getRoomId());
        if (roomExist.isEmpty()) {
            throw new ResourceNotFoundException("Phòng khám không tồn tại");
        }

        // 8. Ngày khám không được ở quá khứ
        if (appointmentRequest.getStartTime().isBefore(Instant.now())) {
            throw new BadRequestException("Ngày khám ở trong quá khứ");
        }

        // 9. Kiểm tra duration minutes
        if (appointmentRequest.getDurationMinutes() < 0) {
            throw new BadRequestException("Thời lượng khám phải là số dương");
        }

        // 10. Cập nhật lịch hẹn
        appointment.get().setReason(appointmentRequest.getReason());
        appointment.get().setStatus(AppointmentStatusConstants.SCHEDULED);
        appointment.get().setStartTime(appointmentRequest.getStartTime());
        appointment.get().setDurationMinutes(appointmentRequest.getDurationMinutes());
        appointment.get().setPatient(patientExist.get());
        appointment.get().setDoctor(doctorExist.get());
        appointment.get().setEmployee(employeeExist.get());
        appointment.get().setRoom(roomExist.get());
        appointment.get().setMedicalServiceEntity(serviceExist.get());
        appointmentRepository.save(appointment.get());
    }

    @Override
    public void changeAppointmentStatus(String appointmentId, String status) {
        // 2. Kiểm tra lịch hẹn có hợp lệ
        Optional<Appointment> appointment = appointmentRepository.findById(appointmentId);
        if (appointment.isEmpty()) {
            throw new ResourceNotFoundException("Lịch hẹn không tồn tại");
        }

        // 2. Kiểm tra status
        if (status == null) {
            throw new BadRequestException("Vui lòng nhập trạng thái cần thay đổi");
        }

        // 3. Kiểm tra trạng thái có hợp lệ
        if (!AppointmentStatusConstants.VALUES.contains(status)) {
            throw new BadRequestException("Trạng thái lịch hẹn không hợp lệ");
        }

        // 4. Cập nhật trạng thái lịch hẹn
        appointment.get().setStatus(status);
        appointmentRepository.save(appointment.get());
    }

    @Override
    public Page<AppointmentResponse> getAllAppointments(String search, LocalDate date, String doctorId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<Appointment> appointments = appointmentRepository.findAllAppointments(pageable, search, date, doctorId);

        Page<AppointmentResponse> appointmentResponses = appointments.map(appointmentMapper::toResponse);

        return appointmentResponses;
    }

    @Override
    public AppointmentDetailResponse getAppointmentDetail(String appointmentId) {
        // 1. Kiểm tra lịch hẹn có hợp lệ
        Optional<Appointment> appointment = appointmentRepository.findById(appointmentId);
        if (appointment.isEmpty()) {
            throw new ResourceNotFoundException("Lịch hẹn không tồn tại");
        }

        // 2. Trả về response
        return AppointmentDetailResponse.builder()
                .appointmentId(appointment.get().getAppointmentId())
                .patientName(appointment.get().getPatient().getFullName())
                .phoneNumber(appointment.get().getPatient().getPhoneNumber())
                .gender(appointment.get().getPatient().getGender())
                .dateOfBirth(appointment.get().getPatient().getDateOfBirth())
                .doctorName(appointment.get().getDoctor().getFullName())
                .employeeName(appointment.get().getEmployee().getFullName())
                .startTime(appointment.get().getStartTime())
                .status(appointment.get().getStatus())
                .reason(appointment.get().getReason())
                .serviceName(appointment.get().getMedicalServiceEntity().getServiceName())
                .roomName(appointment.get().getRoom().getRoomName())
                .build();
    }
}
