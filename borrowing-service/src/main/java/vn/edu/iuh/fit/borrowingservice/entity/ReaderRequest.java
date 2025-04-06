package vn.edu.iuh.fit.borrowingservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import vn.edu.iuh.fit.borrowingservice.enums.BorrowStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
@Entity
@Table(name = "reader_requests")
@Getter
@Setter
@NoArgsConstructor
public class ReaderRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "reader_id", nullable = false)
    private UUID readerId;

    @Column(name = "librarian_id")
    private UUID librarianId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private BorrowStatus status = BorrowStatus.PENDING;

    @Column(name = "date_borrowed")
    private LocalDateTime dateBorrowed;

    @Column(name = "date_returned")
    private LocalDateTime dateReturned;

    @Column(name = "return_date")
    private LocalDateTime returnDate;

    @Column(name = "penalty_fee")
    private Double penaltyFee;

    @Column(name = "created_at", updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Column(name = "borrowing_period")
    private Integer borrowingPeriod;

    @OneToMany(mappedBy = "readerRequest", cascade = CascadeType.ALL)
    private List<ReaderRequestDetail> borrowRequestDetails;
}
