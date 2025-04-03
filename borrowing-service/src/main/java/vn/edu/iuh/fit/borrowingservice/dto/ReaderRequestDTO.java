package vn.edu.iuh.fit.borrowingservice.DTO;

import lombok.Data;

import java.util.List;
import java.util.UUID;
@Data
public class ReaderRequestDTO {
    private UUID readerId;
    private Integer borrowingPeriod;
    private List<UUID> bookCopyIds;
}
