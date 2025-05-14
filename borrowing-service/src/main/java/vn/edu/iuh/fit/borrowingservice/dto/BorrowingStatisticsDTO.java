package vn.edu.iuh.fit.borrowingservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BorrowingStatisticsDTO {
    private Long totalRequests; // Tổng số phiếu mượn
    private Long pendingRequests; // Phiếu mượn đang chờ
    private Long canceledRequests; // Phiếu mượn đang chờ
    private Long approvedRequests; // Phiếu mượn đã phê duyệt
    private Long overdueRequests; // Phiếu mượn quá hạn
}
