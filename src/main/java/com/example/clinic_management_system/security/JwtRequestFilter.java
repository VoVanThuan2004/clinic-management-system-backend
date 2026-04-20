package com.example.clinic_management_system.security;

import com.example.clinic_management_system.entity.User;
import com.example.clinic_management_system.repository.RoleRepository;
import com.example.clinic_management_system.repository.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {
    private final JwtTokenUtil jwtTokenUtil;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 1. Đọc header "Authorization" từ request gửi lên
        String requestHeader = request.getHeader("Authorization");

        String token = null;
        TokenPayload tokenPayload = null;

        // 2. Kiểm tra xem header có chứa Token hợp lệ không (Phải bắt đầu bằng "Bearer ")
        if (requestHeader != null && requestHeader.startsWith("Bearer ")) {
            // Cắt bỏ 7 ký tự đầu "Bearer " để lấy chuỗi token nguyên bản
            token = requestHeader.substring(7);
            try {
                // Giải mã token để lấy thông tin (Payload) bên trong
                tokenPayload = jwtTokenUtil.getTokenPayload(token);
            } catch (ExpiredJwtException e) {
                // NẾU TOKEN HẾT HẠN: Chỉ ghi log, KHÔNG ĐƯỢC THROW EXCEPTION Ở ĐÂY.
                // Việc không throw exception sẽ giúp tokenPayload mang giá trị null,
                // từ đó hệ thống sẽ coi như chưa đăng nhập và gọi đến EntryPoint trả về 401 JSON.
                System.out.println("Cảnh báo: Token đã hết hạn - " + e.getMessage());
            } catch (Exception e) {
                // Bắt các lỗi khác như token sai định dạng, bị sửa đổi chữ ký...
                System.out.println("Cảnh báo: Lỗi xử lý Token - " + e.getMessage());
            }
        } else {
            // NẾU KHÔNG CÓ TOKEN: Cho phép request đi tiếp qua các filter khác.
            // Nếu API này yêu cầu đăng nhập, Spring Security sẽ tự chặn lại ở bước sau.
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Nếu giải mã Token thành công và hệ thống chưa ghi nhận người dùng này đăng nhập
        if (tokenPayload != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Lấy User từ Database dựa trên ID nằm trong Token
            Optional<User> optionalUser = userRepository.findById(tokenPayload.getUserId());

            if (optionalUser.isPresent()) {
                User user = optionalUser.get();

                // 4. Kiểm tra lần cuối xem Token này có thực sự hợp lệ không (còn hạn, đúng thông tin)
                if (jwtTokenUtil.isValidToken(token, tokenPayload)) {

                    // 5. Chuyển đổi quyền (Role) của User từ Database thành định dạng Spring Security hiểu
                    // Ví dụ: Từ "ADMIN" thành "ROLE_ADMIN"
                    List<SimpleGrantedAuthority> authorities = List.of(
                            new SimpleGrantedAuthority("ROLE_" + user.getRole().getName())
                    );

                    // 6. Đóng gói thông tin User vào CustomUserDetail để Spring Security quản lý
                    UserDetails userDetails = new CustomUserDetail(
                            user.getUserId(),
                            user.getFullName(),
                            user.getPassword(),
                            user.getEmail(),
                            authorities
                    );

                    // 7. Tạo "Chứng minh thư" (Authentication) xác nhận người dùng đã hợp lệ
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null, // Credentials (mật khẩu) để null vì mình dùng JWT
                                    userDetails.getAuthorities()
                            );

                    // 8. Đặt "Chứng minh thư" vào SecurityContextHolder.
                    // Kể từ dòng này trở đi, Spring Security ghi nhận user đã đăng nhập thành công.
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    System.out.println("Cảnh báo: Token không vượt qua được hàm isValidToken");
                }
            } else {
                System.out.println("Cảnh báo: Không tìm thấy User trong Database với ID: " + tokenPayload.getUserId());
            }
        }

        // --- ĐOẠN DEBUG (Bạn có thể xóa đi sau khi code chạy thành công) ---
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            System.out.println("Xác thực THÀNH CÔNG");
        } else {
            System.out.println("Xác thực THẤT BẠI - SecurityContext trống (Sẽ bị trả về 401 nếu API yêu cầu quyền)");
        }
        // ------------------------------------------------------------------

        // 9. Dù thành công hay thất bại, bắt buộc phải cho Request đi tiếp đến Controller
        // (hoặc bị Spring Security chặn lại nếu thiếu quyền)
        filterChain.doFilter(request, response);
    }
}