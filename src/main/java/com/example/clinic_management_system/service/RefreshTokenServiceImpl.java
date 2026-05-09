package com.example.clinic_management_system.service;

import com.example.clinic_management_system.dto.response.RefreshTokenResponse;
import com.example.clinic_management_system.entity.RefreshToken;
import com.example.clinic_management_system.entity.User;
import com.example.clinic_management_system.exception.BadRequestException;
import com.example.clinic_management_system.exception.ResourceNotFoundException;
import com.example.clinic_management_system.repository.RefreshTokenRepository;
import com.example.clinic_management_system.repository.UserRepository;
import com.example.clinic_management_system.security.JwtTokenUtil;
import com.example.clinic_management_system.security.TokenPayload;
import com.example.clinic_management_system.utils.TokenConstants;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final PasswordEncoder passwordEncoder;


    @Override
    public void createRefreshTokenForUser(String userId, String userAgent, String ipAddress) {
        // 1. Kiểm tra user này có hợp lệ
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new ResourceNotFoundException("Người dùng không hợp lệ");
        }

        String refreshToken = jwtTokenUtil.generateToken(
                TokenPayload.builder()
                        .userId(userId)
                        .fullName(user.get().getFullName())
                        .role(user.get().getRole().getName())
                        .build(),
                TokenConstants.REFRESH_TOKEN_EXPIRATION

        );

        // 2. Tạo mã refresh token
        refreshTokenRepository.save(RefreshToken.builder()
                        .refreshToken(DigestUtils.sha256Hex(refreshToken))
                        .userAgent(userAgent)
                        .ipAddress(ipAddress)
                        .expiredAt(Instant.now().plus(Duration.ofSeconds(TokenConstants.REFRESH_TOKEN_EXPIRATION)))
                        .user(user.get())
                .build());
    }

    @Override
    @Transactional
    public void deleteRefreshTokenForUser(String userId, String refreshToken) {
        if (userId == null || refreshToken == null) {
            throw new BadRequestException("Thiếu thông tin người dùng hoặc token");
        }
        Optional<RefreshToken> refreshTokenOptional = refreshTokenRepository.findByRefreshToken(refreshToken);
        if (refreshTokenOptional.isEmpty()) {
            throw new ResourceNotFoundException("Mã refresh token không hợp lệ");
        }

        // Kiểm tra mã refresh token có đúng của user
        if (!userId.equals(refreshTokenOptional.get().getUser().getUserId())) {
            throw new BadRequestException("Người dùng không hợp lệ");
        }

        refreshTokenRepository.delete(refreshTokenOptional.get());
    }

    @Override
    public RefreshTokenResponse refreshTokenUser(String refreshToken) {
        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new BadRequestException("Refresh token không hợp lệ");
        }

        // 1. Kiểm tra mã token còn hạn hay không
        if (jwtTokenUtil.isTokenExpired(refreshToken)) {
            throw new BadRequestException("Refresh token đã hết hạn");
        }

        // 2. Kiểm tra mã token hiện tại có tồn tại
        Optional<RefreshToken> refreshTokenOptional = refreshTokenRepository.findByRefreshToken(DigestUtils.sha256Hex(refreshToken));
        if (refreshTokenOptional.isEmpty()) {
            throw new ResourceNotFoundException("Mã refresh token không hợp lệ");
        }

        TokenPayload tokenPayload = jwtTokenUtil.getTokenPayload(refreshToken);

        // 3. Tạo mã access token
        String accessToken = jwtTokenUtil.generateToken(TokenPayload.builder()
                .userId(tokenPayload.getUserId())
                .fullName(tokenPayload.getFullName())
                .role(tokenPayload.getRole())
                .build(), TokenConstants.ACCESS_TOKEN_EXPIRATION);

        return RefreshTokenResponse.builder()
                .accessToken(accessToken)
                .build();
    }
}
