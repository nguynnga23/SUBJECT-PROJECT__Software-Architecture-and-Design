package vn.edu.iuh.fit.borrowingservice.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
            ReaderRequest request = new ReaderRequest();
            request.setReaderId(UUID.fromString(readerId));
            request.setStatus(BorrowStatus.PENDING);
            request.setBorrowingPeriod(requestDTO.getBorrowingPeriod());
            request.setCreatedAt(LocalDateTime.now());
            request.setUpdatedAt(LocalDateTime.now());

            // Tính toán ngày trả sách
            LocalDateTime returnDate = LocalDateTime.now().plusDays(requestDTO.getBorrowingPeriod());
            request.setReturnDate(returnDate);

            // Map borrowRequestDetails từ DTO sang Entity
            List<ReaderRequestDetail> requestDetails = requestDTO.getBorrowRequestDetails().stream()
                    .map(detailDTO -> {
                        ReaderRequestDetail detail = new ReaderRequestDetail();
                        detail.setReaderRequest(request);
                        detail.setBookCopyId(detailDTO.getBookCopyId());  // Assumed bookId as BookCopyId in your DTO
                        return detail;
                    }).collect(Collectors.toList());

            request.setBorrowRequestDetails(requestDetails);

            // Lưu yêu cầu mượn vào cơ sở dữ liệu
            return readerRequestRepository.save(request);

        } catch (RuntimeException e) {
            throw new RuntimeException("Error creating borrow request: " + e.getMessage());
        }
    }

    public ReaderRequest updateStatus(UUID requestId, UpdateStatusDTO statusDTO) {
        if(!readerRequestRepository.findById(requestId).isPresent()){
            throw  new IllegalArgumentException("Not found requestId");

        }
        if (!EnumSet.allOf(BorrowStatus.class).contains(statusDTO.getStatus())) {
            throw new IllegalArgumentException("Invalid status: " + statusDTO.getStatus());
        }
        ReaderRequest request = readerRequestRepository.findById(requestId).orElseThrow();
        request.setStatus(statusDTO.getStatus());
        request.setLibrarianId(statusDTO.getLibrarianId());
        request.setUpdatedAt(LocalDateTime.now());
        return readerRequestRepository.save(request);
    }

    public ReaderRequest updateBorrowDate(UUID requestId) {
        if(!readerRequestRepository.findById(requestId).isPresent()){
            throw  new IllegalArgumentException("Not found requestId");

        }
        ReaderRequest request = readerRequestRepository.findById(requestId).orElseThrow();
        request.setDateBorrowed(LocalDateTime.now());
        request.setStatus(BorrowStatus.BORROWED);
        request.setUpdatedAt(LocalDateTime.now());
        return readerRequestRepository.save(request);
    }

    public ReaderRequest updateReturnDate(UUID requestId) {
        if(!readerRequestRepository.findById(requestId).isPresent()){
            throw  new IllegalArgumentException("Not found requestId");

        }
        ReaderRequest request = readerRequestRepository.findById(requestId).orElseThrow();
        if(request.getReturnDate().isBefore(LocalDateTime.now())){
//            throw new IllegalArgumentException("Overdue payment date, pay the fee");
            request.setDateReturned(LocalDateTime.now());
            request.setStatus(BorrowStatus.OVERDUE);
            request.setUpdatedAt(LocalDateTime.now());
            return readerRequestRepository.save(request);
        }
        request.setDateReturned(LocalDateTime.now());
        request.setStatus(BorrowStatus.RETURNED);
        request.setUpdatedAt(LocalDateTime.now());
        return readerRequestRepository.save(request);
    }

    public ReaderRequest updatePenalty(UUID requestId, Double penaltyFee) {
        if(!readerRequestRepository.findById(requestId).isPresent()){
            throw  new IllegalArgumentException("Not found requestId");

        }

        ReaderRequest request = readerRequestRepository.findById(requestId).orElseThrow();
        if(request.getStatus().equals(BorrowStatus.OVERDUE))
        {
//           count days return late
//            long overdueDays = ChronoUnit.DAYS.between(request.getReturnDate(), LocalDateTime.now());
//            penaltyFee = overdueDays*50000.0;
            request.setPenaltyFee(penaltyFee);
            return readerRequestRepository.save(request);
        }
        else{
            throw new IllegalArgumentException("Not found request have status OVERDUE");
        }

    }

    public List<ReaderRequest> getBorrowHistory(UUID readerId) {
        return readerRequestRepository.findByReaderId(readerId);
    }
}
