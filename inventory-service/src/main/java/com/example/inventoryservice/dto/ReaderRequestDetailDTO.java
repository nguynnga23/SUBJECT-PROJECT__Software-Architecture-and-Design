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
    private BookCopyDTO bookCopy;

    @JsonCreator
    public ReaderRequestDetailDTO(
        @JsonProperty("bookCopyId") BookCopyDTO bookCopy
    ) {
        this.bookCopy = bookCopy;
    }

    public ReaderRequestDetailDTO() {
    }

}
