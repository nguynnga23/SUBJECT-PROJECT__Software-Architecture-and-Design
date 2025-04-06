package vn.edu.iuh.fit.borrowingservice.dto;

import lombok.Data;
import vn.edu.iuh.fit.borrowingservice.enums.BorrowStatus;

import java.util.UUID;

@Data
public class UpdateStatusDTO {
    private UUID librarianId;
    private BorrowStatus status;
}
