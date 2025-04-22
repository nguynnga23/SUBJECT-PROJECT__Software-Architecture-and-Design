package vn.edu.iuh.fit.borrowingservice.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class BorrowRequestDetailDTO {
    private UUID bookId; // người dùng chỉ cần gửi bookId
}
