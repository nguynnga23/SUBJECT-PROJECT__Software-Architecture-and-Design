package vn.edu.iuh.fit.borrowingservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.iuh.fit.borrowingservice.dto.PenaltyDTO;
import vn.edu.iuh.fit.borrowingservice.dto.ReaderRequestDTO;
import vn.edu.iuh.fit.borrowingservice.dto.UpdateStatusDTO;
import vn.edu.iuh.fit.borrowingservice.entity.ReaderRequest;
import vn.edu.iuh.fit.borrowingservice.mapper.ReaderRequestMapper;
import vn.edu.iuh.fit.borrowingservice.service.ReaderRequestService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/borrowing-service/borrow-requests")
public class ReaderRequestController {
    @Autowired
    private ReaderRequestService readerRequestService;
    private ReaderRequestMapper mapper = new ReaderRequestMapper();
    @PostMapping
    public ResponseEntity<ReaderRequestDTO> createRequest(@RequestBody ReaderRequestDTO requestDTO) {
        // Chuyển DTO thành Entity
        ReaderRequest requestEntity = readerRequestService.createBorrowRequest(requestDTO);

        // Map từ Entity sang DTO
        ReaderRequestDTO responseDTO = mapper.mapToDTO(requestEntity);

        // Trả về DTO đã map
        return ResponseEntity.ok(responseDTO);
    }



    @PutMapping("/{requestId}/status")
    public ResponseEntity<ReaderRequestDTO> updateStatus(@PathVariable UUID requestId, @RequestBody UpdateStatusDTO statusDTO) {
        // Chuyển DTO thành Entity
        ReaderRequest requestEntity = readerRequestService.updateStatus(requestId, statusDTO);

        // Map từ Entity sang DTO
        ReaderRequestDTO responseDTO = mapper.mapToDTO(requestEntity);

        // Trả về DTO đã map
        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping("/{requestId}/borrow")
    public ResponseEntity<ReaderRequest> updateBorrowDate(@PathVariable UUID requestId) {
        return ResponseEntity.ok(readerRequestService.updateBorrowDate(requestId));
    }

    @PutMapping("/{requestId}/return")
    public ResponseEntity<ReaderRequest> updateReturnDate(@PathVariable UUID requestId) {
        return ResponseEntity.ok(readerRequestService.updateReturnDate(requestId));
    }

    @PutMapping("/{requestId}/penalty")
    public ResponseEntity<ReaderRequest> updatePenalty(@PathVariable UUID requestId, @RequestBody PenaltyDTO penaltyDTO) {
        return ResponseEntity.ok(readerRequestService.updatePenalty(requestId, penaltyDTO.getPenaltyFee()));
    }

    @GetMapping("/users/{userId}/borrow-history")
    public ResponseEntity<List<ReaderRequest>> getBorrowHistory(@PathVariable UUID userId) {
        return ResponseEntity.ok(readerRequestService.getBorrowHistory(userId));
    }
}