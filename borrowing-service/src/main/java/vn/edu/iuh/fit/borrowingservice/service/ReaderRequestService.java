package vn.edu.iuh.fit.borrowingservice.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.iuh.fit.borrowingservice.annotation.RequireAdmin;
import vn.edu.iuh.fit.borrowingservice.clients.UserServiceClient;
import vn.edu.iuh.fit.borrowingservice.dto.ReaderRequestDTO;
import vn.edu.iuh.fit.borrowingservice.dto.UpdateStatusDTO;
import vn.edu.iuh.fit.borrowingservice.dto.UserDTO;
import vn.edu.iuh.fit.borrowingservice.entity.ReaderRequest;
import vn.edu.iuh.fit.borrowingservice.entity.ReaderRequestDetail;
import vn.edu.iuh.fit.borrowingservice.enums.BorrowStatus;
import vn.edu.iuh.fit.borrowingservice.repository.ReaderRequestRepository;

import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ReaderRequestService {

    @Autowired
    private ReaderRequestRepository readerRequestRepository;

    @Autowired
    private UserServiceClient userServiceClient;

    @Autowired
    private HttpServletRequest request;

    public ReaderRequest createBorrowRequest(ReaderRequestDTO requestDTO) {
        // Lấy readerId từ HTTP Header do API Gateway truyền xuống
        String readerId = request.getHeader("X-User-Id");
        if (readerId == null || readerId.isEmpty()) {
            throw new IllegalArgumentException("Missing Reader ID in request header.");
        }

        // Kiểm tra thông tin mượn
        if (requestDTO.getBorrowRequestDetails() == null || requestDTO.getBorrowRequestDetails().isEmpty()) {
            throw new IllegalArgumentException("The list of books must not be left blank.");
        }
        if (requestDTO.getBorrowingPeriod() == null || requestDTO.getBorrowingPeriod() <= 0) {
            throw new IllegalArgumentException("The borrowing period must be greater than 0 and not left blank.");
        }

        try {
            // Tạo ReaderRequest từ DTO
            ReaderRequest readerRequest = new ReaderRequest();
            readerRequest.setReaderId(UUID.fromString(readerId));
            readerRequest.setStatus(BorrowStatus.PENDING);
            readerRequest.setBorrowingPeriod(requestDTO.getBorrowingPeriod());
            readerRequest.setCreatedAt(LocalDateTime.now());
            readerRequest.setUpdatedAt(LocalDateTime.now());

            // Tính toán ngày trả sách
            LocalDateTime returnDate = LocalDateTime.now().plusDays(requestDTO.getBorrowingPeriod());
            readerRequest.setReturnDate(returnDate);

            // Map borrowRequestDetails từ DTO sang Entity
            List<ReaderRequestDetail> requestDetails = requestDTO.getBorrowRequestDetails().stream()
                    .map(detailDTO -> {
                        ReaderRequestDetail detail = new ReaderRequestDetail();
                        detail.setReaderRequest(readerRequest);
                        detail.setBookCopyId(detailDTO.getBookCopyId());
                        return detail;
                    }).collect(Collectors.toList());

            readerRequest.setBorrowRequestDetails(requestDetails);

            // Lưu yêu cầu mượn vào cơ sở dữ liệu
            return readerRequestRepository.save(readerRequest);

        } catch (RuntimeException e) {
            throw new RuntimeException("Error creating borrow request: " + e.getMessage());
        }
    }

    @RequireAdmin
    public ReaderRequest updateStatus(UUID requestId, UpdateStatusDTO statusDTO) {
        ReaderRequest readerRequest = readerRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Not found requestId"));

        if (!EnumSet.allOf(BorrowStatus.class).contains(statusDTO.getStatus())) {
            throw new IllegalArgumentException("Invalid status: " + statusDTO.getStatus());
        }

        readerRequest.setStatus(statusDTO.getStatus());
        readerRequest.setLibrarianId(UUID.fromString(request.getHeader("X-User-Id")));
        readerRequest.setUpdatedAt(LocalDateTime.now());
        return readerRequestRepository.save(readerRequest);
    }

    @RequireAdmin
    public ReaderRequest updateBorrowDate(UUID requestId) {
        ReaderRequest readerRequest = readerRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Not found requestId"));

        readerRequest.setDateBorrowed(LocalDateTime.now());
        readerRequest.setStatus(BorrowStatus.BORROWED);
        readerRequest.setUpdatedAt(LocalDateTime.now());
        return readerRequestRepository.save(readerRequest);
    }

    @RequireAdmin
    public ReaderRequest updateReturnDate(UUID requestId) {
        ReaderRequest readerRequest = readerRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Not found requestId"));

        if (readerRequest.getReturnDate().isBefore(LocalDateTime.now())) {
            readerRequest.setDateReturned(LocalDateTime.now());
            readerRequest.setStatus(BorrowStatus.OVERDUE);
            readerRequest.setUpdatedAt(LocalDateTime.now());
            return readerRequestRepository.save(readerRequest);
        }
        readerRequest.setDateReturned(LocalDateTime.now());
        readerRequest.setStatus(BorrowStatus.RETURNED);
        readerRequest.setUpdatedAt(LocalDateTime.now());
        return readerRequestRepository.save(readerRequest);
    }

    @RequireAdmin
    public ReaderRequest updatePenalty(UUID requestId, Double penaltyFee) {
        ReaderRequest readerRequest = readerRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Not found requestId"));

        if (readerRequest.getStatus().equals(BorrowStatus.OVERDUE)) {
            readerRequest.setPenaltyFee(penaltyFee);
            return readerRequestRepository.save(readerRequest);
        } else {
            throw new IllegalArgumentException("Not found request have status OVERDUE");
        }
    }

    public List<ReaderRequest> getBorrowHistory(UUID readerId) {
        return readerRequestRepository.findByReaderId(readerId);
    }
}