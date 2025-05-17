package vn.edu.iuh.fit.borrowingservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import vn.edu.iuh.fit.borrowingservice.annotation.RequireAdmin;
import vn.edu.iuh.fit.borrowingservice.clients.InventoryServiceClient;
import vn.edu.iuh.fit.borrowingservice.clients.UserServiceClient;
import vn.edu.iuh.fit.borrowingservice.dto.*;
import vn.edu.iuh.fit.borrowingservice.entity.ReaderRequest;
import vn.edu.iuh.fit.borrowingservice.entity.ReaderRequestDetail;
import vn.edu.iuh.fit.borrowingservice.enums.BorrowStatus;
import vn.edu.iuh.fit.borrowingservice.kafka.KafkaBorrowingProducer;
import vn.edu.iuh.fit.borrowingservice.mapper.ReaderRequestMapper;
import vn.edu.iuh.fit.borrowingservice.repository.ReaderRequestRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class ReaderRequestService {
    @Autowired
    private ReaderRequestRepository readerRequestRepository;
    @Autowired
    private ReaderRequestMapper mapper;
    @Autowired
    private UserServiceClient userServiceClient;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private KafkaBorrowingProducer producer;

    @Autowired
    private InventoryServiceClient inventoryServiceClient;

    public ReaderRequest createBorrowRequest(ReaderRequestDTO requestDTO) {
        String readerId = request.getHeader("X-User-Id");
        if (readerId == null || readerId.isEmpty()) {
            throw new IllegalArgumentException("Missing Reader ID in request header.");
        }

        if (requestDTO.getBorrowRequestDetails() == null || requestDTO.getBorrowRequestDetails().isEmpty()) {
            throw new IllegalArgumentException("The list of books must not be left blank.");
        }

        if (requestDTO.getBorrowingPeriod() == null || requestDTO.getBorrowingPeriod() <= 0) {
            throw new IllegalArgumentException("The borrowing period must be greater than 0 and not left blank.");
        }

        try {
            ReaderRequest readerRequest = new ReaderRequest();
            readerRequest.setReaderId(UUID.fromString(readerId));
            readerRequest.setStatus(BorrowStatus.PENDING);
            readerRequest.setBorrowingPeriod(requestDTO.getBorrowingPeriod());
            readerRequest.setCreatedAt(LocalDateTime.now());
            readerRequest.setUpdatedAt(LocalDateTime.now());
            readerRequest.setReturnDate(LocalDateTime.now().plusDays(requestDTO.getBorrowingPeriod()));

            List<ReaderRequestDetail> requestDetails = new ArrayList<>();

            for (BorrowRequestDetailDTO detailDTO : requestDTO.getBorrowRequestDetails()) {
                UUID bookId = detailDTO.getBookId();

                // Bước 1: Kiểm tra xem sách có bản sao khả dụng hay không
                boolean isAvailable = inventoryServiceClient.checkCopyAvailable(bookId).hasBody();
                if (!isAvailable) {
                    throw new IllegalArgumentException("No available copies for book with ID " + bookId);
                }

                // Bước 2: Nếu có bản sao khả dụng, lấy bản sao đầu tiên
                ResponseEntity<BookCopyDTO> response = (ResponseEntity<BookCopyDTO>) inventoryServiceClient.getAvailableCopy(bookId);
                if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
                    throw new IllegalArgumentException("Unable to fetch available copy for book with ID " + bookId);
                }

                // Lấy BookCopyDTO từ response body
                BookCopyDTO bookCopyDTO = response.getBody();
                System.out.println(bookCopyDTO.getId());

                // Tạo chi tiết yêu cầu mượn với bookCopyId và thêm vào danh sách
                ReaderRequestDetail detail = new ReaderRequestDetail();
                detail.setReaderRequest(readerRequest);
                detail.setBookCopyId(bookCopyDTO.getId()); // Sử dụng id của bookCopy
                requestDetails.add(detail);
            }

            readerRequest.setReaderRequestDetails(requestDetails);

            ReaderRequest savedRequest = readerRequestRepository.save(readerRequest);

            // Gửi sự kiện đến Notification Service
            UserDTO reader = userServiceClient.getUserProfile(UUID.fromString(readerId));
            String notificationMessage = "Borrow request created successfully for email: " + reader.getEmail();
            producer.sendToNotificationService(notificationMessage);

            // Gửi sự kiện đến Inventory Service
            // Sau khi lưu borrow request
            ReaderRequestDTO dto = mapper.mapToDTO(readerRequest);
            String inventoryMessage = objectMapper.writeValueAsString(dto);
            producer.sendToInventoryService(inventoryMessage);
            return savedRequest;

        } catch (RuntimeException e) {
            throw new RuntimeException("Error creating borrow request: " + e.getMessage());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
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

    public List<ReaderRequest> getListBorrowRequest() {
        return readerRequestRepository.findAll();
    }
    public List<ReaderRequest> getBorrowHistory() {
        String readerId = request.getHeader("X-User-Id");
        if (readerId == null || readerId.isEmpty()) {
            throw new IllegalArgumentException("Missing Reader ID in request header.");
        }
        return readerRequestRepository.findByReaderId(UUID.fromString(readerId));
    }

    public ReaderRequest cancelBorrowRequest(UUID requestId) {
        ReaderRequest readerRequest = readerRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Not found requestId"));

        if (!readerRequest.getStatus().equals(BorrowStatus.PENDING)) {
            throw new IllegalArgumentException("Only pending requests can be canceled.");
        }

        readerRequest.setStatus(BorrowStatus.CANCELED);
        readerRequest.setUpdatedAt(LocalDateTime.now());
        readerRequestRepository.save(readerRequest);

        // Optionally, send a notification or event for the cancellation
        // String notificationMessage = "Borrow request with ID " + requestId + " has been canceled.";
        // producer.sendToNotificationService(notificationMessage);
        return readerRequest;
    }

    public BorrowingStatisticsDTO getBorrowingStatistics() {
        BorrowingStatisticsDTO statisticsDTO = new BorrowingStatisticsDTO();
        statisticsDTO.setTotalRequests(readerRequestRepository.countAllRequests());
        statisticsDTO.setCanceledRequests(readerRequestRepository.countCanceledRequests());
        statisticsDTO.setPendingRequests(readerRequestRepository.countPendingRequests());
        statisticsDTO.setApprovedRequests(readerRequestRepository.countApprovedRequests());
        statisticsDTO.setBorrowedRequests(readerRequestRepository.countBorrowedRequests());
        statisticsDTO.setReturnedRequests(readerRequestRepository.countReturnedRequests());
        statisticsDTO.setOverdueRequests(readerRequestRepository.countOverdueRequests());
        return statisticsDTO;
    }

    public Long countRequestsByDate(LocalDate date) {
        return readerRequestRepository.countRequestsByDate(date);
    }

    public Long countBookByDate(LocalDate date) {
        return readerRequestRepository.countBooksBorrowedByDate(date);
    }
}