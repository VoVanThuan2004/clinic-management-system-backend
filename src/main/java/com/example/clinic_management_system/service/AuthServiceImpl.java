package com.example.clinic_management_system.service;
import com.example.clinic_management_system.dto.request.ChangePasswordRequest;
import com.example.clinic_management_system.dto.request.LoginRequest;
import com.example.clinic_management_system.dto.request.LogoutRequest;
import com.example.clinic_management_system.dto.response.LoginResponse;
import com.example.clinic_management_system.entity.User;
import com.example.clinic_management_system.exception.BadRequestException;
import com.example.clinic_management_system.exception.ResourceNotFoundException;
import com.example.clinic_management_system.repository.RefreshTokenRepository;
import com.example.clinic_management_system.repository.UserRepository;
import com.example.clinic_management_system.security.JwtTokenUtil;
import com.example.clinic_management_system.security.TokenPayload;
import com.example.clinic_management_system.utils.TokenConstants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.cli.Digest;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
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
    public LoginResponse login(LoginRequest loginRequest, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        Optional<User> user = userRepository.findByEmail(loginRequest.getEmail());
        if (user.isEmpty()) {
            throw new BadRequestException("Email không hợp lệ");
        }

        System.out.println("User mk: " + user.get().getPassword());

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


        // Set refresh token vào cookie response
        addRefreshTokenCookie(httpServletResponse, refreshToken);

        return LoginResponse.builder()
                .userId(user.get().getUserId())
                .fullName(user.get().getFullName())
                .avatarUrl(user.get().getAvatarUrl())
                .role(user.get().getRole().getName())
                .accessToken(accessToken)
                .build();
    }

    @Override
    public void logout(LogoutRequest logoutRequest) {
        if (logoutRequest.getRefreshToken() == null || logoutRequest.getUserId() == null) {
            throw new BadRequestException("Thiếu thông tin mã refresh token hoặc userId");
        }

        refreshTokenService.deleteRefreshTokenForUser(logoutRequest.getUserId(), logoutRequest.getRefreshToken());
    }

    @Override
    public void logoutAccount(String refreshToken, HttpServletResponse response) {

        // 1. Xóa cookie ở client
        ResponseCookie deleteCookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)  // set maxAge=0 để xóa cookie
                .sameSite("Lax")
                .build();

        response.setHeader(HttpHeaders.SET_COOKIE, deleteCookie.toString());

        // 2. Nếu không có refresh token -> throw lỗi
        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new BadRequestException("Refresh token không hợp lệ");
        }

        TokenPayload tokenPayload = jwtTokenUtil.getTokenPayload(refreshToken);

        // 3. Xóa refresh token
        refreshTokenService.deleteRefreshTokenForUser(tokenPayload.getUserId(), DigestUtils.sha256Hex(refreshToken));
    }

    @Override
    public void changePassword(String userId, ChangePasswordRequest changePasswordRequest) {
        if (userId == null || userId.isEmpty()) {
            throw new BadRequestException("Vui lòng đăng nhập tài khoản");
        }

        // Kiểm tra mật khẩu hiện tại có khác mật khẩu mới
        if (changePasswordRequest.getOldPassword().equals(changePasswordRequest.getNewPassword())) {
            throw new BadRequestException("Mật khẩu mới đang trùng với mật khẩu hiện tại");
        }

        // So sánh mật khẩu mới có khớp với mật khẩu xác nhận
        if (!changePasswordRequest.getNewPassword().equals(changePasswordRequest.getConfirmNewPassword())) {
            throw new BadRequestException("Mật khẩu mới và mật khẩu xác nhận không khớp");
        }

        // Set new password cho user
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new ResourceNotFoundException("Người dùng không tồn tại");
        }

        user.get().setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        userRepository.save(user.get());
    }

    private String getClientIp(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null || xfHeader.isEmpty()) {
            return request.getRemoteAddr();
        }
        // Nếu có nhiều IP thì lấy IP đầu tiên
        return xfHeader.split(",")[0];
    }

    private void addRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(false)
                .sameSite("Lax")
                .path("/")
                .maxAge(TokenConstants.REFRESH_TOKEN_EXPIRATION)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
}
