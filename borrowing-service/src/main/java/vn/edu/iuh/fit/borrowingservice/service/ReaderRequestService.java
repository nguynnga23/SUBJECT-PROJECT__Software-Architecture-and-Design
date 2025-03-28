package vn.edu.iuh.fit.borrowingservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.util.EnumUtils;
import vn.edu.iuh.fit.borrowingservice.DTO.ReaderRequestDTO;
import vn.edu.iuh.fit.borrowingservice.DTO.UpdateStatusDTO;
import vn.edu.iuh.fit.borrowingservice.entity.ReaderRequest;
import vn.edu.iuh.fit.borrowingservice.entity.ReaderRequestDetail;
import vn.edu.iuh.fit.borrowingservice.enums.BorrowStatus;
import vn.edu.iuh.fit.borrowingservice.repository.ReaderRequestRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.EnumSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ReaderRequestService {
    @Autowired
    private ReaderRequestRepository readerRequestRepository;

    public ReaderRequest createBorrowRequest(ReaderRequestDTO requestDTO) {
        if (requestDTO.getReaderId() == null || requestDTO.getBookCopyIds() == null || requestDTO.getBookCopyIds().isEmpty()) {
            throw new IllegalArgumentException("Reader ID and The list of books must not be left blank.");
        }
        if(requestDTO.getBorrowingPeriod()<=0 || requestDTO.getBorrowingPeriod() == null )
        {
            throw new IllegalArgumentException("The borrowing period must be greater than 0 and not left blank.");
        }

       try {
           ReaderRequest request = new ReaderRequest();
           request.setReaderId(requestDTO.getReaderId());
           request.setStatus(BorrowStatus.PENDING);
           request.setBorrowingPeriod(requestDTO.getBorrowingPeriod());
           request.setCreatedAt(LocalDateTime.now());
           request.setUpdatedAt(LocalDateTime.now());
//           Tiính ngày dự kiến trả return_date dựa vào period
            int period = requestDTO.getBorrowingPeriod();
            LocalDateTime return_date = LocalDateTime.now().plusDays(period);
            request.setReturnDate(return_date);

           // Tạo danh sách ReaderRequestDetail từ danh sách bookCopyIds
           List<ReaderRequestDetail> requestDetails = requestDTO.getBookCopyIds().stream().map(bookCopyId -> {
               ReaderRequestDetail detail = new ReaderRequestDetail();
               detail.setReaderRequest(request);
               detail.setBookCopyId(bookCopyId);
               return detail;
           }).collect(Collectors.toList());

           // Gán danh sách vào request
           request.setBorrowRequestDetails(requestDetails);
           return readerRequestRepository.save(request);
       } catch (RuntimeException e) {

           throw new RuntimeException("Lỗi khi tạo yêu cầu mượn sách: " + e.getMessage());
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
