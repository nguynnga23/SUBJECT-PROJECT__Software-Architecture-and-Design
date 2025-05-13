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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/borrowing-service/borrow-requests")
public class ReaderRequestController {
    @Autowired
    private ReaderRequestService readerRequestService;
    @Autowired
    private ReaderRequestMapper mapper;
    @PostMapping
    public ResponseEntity<ReaderRequestDTO> createRequest(@RequestBody ReaderRequestDTO requestDTO) {
        // Chuyển DTO thành Entity
        ReaderRequest requestEntity = readerRequestService.createBorrowRequest(requestDTO);

        // Map từ Entity sang DTO
        ReaderRequestDTO responseDTO = mapper.mapToDTO(requestEntity);

        // Trả về DTO đã map
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping
    public ResponseEntity<List<ReaderRequestDTO>> getReaderRequests() {
        List<ReaderRequest> readerRequests = readerRequestService.getListBorrowRequest();
        List<ReaderRequestDTO> readerRequestDTOs = new ArrayList<>();
        for(ReaderRequest readerRequest : readerRequests) {
            ReaderRequestDTO readerRequestDTO = mapper.mapToDTO(readerRequest);
            readerRequestDTOs.add(readerRequestDTO);
        }
        return ResponseEntity.ok(readerRequestDTOs);
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
    @PutMapping("/{requestId}/canceled")
    public ResponseEntity<ReaderRequestDTO> updateStatus(@PathVariable UUID requestId) {
        // Chuyển DTO thành Entity
        ReaderRequest requestEntity = readerRequestService.cancelBorrowRequest(requestId);

        // Map từ Entity sang DTO
        ReaderRequestDTO responseDTO = mapper.mapToDTO(requestEntity);

        // Trả về DTO đã map
        return ResponseEntity.ok(responseDTO);
    }
    @PutMapping("/{requestId}/borrow")
    public ResponseEntity<ReaderRequestDTO> updateBorrowDate(@PathVariable UUID requestId) {
        // Chuyển DTO thành Entity
        ReaderRequest requestEntity = readerRequestService.updateBorrowDate(requestId);

        // Map từ Entity sang DTO
        ReaderRequestDTO responseDTO = mapper.mapToDTO(requestEntity);

        // Trả về DTO đã map
        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping("/{requestId}/return")
    public ResponseEntity<ReaderRequestDTO> updateReturnDate(@PathVariable UUID requestId) {
        // Chuyển DTO thành Entity
        ReaderRequest requestEntity = readerRequestService.updateReturnDate(requestId);

        // Map từ Entity sang DTO
        ReaderRequestDTO responseDTO = mapper.mapToDTO(requestEntity);

        // Trả về DTO đã map
        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping("/{requestId}/penalty")
    public ResponseEntity<ReaderRequestDTO> updatePenalty(@PathVariable UUID requestId, @RequestBody PenaltyDTO penaltyDTO) {
        // Chuyển DTO thành Entity
        ReaderRequest requestEntity = readerRequestService.updatePenalty(requestId, penaltyDTO.getPenaltyFee());

        // Map từ Entity sang DTO
        ReaderRequestDTO responseDTO = mapper.mapToDTO(requestEntity);

        // Trả về DTO đã map
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/users/borrow-history")
    public ResponseEntity<List<ReaderRequestDTO>> getBorrowHistory() {
        List<ReaderRequest> borrowHistory = readerRequestService.getBorrowHistory();
        List<ReaderRequestDTO> readerRequestDTOs = new ArrayList<>();
        for(ReaderRequest readerRequest : borrowHistory) {
            ReaderRequestDTO readerRequestDTO = mapper.mapToDTO(readerRequest);
            readerRequestDTOs.add(readerRequestDTO);
        }
        return ResponseEntity.ok(readerRequestDTOs);
    }

}