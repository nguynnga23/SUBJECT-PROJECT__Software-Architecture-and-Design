package com.example.inventoryservice.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@Data
public class ReaderRequestDTO {
    private UUID id;
    private UUID readerId;
    private UUID librarianId;
    private String status;
    private LocalDateTime dateBorrowed;
    private LocalDateTime dateReturned;
    private LocalDateTime returnDate;
    private Double penaltyFee;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int borrowingPeriod;
    private List<ReaderRequestDetailDTO> readerRequestDetails;

    @JsonCreator
    public ReaderRequestDTO(
        @JsonProperty("id") UUID id,
        @JsonProperty("readerId") UUID readerId,
        @JsonProperty("librarianId") UUID librarianId,
        @JsonProperty("status") String status,
        @JsonProperty("dateBorrowed") LocalDateTime dateBorrowed,
        @JsonProperty("dateReturned") LocalDateTime dateReturned,
        @JsonProperty("returnDate") LocalDateTime returnDate,
        @JsonProperty("penaltyFee") Double penaltyFee,
        @JsonProperty("createdAt") LocalDateTime createdAt,
        @JsonProperty("updatedAt") LocalDateTime updatedAt,
        @JsonProperty("borrowingPeriod") int borrowingPeriod,
        @JsonProperty("readerRequestDetails") List<ReaderRequestDetailDTO> readerRequestDetails
    ) {
        this.id = id;
        this.readerId = readerId;
        this.librarianId = librarianId;
        this.status = status;
        this.dateBorrowed = dateBorrowed;
        this.dateReturned = dateReturned;
        this.returnDate = returnDate;
        this.penaltyFee = penaltyFee;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.borrowingPeriod = borrowingPeriod;
        this.readerRequestDetails = readerRequestDetails;
    }

    public ReaderRequestDTO() {
    }
}


