package com.example.inventoryservice.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@Data
public class ReaderRequestDetailDTO {
    private UUID bookCopyId;

    @JsonCreator
    public ReaderRequestDetailDTO(
        @JsonProperty("bookCopyId") UUID bookCopyId
    ) {
        this.bookCopyId = bookCopyId;
    }

    public ReaderRequestDetailDTO() {
    }

}
