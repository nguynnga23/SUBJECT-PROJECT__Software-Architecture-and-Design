package com.notificationservice.enums;

public enum NotificationType {
    DUE_DATE_REMINDER,            // Nhắc sắp đến hạn trả sách
    BORROW_CONFIRMATION,          // Xác nhận đã mượn thành công
    OVERDUE_NOTICE,               // Thông báo quá hạn
    FINE_NOTICE,                  // Thông báo phí phạt
    GENERAL_ANNOUNCEMENT,         // Thông báo chung từ thư viện (nghỉ lễ, thông báo mới)
    ACCOUNT_LOCKED,               // Tài khoản bị khóa
    BOOK_AVAILABLE                // Sách đã có lại (trước đó hết sách)
}


