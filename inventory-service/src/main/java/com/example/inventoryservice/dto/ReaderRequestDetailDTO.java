package com.example.inventoryservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReaderRequestDetailDTO {
    private BookCopyDTO bookCopy;

    // Không cần @JsonCreator nếu bạn muốn ánh xạ tự động toàn bộ đối tượng
    public ReaderRequestDetailDTO() {
    }
}
