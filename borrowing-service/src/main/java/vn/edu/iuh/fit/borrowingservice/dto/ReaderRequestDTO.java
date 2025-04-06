package vn.edu.iuh.fit.borrowingservice.dto;

import lombok.Data;
import vn.edu.iuh.fit.borrowingservice.enums.BorrowStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
@Data
public class ReaderRequestDTO {
    private UUID id;
    private UUID readerId;
    private UUID librarianId;
    private BorrowStatus status;
    private LocalDateTime dateBorrowed;
    private LocalDateTime dateReturned;
    private LocalDateTime returnDate;
    private Double penaltyFee;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer borrowingPeriod;

    // Nếu cần hiển thị chi tiết sách mượn:
    private List<ReaderRequestDetailDTO> borrowRequestDetails; // Optional
}

