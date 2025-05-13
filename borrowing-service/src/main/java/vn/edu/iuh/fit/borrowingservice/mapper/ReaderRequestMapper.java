package vn.edu.iuh.fit.borrowingservice.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vn.edu.iuh.fit.borrowingservice.clients.InventoryServiceClient;
import vn.edu.iuh.fit.borrowingservice.dto.BookCopyDTO;
import vn.edu.iuh.fit.borrowingservice.dto.BorrowRequestDetailDTO;
import vn.edu.iuh.fit.borrowingservice.dto.ReaderRequestDTO;
import vn.edu.iuh.fit.borrowingservice.dto.ReaderRequestDetailDTO;
import vn.edu.iuh.fit.borrowingservice.entity.ReaderRequest;

import java.util.List;
import java.util.stream.Collectors;
@Component
public class ReaderRequestMapper {
    @Autowired
    private InventoryServiceClient inventoryServiceClient;

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
        // Map borrowRequestDetails nếu cần
        if (entity.getReaderRequestDetails() != null) {
            List<ReaderRequestDetailDTO> detailDTOs = entity.getReaderRequestDetails().stream()
                    .map(detail -> {
                        ReaderRequestDetailDTO detailDTO = new ReaderRequestDetailDTO();
                        detailDTO.setBookCopyId(inventoryServiceClient.getBookCopyById(detail.getBookCopyId()));
                        return detailDTO;
                    })
                    .collect(Collectors.toList());
            dto.setReaderRequestDetails(detailDTOs);
        }
        return dto;
    }
}
