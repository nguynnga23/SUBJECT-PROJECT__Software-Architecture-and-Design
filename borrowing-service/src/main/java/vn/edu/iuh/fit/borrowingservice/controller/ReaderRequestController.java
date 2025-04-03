package vn.edu.iuh.fit.borrowingservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.iuh.fit.borrowingservice.dto.PenaltyDTO;
import vn.edu.iuh.fit.borrowingservice.dto.ReaderRequestDTO;
import vn.edu.iuh.fit.borrowingservice.dto.UpdateStatusDTO;
import vn.edu.iuh.fit.borrowingservice.entity.ReaderRequest;
import vn.edu.iuh.fit.borrowingservice.service.ReaderRequestService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/borrow-requests")
public class ReaderRequestController {
    @Autowired
    private ReaderRequestService readerRequestService;

    @PostMapping
    public ResponseEntity<ReaderRequest> createRequest(@RequestBody ReaderRequestDTO requestDTO) {
        return ResponseEntity.ok(readerRequestService.createBorrowRequest(requestDTO));
    }

    @PutMapping("/{requestId}/status")
    public ResponseEntity<ReaderRequest> updateStatus(@PathVariable UUID requestId, @RequestBody UpdateStatusDTO statusDTO) {
        return ResponseEntity.ok(readerRequestService.updateStatus(requestId, statusDTO));
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