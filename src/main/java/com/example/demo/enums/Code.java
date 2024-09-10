package com.example.demo.enums;

import org.springframework.http.HttpStatus;
import lombok.Getter;

@Getter
public enum Code {

    // 1xx: Authentication & Authorization Errors
    UNAUTHENTICATED(100, "Không được xác thực", HttpStatus.UNAUTHORIZED),
    ACCESS_DENIED(951, "Truy cập bị từ chối", HttpStatus.FORBIDDEN),

    // 2xx: User Errors
    USER_EXISTED(200, "Người dùng đã tồn tại", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(201, "Người dùng không tồn tại", HttpStatus.NOT_FOUND),
    USER_NOT_FOUND(202, "Không tìm thấy người dùng", HttpStatus.NOT_FOUND),

    // 3xx: Project Errors
    PROJECT_NOT_EXISTED(300, "Project không tồn tại", HttpStatus.NOT_FOUND),
    PROJECT_NOT_FOUND(301, "Không tìm thấy Project", HttpStatus.NOT_FOUND),

    // 4xx: Fresher Errors
    FRESHER_NOT_EXISTED(400, "Fresher không tồn tại", HttpStatus.NOT_FOUND),
    FRESHER_NOT_FOUND(401, "Không tìm thấy Fresher", HttpStatus.NOT_FOUND),

    // 5xx: FresherProject Errors
    FRESHERPROJECT_NOT_EXISTED(500, "FresherProject không tồn tại", HttpStatus.NOT_FOUND),
    FRESHERPROJECT_NOT_FOUND(501, "Không tìm thấy FresherProject", HttpStatus.NOT_FOUND),

    // 6xx: Center Errors
    CENTER_NOT_EXISTED(600, "Center không tồn tại", HttpStatus.NOT_FOUND),
    CENTER_NOT_FOUND(601, "Không tìm thấy Center", HttpStatus.NOT_FOUND),

    // 7xx: Assignment Errors
    ASSIGNMENT_NOT_EXISTED(700, "Assignment không tồn tại", HttpStatus.NOT_FOUND),
    ASSIGNMENT_NOT_FOUND(701, "Không tìm thấy Assignment", HttpStatus.NOT_FOUND),

    // 8xx: Notification Errors
    NOTIFICATION_NOT_EXISTED(800, "Notification không tồn tại", HttpStatus.NOT_FOUND),
    NOTIFICATION_NOT_FOUND(801, "Không tìm thấy Notification", HttpStatus.NOT_FOUND),

    // 9xx: Input & Email Errors
    ENTER_MISS_INFO(900, "Nhập thiếu thông tin", HttpStatus.BAD_REQUEST),
    ERROR_SEND_EMAIL(901, "Lỗi gửi email", HttpStatus.INTERNAL_SERVER_ERROR),

    // 0xx: Uncategorized/Internal Errors
    INTERNAL_SERVER_ERROR(0, "Lỗi hệ thống", HttpStatus.INTERNAL_SERVER_ERROR),
    RUNTIME_EXCEPTION(1, "Runtime Exception xảy ra", HttpStatus.INTERNAL_SERVER_ERROR),
    UNCATEGORIZED_EXCEPTION(2, "Lỗi chưa được phân loại", HttpStatus.INTERNAL_SERVER_ERROR),

    // 10xx: Okay Responses
    OK(1000, "OK", HttpStatus.OK),
    LOGIN_SUCCESSFULLY(1001, "Đăng nhập thành công", HttpStatus.OK),
    LOGGED_IN(1002, "Bạn đã đăng nhập", HttpStatus.OK);

    private final int code;
    private final String message;
    private final HttpStatus statusCode;

    Code(int code, String message, HttpStatus statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
}
