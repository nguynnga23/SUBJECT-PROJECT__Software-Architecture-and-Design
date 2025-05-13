package vn.edu.iuh.fit.borrowingservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.edu.iuh.fit.borrowingservice.entity.ReaderRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ReaderRequestRepository extends JpaRepository<ReaderRequest, UUID> {
    List<ReaderRequest> findByReaderId(UUID readerId);
    // Tổng số phiếu mượn
    @Query("SELECT COUNT(r) FROM ReaderRequest r")
    Long countAllRequests();

    // Phiếu mượn đang chờ
    @Query("SELECT COUNT(r) FROM ReaderRequest r WHERE r.status = 'PENDING'")
    Long countPendingRequests();

    // Phiếu mượn đang chờ
    @Query("SELECT COUNT(r) FROM ReaderRequest r WHERE r.status = 'CANCELED'")
    Long countCanceledRequests();

    // Phiếu mượn đã phê duyệt
    @Query("SELECT COUNT(r) FROM ReaderRequest r WHERE r.status = 'APPROVED'")
    Long countApprovedRequests();

    // Phiếu mượn quá hạn
    @Query("SELECT COUNT(r) FROM ReaderRequest r WHERE r.status = 'OVERDUE'")
    Long countOverdueRequests();


}
