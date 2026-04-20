package com.example.clinic_management_system;

import com.example.clinic_management_system.entity.Role;
import com.example.clinic_management_system.entity.User;
import com.example.clinic_management_system.exception.ResourceNotFoundException;
import com.example.clinic_management_system.repository.RoleRepository;
import com.example.clinic_management_system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // Nếu chưa có dữ liệu role
        if (roleRepository.count() == 0) {
            Role adminRole = Role.builder()
                    .name("ADMIN")
                    .description("Quản trị viên hệ thống - Toàn quyền")
                    .build();

            Role doctorRole = Role.builder()
                    .name("DOCTOR")
                    .description("Bác sĩ - Xem lịch khám và quản lý bệnh án")
                    .build();

            Role employeeRole = Role.builder()
                    .name("EMPLOYEE")
                    .description("Nhân viên phòng khám - Quản lý bệnh nhân và lịch hẹn")
                    .build();

            roleRepository.saveAll(List.of(adminRole, doctorRole, employeeRole));
            System.out.println("Đã khởi tạo dữ liệu role");
        }

        // Tạo tài khoản admin mặc định nếu chưa có
        boolean isAdmin = userRepository.existsUserByRoleName("ADMIN");
        if (!isAdmin) {
            Optional<Role> role = Optional.ofNullable(roleRepository.findRoleByName("ADMIN")
                    .orElseThrow(() -> new ResourceNotFoundException("Vai trò không hợp lệ!")));

            User user = new User();
            user.setEmail("admin@gmail.com");
            user.setPassword(passwordEncoder.encode("12345678"));
            user.setFullName("Admin");
            user.setGender(1);
            user.setPhoneNumber("0935148429");
            user.setAvatarUrl(null);
            user.setRole(role.get());

            userRepository.save(user);
        }
    }
}
