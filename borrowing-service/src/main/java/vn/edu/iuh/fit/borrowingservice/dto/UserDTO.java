package vn.edu.iuh.fit.borrowingservice.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class UserDTO {
    private UUID userId;
    private String email;
    private String fullName;
    private String role;
}
