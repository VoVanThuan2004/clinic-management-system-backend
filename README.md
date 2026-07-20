# Clinic Management System

Hệ thống quản lý phòng khám - Backend API xây dựng với Spring Boot.

## Công nghệ sử dụng

- **Java 19** + **Spring Boot 4.0.5**
- **Spring Security** + **JWT** (jjwt) — xác thực & phân quyền
- **Spring Data JPA** — ORM
- **Spring WebSocket** — thông báo real-time
- **MySQL 8** — cơ sở dữ liệu
- **Cloudinary** — lưu trữ file ảnh
- **Lombok** — giảm boilerplate code
- **Maven** — build tool

## Vai trò người dùng

| Role | Mô tả |
|------|-------|
| **Admin** | Quản trị hệ thống, quản lý nhân viên/bác sĩ, xem báo cáo |
| **Bác sĩ** | Khám bệnh, tạo đơn thuốc, xem hồ sơ bệnh án |
| **Nhân viên** | Tiếp nhận bệnh nhân, đặt lịch hẹn, thanh toán |

## Tính năng chính

- Xác thực & phân quyền với **JWT** + Refresh Token
- Quản lý **bệnh nhân**, **lịch hẹn**, **hồ sơ bệnh án**
- Quản lý **thuốc**, **toa thuốc**, **dịch vụ y tế**
- Quản lý **phòng khám**, **danh mục dịch vụ**
- **Thanh toán** hóa đơn
- **Thông báo real-time** qua WebSocket
- **Upload file** lên Cloudinary
- **Dashboard** thống kê

## Yêu cầu

- **Java 19**
- **Maven 3.8+**
- **Docker** (cho MySQL)
- **MySQL 8** (nếu không dùng Docker)

## Cài đặt & Chạy

### 1. Khởi động MySQL

```bash
docker compose up -d
```

### 2. Cấu hình

Sửa file `src/main/resources/application.properties` nếu cần.

### 3. Build & Run

```bash
./mvnw spring-boot:run
```

Server chạy tại `http://localhost:8080`.

## API Endpoints

Base path: `/api/v1`

### Authentication (`/api/v1/auth`)
| Method | Endpoint | Mô tả |
|--------|----------|-------|
| POST | `/login` | Đăng nhập |
| POST | `/logout` | Đăng xuất |
| POST | `/v2/logout` | Đăng xuất (cookie) |
| POST | `/change-password` | Đổi mật khẩu |

### Refresh Token (`/api/v1/auth/refresh-token`)
| Method | Endpoint | Mô tả |
|--------|----------|-------|
| POST | `` | Refresh token (cookie) |

### Users (`/api/v1/users`)
| Method | Endpoint | Mô tả |
|--------|----------|-------|
| GET | `` | Lấy profile |
| POST | `` | Tạo user (Admin) |
| GET | `/employees` | Danh sách nhân viên (Admin) |
| GET | `/doctors` | Danh sách bác sĩ (Admin) |
| GET | `/doctors/select` | Danh sách bác sĩ (options) |
| PUT | `/{id}` | Cập nhật profile |
| PUT | `/employees/{id}` | Cập nhật nhân viên |
| PUT | `/doctors/{id}` | Cập nhật bác sĩ |

### Roles (`/api/v1/roles`)
| Method | Endpoint | Mô tả |
|--------|----------|-------|
| GET | `` | Danh sách vai trò |
| GET | `/{roleId}` | Chi tiết vai trò |

### Patients (`/api/v1/patients`)
| Method | Endpoint | Mô tả |
|--------|----------|-------|
| GET | `` | Danh sách bệnh nhân (pagination + search) |
| POST | `` | Thêm bệnh nhân |
| POST | `/import` | Import danh sách |
| PUT | `/{patientId}` | Cập nhật |
| DELETE | `/{patientId}` | Xóa 1 bệnh nhân |
| DELETE | `` | Xóa nhiều (bulk) |
| GET | `/export` | Export danh sách |
| GET | `/{patientId}/history` | Lịch sử khám bệnh |

### Appointments (`/api/v1/appointments`)
| Method | Endpoint | Mô tả |
|--------|----------|-------|
| GET | `` | Danh sách lịch hẹn |
| POST | `` | Tạo lịch hẹn |
| PUT | `/{appointmentId}` | Cập nhật lịch hẹn |
| PUT | `/{appointmentId}/status` | Cập nhật trạng thái |
| GET | `/doctor` | Lịch hẹn của bác sĩ |
| GET | `/{appointmentId}` | Chi tiết lịch hẹn |
| GET | `/{appointmentId}/pdf` | Chi tiết lịch hẹn (PDF) |
| GET | `/booked-slots` | Slots trống của bác sĩ |

