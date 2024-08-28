package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.Getter;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(0, "Lỗi chưa được phân loại", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_EXISTED(400, "Người dùng đã tồn tại", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(404, "Người dùng không tồn tại", HttpStatus.NOT_FOUND),
    FRESHER_NOT_EXISTED(404, "Fresher không tồn tại", HttpStatus.NOT_FOUND),
    PROJECT_NOT_EXISTED(404, "Project không tồn tại", HttpStatus.NOT_FOUND),
    FRESHERPROJECT_NOT_EXISTED(404, "FresherProject không tồn tại", HttpStatus.NOT_FOUND),
    CENTER_NOT_EXISTED(404, "Center không tồn tại", HttpStatus.NOT_FOUND),
    ASSIGNMENT_NOT_EXISTED(404, "Assignment không tồn tại", HttpStatus.NOT_FOUND),
    NOTIFICATION_NOT_EXISTED(404, "Assignment không tồn tại", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(401, "Không được xác thực", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(403, "Bạn không có quyền", HttpStatus.FORBIDDEN),
    ENTER_MISS_INFO(405, "Nhập thiếu thông tin", HttpStatus.METHOD_NOT_ALLOWED),
    ;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}
