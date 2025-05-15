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

    // Phiếu mượn đã phê duyệt
    @Query("SELECT COUNT(r) FROM ReaderRequest r WHERE r.status = 'BORROWED'")
    Long countBorrowedRequests();

    // Phiếu mượn đã phê duyệt
    @Query("SELECT COUNT(r) FROM ReaderRequest r WHERE r.status = 'RETURNED'")
    Long countReturnedRequests();

    // Phiếu mượn quá hạn
    @Query("SELECT COUNT(r) FROM ReaderRequest r WHERE r.status = 'OVERDUE'")
    Long countOverdueRequests();

    // Thống kê số phiếu mượn theo ngày
    @Query("SELECT COUNT(r) FROM ReaderRequest r WHERE DATE(r.createdAt) = :date")
    Long countRequestsByDate(@Param("date") LocalDate date);

    // Thống kê số sách mượn trong ngày
    @Query("SELECT COUNT(d) FROM ReaderRequest r JOIN r.readerRequestDetails d WHERE DATE(r.createdAt) = :date")
    Long countBooksBorrowedByDate(@Param("date") LocalDate date);

    // Top 10 sách được mượn nhiều nhất
//    @Query("SELECT d.bookCopyId, COUNT(d.bookCopyId) as borrowCount " +
//           "FROM ReaderRequest r JOIN r.readerRequestDetails d " +
//           "GROUP BY d.bookCopyId " +
//           "ORDER BY borrowCount DESC")
//    List<Object[]> findTop10BooksBorrowed(org.springframework.data.domain.Pageable pageable);

    // Top 10 thể loại sách được mượn nhiều nhất (giả sử ReaderRequestDetail có trường genre hoặc join với Book entity)
//    @Query("SELECT b.genre, COUNT(b.genre) as genreCount " +
//           "FROM ReaderRequest r JOIN r.readerRequestDetails d JOIN BookCopy bc ON d.bookCopyId = bc.id JOIN Book b ON bc.bookId = b.id " +
//           "GROUP BY b.genre " +
//           "ORDER BY genreCount DESC")
//    List<Object[]> findTop10GenresBorrowed(org.springframework.data.domain.Pageable pageable);
}