### Medical Records (`/api/v1/medical-records`)
| Method | Endpoint | Mô tả |
|--------|----------|-------|
| GET | `` | Danh sách hồ sơ |
| POST | `` | Tạo hồ sơ |
| PUT | `/{medicalRecordId}` | Cập nhật hồ sơ |
| GET | `/{recordId}` | Chi tiết hồ sơ |
| GET | `/{recordId}/pdf` | Chi tiết hồ sơ (PDF) |
| GET | `/check` | Kiểm tra hồ sơ theo lịch hẹn |

### Prescriptions (`/api/v1/prescriptions`)
| Method | Endpoint | Mô tả |
|--------|----------|-------|
| POST | `` | Tạo toa thuốc |
| GET | `/{recordId}` | Chi tiết toa thuốc |

### Prescription Items (`/api/v1/items`)
| Method | Endpoint | Mô tả |
|--------|----------|-------|
| POST | `` | Thêm thuốc vào toa |
| PUT | `/{itemId}/quantity` | Cập nhật số lượng |
| PUT | `/{itemId}/dosage` | Cập nhật liều lượng |
| DELETE | `/{itemId}` | Xóa thuốc khỏi toa |

### Medicines (`/api/v1/medicines`)
| Method | Endpoint | Mô tả |
|--------|----------|-------|
| GET | `` | Danh sách thuốc |
| POST | `` | Thêm thuốc (Admin) |
| PUT | `/{medicineId}` | Cập nhật (Admin) |
| DELETE | `/{medicineId}` | Xóa (Admin) |

### Medical Services (`/api/v1/services`)
| Method | Endpoint | Mô tả |
|--------|----------|-------|
| GET | `` | Danh sách dịch vụ (Admin) |
| GET | `/options` | Danh sách options |
| POST | `` | Thêm dịch vụ (Admin) |
| PUT | `/{serviceId}` | Cập nhật (Admin) |
| DELETE | `/{serviceId}` | Xóa (Admin) |

### Categories (`/api/v1/categories`)
| Method | Endpoint | Mô tả |
|--------|----------|-------|
| GET | `` | Danh sách danh mục (Admin) |
| GET | `/options` | Danh sách options |
| POST | `` | Tạo danh mục (Admin) |
| PUT | `/{categoryId}` | Cập nhật (Admin) |
| DELETE | `/{categoryId}` | Xóa (Admin) |

### Rooms (`/api/v1/rooms`)
| Method | Endpoint | Mô tả |
|--------|----------|-------|
| GET | `` | Danh sách phòng (Admin) |
| GET | `/options` | Danh sách options |
| POST | `` | Thêm phòng (Admin) |
| PUT | `/{roomId}` | Cập nhật (Admin) |
| PUT | `/is-active/{roomId}` | Cập nhật trạng thái (Admin) |
| DELETE | `/{roomId}` | Xóa (Admin) |

### Order Payments (`/api/v1/order-payments`)
| Method | Endpoint | Mô tả |
|--------|----------|-------|
| POST | `` | Tạo hóa đơn thanh toán |
| GET | `/{orderId}` | Chi tiết hóa đơn |

### Record Files (`/api/v1/record-files`)
| Method | Endpoint | Mô tả |
|--------|----------|-------|
| POST | `` | Upload file xét nghiệm |
| GET | `/{recordId}` | Danh sách file của hồ sơ |
| DELETE | `/{fileId}` | Xóa file |

### Notifications (`/api/v1/notifications`)
| Method | Endpoint | Mô tả |
|--------|----------|-------|
| GET | `` | Danh sách thông báo |
| GET | `/total` | Tổng số thông báo |
| PUT | `/{id}/read` | Đánh dấu đã đọc |
| PUT | `/read-all` | Đánh dấu đã đọc tất cả |
| DELETE | `/{id}` | Xóa thông báo |

### Dashboard (`/api/v1/dashboard`)
| Method | Endpoint | Mô tả |
|--------|----------|-------|
| GET | `/today` | Thống kê hôm nay (Admin) |
| GET | `/statistic` | Thống kê doanh thu (Admin) |
| GET | `/top-medicine` | Top thuốc bán chạy (Admin) |

## Sơ đồ cơ sở dữ liệu

_Đang cập nhật..._
