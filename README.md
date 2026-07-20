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

## Công nghệ sử dụng

- **Java 19** + **Spring Boot 4.0.5**
- **Spring Security** + **JWT** — xác thực & phân quyền
- **Spring Data JPA** — ORM với Hibernate
- **Spring WebSocket** — thông báo real-time
- **MySQL 8** — cơ sở dữ liệu
- **Cloudinary** — lưu trữ file ảnh
- **Lombok** — giảm boilerplate code
- **Maven** — build tool

## Vai trò (Roles)

| Role | Mô tả |
|------|-------|
| **Admin** | Quản trị hệ thống, quản lý nhân viên & bác sĩ, xem dashboard |
| **Bác sĩ** | Khám bệnh, tạo đơn thuốc, xem hồ sơ bệnh án |
| **Nhân viên** | Tiếp nhận bệnh nhân, đặt lịch hẹn, thanh toán |

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

### Authentication
| Method | Endpoint | Mô tả |
|--------|----------|-------|
| POST | `/api/auth/login` | Đăng nhập |
| POST | `/api/auth/refresh` | Refresh token |
| POST | `/api/auth/logout` | Đăng xuất |

### Users
| Method | Endpoint | Mô tả |
|--------|----------|-------|
| GET | `/api/users` | Danh sách user |
| POST | `/api/users` | Tạo user |
| PUT | `/api/users/{id}` | Cập nhật user |
| DELETE | `/api/users/{id}` | Xóa user |

### Patients
| Method | Endpoint | Mô tả |
|--------|----------|-------|
| GET | `/api/patients` | Danh sách bệnh nhân |
| POST | `/api/patients` | Thêm bệnh nhân |
| PUT | `/api/patients/{id}` | Cập nhật |
| DELETE | `/api/patients/{id}` | Xóa |

### Appointments
| Method | Endpoint | Mô tả |
|--------|----------|-------|
| GET | `/api/appointments` | Danh sách lịch hẹn |
| POST | `/api/appointments` | Tạo lịch hẹn |
| PUT | `/api/appointments/{id}` | Cập nhật |
| DELETE | `/api/appointments/{id}` | Xóa |

### Medical Records
| Method | Endpoint | Mô tả |
|--------|----------|-------|
| GET | `/api/medical-records` | Danh sách hồ sơ |
| POST | `/api/medical-records` | Tạo hồ sơ |
| PUT | `/api/medical-records/{id}` | Cập nhật |
| DELETE | `/api/medical-records/{id}` | Xóa |

### Prescriptions
| Method | Endpoint | Mô tả |
|--------|----------|-------|
| GET | `/api/prescriptions` | Danh sách đơn thuốc |
| POST | `/api/prescriptions` | Tạo đơn thuốc |
| PUT | `/api/prescriptions/{id}` | Cập nhật |
| DELETE | `/api/prescriptions/{id}` | Xóa |

### Medicines
| Method | Endpoint | Mô tả |
|--------|----------|-------|
| GET | `/api/medicines` | Danh sách thuốc |
| POST | `/api/medicines` | Thêm thuốc |
| PUT | `/api/medicines/{id}` | Cập nhật |
| DELETE | `/api/medicines/{id}` | Xóa |

### Medical Services
| Method | Endpoint | Mô tả |
|--------|----------|-------|
| GET | `/api/medical-services` | Danh sách dịch vụ |
| POST | `/api/medical-services` | Thêm dịch vụ |
| PUT | `/api/medical-services/{id}` | Cập nhật |
| DELETE | `/api/medical-services/{id}` | Xóa |

### Rooms
| Method | Endpoint | Mô tả |
|--------|----------|-------|
| GET | `/api/rooms` | Danh sách phòng |
| POST | `/api/rooms` | Thêm phòng |
| PUT | `/api/rooms/{id}` | Cập nhật |
| DELETE | `/api/rooms/{id}` | Xóa |

### Notifications
| Method | Endpoint | Mô tả |
|--------|----------|-------|
| GET | `/api/notifications` | Danh sách thông báo |
| POST | `/api/notifications` | Gửi thông báo |

### Dashboard
| Method | Endpoint | Mô tả |
|--------|----------|-------|
| GET | `/api/dashboard` | Thống kê tổng quan |

## Sơ đồ cơ sở dữ liệu

_Đang cập nhật..._
