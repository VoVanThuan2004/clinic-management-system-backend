package com.example.clinic_management_system.service;

import com.example.clinic_management_system.dto.request.DoctorUpdateRequest;
import com.example.clinic_management_system.dto.request.UserRequest;
import com.example.clinic_management_system.dto.request.EmployeeUpdateRequest;
import com.example.clinic_management_system.dto.request.UserUpdateRequest;
import com.example.clinic_management_system.dto.response.UploadResult;
import com.example.clinic_management_system.dto.response.UserResponse;
import com.example.clinic_management_system.entity.DoctorDetail;
import com.example.clinic_management_system.entity.Role;
import com.example.clinic_management_system.entity.User;
import com.example.clinic_management_system.exception.BadRequestException;
import com.example.clinic_management_system.exception.ResourceNotFoundException;
import com.example.clinic_management_system.repository.RoleRepository;
import com.example.clinic_management_system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final CloudinaryService cloudinaryService;

    @Override
    @Transactional
    public void createUser(UserRequest userRequest) {
        // Kiểm tra thông tin
        if (userRequest.getRoleId() == null || userRequest.getEmail() == null
            || userRequest.getPassword() == null
        ) {
            throw new BadRequestException("Vui lòng nhập đầy đủ thông tin tạo người dùng");
        }

        // Kiểm tra password
        if (userRequest.getPassword().length() < 8) {
            throw new BadRequestException("Mật khẩu phải có ít nhất 8 ký tự");
        }

        // Kiểm tra người dùng có tồn tại
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            throw new BadRequestException("Email này đã tồn tại");
        }

        // Kiểm tra vai trò có tồn tại
        if (!roleRepository.existsByRoleId(userRequest.getRoleId())) {
            throw new ResourceNotFoundException("Vai trò không hợp lệ!");
        }

        // Tạo user
        User user = new User();
        user.setEmail(userRequest.getEmail());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setFullName(userRequest.getFullName());
        user.setPhoneNumber(userRequest.getPhoneNumber());
        user.setGender(userRequest.getGender());

        Role role = roleRepository.findById(userRequest.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Vai trò không hợp lệ!"));

        user.setRole(role);
        user = userRepository.save(user);

        // Nếu có doctor detail -> tạo thêm thông tin bác sĩ
        if (userRequest.getDoctorDetailRequest() != null) {
            // Kiểm tra roleId có phải là DOCTOR
            if (roleRepository.existsByRoleIdAndName(userRequest.getRoleId(), "DOCTOR")) {
                if (userRequest.getDoctorDetailRequest().getBiography() == null ||
                        userRequest.getDoctorDetailRequest().getSpecialty() == null ||
                        userRequest.getDoctorDetailRequest().getExperienceYears() == null
                ) {
                    throw new BadRequestException("Vui lòng nhập đầy đủ thông tin chi tiết của bác sĩ");
                }

                // Tạo doctor detail
                DoctorDetail doctorDetail = DoctorDetail.builder()
                        .user(user)
                        .biography(userRequest.getDoctorDetailRequest().getBiography())
                        .specialty(userRequest.getDoctorDetailRequest().getSpecialty())
                        .experienceYears(userRequest.getDoctorDetailRequest().getExperienceYears())
                        .build();
            }
        }
    }

    @Override
    public UserResponse getProfile(String userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new ResourceNotFoundException("Người dùng không hợp lệ");
        }

        return UserResponse.builder()
                .userId(userId)
                .email(user.get().getEmail())
                .fullName(user.get().getFullName())
                .phoneNumber(user.get().getPhoneNumber())
                .gender(user.get().getGender())
                .avatarUrl(user.get().getAvatarUrl())
                .role(user.get().getRole().getName())
                .build();
    }

    @Override
    public Page<UserResponse> getAllEmployees(int page, int size, String search) {
        // Tạo object Pageable để phân trang
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<User> users = userRepository.findAllEmployees(pageable, search);
        return users.map(user -> {
            return UserResponse.builder()
                    .userId(user.getUserId())
                    .email(user.getEmail())
                    .fullName(user.getFullName())
                    .phoneNumber(user.getPhoneNumber())
                    .gender(user.getGender())
                    .avatarUrl(user.getAvatarUrl())
                    .role(user.getRole().getName())
                    .build();
        });
    }

    @Override
    @Transactional
    public void updateEmployee(String id, EmployeeUpdateRequest employeeUpdateRequest, MultipartFile file) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new ResourceNotFoundException("Người dùng không tồn tại");
        }

        // Kiểm tra các field update
        if (employeeUpdateRequest.getFullName() == null || employeeUpdateRequest.getPhoneNumber() == null ||
            employeeUpdateRequest.getGender() == null || employeeUpdateRequest.getDateOfBirth() == null
        ) {
            throw new BadRequestException("Vui lòng nhập đầy đủ thông tin");
        }

        // Kiểm tra có upload file
        if (file != null) {
            // Xóa file cũ
            if (user.get().getAvatarPublicId() != null) {
                cloudinaryService.deleteFile(user.get().getAvatarPublicId());
            }

            // Upload ảnh mới lên cloudinary
            UploadResult uploadResult = cloudinaryService.uploadFile(file);
            user.get().setAvatarPublicId(uploadResult.getPublicId());
            user.get().setAvatarUrl(uploadResult.getSecureUrl());
        }

        // Cập nhật thông tin user
        user.get().setFullName(employeeUpdateRequest.getFullName());
        user.get().setPhoneNumber(employeeUpdateRequest.getPhoneNumber());
        user.get().setGender(employeeUpdateRequest.getGender());
        user.get().setDateOfBirth(employeeUpdateRequest.getDateOfBirth());
        userRepository.save(user.get());
    }

    @Override
    public void updateDoctor(String id, DoctorUpdateRequest doctorUpdateRequest, MultipartFile file) {
        Optional<User> doctor = userRepository.findById(id);
        if (doctor.isEmpty()) {
            throw new ResourceNotFoundException("Người dùng không tồn tại");
        }

        // Kiểm tra các field update
        if (doctorUpdateRequest.getFullName() == null || doctorUpdateRequest.getPhoneNumber() == null ||
                doctorUpdateRequest.getGender() == null || doctorUpdateRequest.getDateOfBirth() == null ||
                doctorUpdateRequest.getDoctorDetail().getSpecialty() == null || doctorUpdateRequest.getDoctorDetail().getExperienceYears() == null ||
                doctorUpdateRequest.getDoctorDetail().getBiography() == null
        ) {
            throw new BadRequestException("Vui lòng nhập đầy đủ thông tin");
        }

        // Kiểm tra có upload file
        if (file != null) {
            // Xóa file cũ
            if (doctor.get().getAvatarPublicId() != null) {
                cloudinaryService.deleteFile(doctor.get().getAvatarPublicId());
            }

            // Upload ảnh mới lên cloudinary
            UploadResult uploadResult = cloudinaryService.uploadFile(file);
            doctor.get().setAvatarPublicId(uploadResult.getPublicId());
            doctor.get().setAvatarUrl(uploadResult.getSecureUrl());
        }

        // Cập nhật thông tin user
        doctor.get().setFullName(doctorUpdateRequest.getFullName());
        doctor.get().setPhoneNumber(doctorUpdateRequest.getPhoneNumber());
        doctor.get().setGender(doctorUpdateRequest.getGender());
        doctor.get().setDateOfBirth(doctorUpdateRequest.getDateOfBirth());
        doctor.get().getDoctorDetail().setSpecialty(doctorUpdateRequest.getDoctorDetail().getSpecialty());
        doctor.get().getDoctorDetail().setExperienceYears(doctorUpdateRequest.getDoctorDetail().getExperienceYears());
        doctor.get().getDoctorDetail().setBiography(doctorUpdateRequest.getDoctorDetail().getBiography());
        userRepository.save(doctor.get());
    }

    @Override
    public void updateUser(String id, UserUpdateRequest userUpdateRequest, MultipartFile file) {
        // 1. Kiểm tra thông tin user
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new ResourceNotFoundException("Người dùng không tồn tại");
        }

        // 2. Set các field cần cập nhật
        user.get().setFullName(userUpdateRequest.getFullName());
        user.get().setPhoneNumber(userUpdateRequest.getPhoneNumber());

        // 3. Kiểm tra có upload file mới không
        if (file != null) {
            // 3.1 Xóa ảnh cũ trên cloudinary
            if (user.get().getAvatarPublicId() != null) {
                cloudinaryService.deleteFile(user.get().getAvatarPublicId());
            }

            // 3.2 Kiểm tra định dạng file
            if (file.getSize() > 5 * 1024 * 1024) {
                throw new BadRequestException("File vượt quá 5MB");
            }

            String contentType = file.getContentType();
            List<String> allowedTypes = List.of("image/jpeg", "image/png", "image/jpg");
            if (contentType == null || !allowedTypes.contains(contentType)) {
                throw new BadRequestException("Chỉ chấp nhận JPG, PNG");
            }

            try {
                BufferedImage image = ImageIO.read(file.getInputStream());
                if (image == null) {
                    throw new BadRequestException("File không phải ảnh hợp lệ");
                }
            } catch (IOException e) {
                throw new BadRequestException("Không đọc được file");
            }

            // 3.3 Upload file ảnh lên cloudinary
            UploadResult uploadResult = cloudinaryService.uploadFile(file);
            user.get().setAvatarPublicId(uploadResult.getPublicId());
            user.get().setAvatarUrl(uploadResult.getSecureUrl());
        }

        userRepository.save(user.get());
    }

}
