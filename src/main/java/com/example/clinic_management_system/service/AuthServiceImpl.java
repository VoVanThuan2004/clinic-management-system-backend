package com.example.clinic_management_system.service;
import com.example.clinic_management_system.dto.request.LoginRequest;
import com.example.clinic_management_system.dto.request.LogoutRequest;
import com.example.clinic_management_system.dto.response.LoginResponse;
import com.example.clinic_management_system.entity.RefreshToken;
import com.example.clinic_management_system.entity.User;
import com.example.clinic_management_system.exception.BadRequestException;
import com.example.clinic_management_system.repository.RefreshTokenRepository;
import com.example.clinic_management_system.repository.UserRepository;
import com.example.clinic_management_system.security.JwtTokenUtil;
import com.example.clinic_management_system.security.TokenPayload;
import com.example.clinic_management_system.utils.TokenConstants;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public LoginResponse login(LoginRequest loginRequest, HttpServletRequest httpServletRequest) {
        Optional<User> user = userRepository.findByEmail(loginRequest.getEmail());
        if (user.isEmpty()) {
            throw new BadRequestException("Email không hợp lệ");
        }

        // Kiểm tra password
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.get().getPassword())) {
            throw new BadRequestException("Mật khẩu không hợp lệ");
        }

        // Tạo mã access token
        String accessToken = jwtTokenUtil.generateToken(
                TokenPayload.builder()
                        .userId(user.get().getUserId())
                        .fullName(user.get().getFullName())
                        .role(user.get().getRole().getName())
                        .build(),
                TokenConstants.ACCESS_TOKEN_EXPIRATION
        );


        String refreshToken = jwtTokenUtil.generateToken(
                TokenPayload.builder()
                        .userId(user.get().getUserId())
                        .fullName(user.get().getFullName())
                        .role(user.get().getRole().getName())
                        .build(),
                TokenConstants.REFRESH_TOKEN_EXPIRATION
        );

        // Lưu refresh token đã tạo
        refreshTokenService.createRefreshTokenForUser(user.get().getUserId(),
                httpServletRequest.getHeader("user-agent"), getClientIp(httpServletRequest));

        return LoginResponse.builder()
                .fullName(user.get().getFullName())
                .avatarUrl(user.get().getAvatarUrl())
                .role(user.get().getRole().getName())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public void logout(LogoutRequest logoutRequest) {
        if (logoutRequest.getRefreshToken() == null || logoutRequest.getUserId() == null) {
            throw new BadRequestException("Thiếu thông tin mã refresh token hoặc userId");
        }

        refreshTokenService.deleteRefreshTokenForUser(logoutRequest.getUserId(), logoutRequest.getRefreshToken());
    }

    private String getClientIp(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null || xfHeader.isEmpty()) {
            return request.getRemoteAddr();
        }
        // Nếu có nhiều IP thì lấy IP đầu tiên
        return xfHeader.split(",")[0];
    }
}
