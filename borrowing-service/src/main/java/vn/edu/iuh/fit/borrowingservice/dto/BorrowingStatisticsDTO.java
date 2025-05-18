package vn.edu.iuh.fit.borrowingservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BorrowingStatisticsDTO {
    private Long totalRequests; // Tổng số phiếu mượn
    private Long pendingRequests; // Phiếu mượn đang chờ
    private Long canceledRequests; // Phiếu mượn đã hủy
    private Long approvedRequests; // Phiếu mượn đã phê duyệt
    private Long borrowedRequests; // Phiếu mượn đã lấy sách
    private Long returnedRequests; // Phiếu mượn đã hoàn thành
    private Long overdueRequests; // Phiếu mượn quá hạn
}
