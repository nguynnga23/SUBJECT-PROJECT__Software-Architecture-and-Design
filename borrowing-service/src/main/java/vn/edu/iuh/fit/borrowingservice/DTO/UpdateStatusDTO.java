package vn.edu.iuh.fit.borrowingservice.DTO;

import lombok.Data;
import vn.edu.iuh.fit.borrowingservice.enums.BorrowStatus;
@Data
public class UpdateStatusDTO {
    private BorrowStatus status;
}
