package vn.edu.iuh.fit.borrowingservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "reader_request_details")
@Getter
@Setter
@NoArgsConstructor
public class ReaderRequestDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "reader_request_id", nullable = false)
    private ReaderRequest readerRequest;

    @Column(name = "book_copy_id", nullable = false)
    private UUID bookCopyId;
}