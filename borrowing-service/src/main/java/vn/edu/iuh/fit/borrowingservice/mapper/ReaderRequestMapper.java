package vn.edu.iuh.fit.borrowingservice.mapper;

import vn.edu.iuh.fit.borrowingservice.dto.ReaderRequestDTO;
import vn.edu.iuh.fit.borrowingservice.dto.ReaderRequestDetailDTO;
import vn.edu.iuh.fit.borrowingservice.entity.ReaderRequest;

import java.util.List;
import java.util.stream.Collectors;

public class ReaderRequestMapper {
    public ReaderRequestDTO mapToDTO(ReaderRequest entity) {
        ReaderRequestDTO dto = new ReaderRequestDTO();
        dto.setId(entity.getId());
        dto.setReaderId(entity.getReaderId());
        dto.setLibrarianId(entity.getLibrarianId());
        dto.setStatus(entity.getStatus());
        dto.setDateBorrowed(entity.getDateBorrowed());
        dto.setDateReturned(entity.getDateReturned());
        dto.setReturnDate(entity.getReturnDate());
        dto.setPenaltyFee(entity.getPenaltyFee());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        dto.setBorrowingPeriod(entity.getBorrowingPeriod());
        // ❌ đừng map borrowRequestDetails nếu không cần
        // Map borrowRequestDetails nếu cần
        if (entity.getBorrowRequestDetails() != null) {
            List<ReaderRequestDetailDTO> detailDTOs = entity.getBorrowRequestDetails().stream()
                    .map(detail -> {
                        ReaderRequestDetailDTO detailDTO = new ReaderRequestDetailDTO();
                        detailDTO.setBookCopyId(detail.getBookCopyId());
                        return detailDTO;
                    })
                    .collect(Collectors.toList());
            dto.setBorrowRequestDetails(detailDTOs);
        }
        return dto;
    }
}
