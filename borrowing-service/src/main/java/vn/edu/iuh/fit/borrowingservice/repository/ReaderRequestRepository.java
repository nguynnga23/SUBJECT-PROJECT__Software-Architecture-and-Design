package vn.edu.iuh.fit.borrowingservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.iuh.fit.borrowingservice.entity.ReaderRequest;

import java.util.List;
import java.util.UUID;

public interface ReaderRequestRepository extends JpaRepository<ReaderRequest, UUID> {
    List<ReaderRequest> findByReaderId(UUID readerId);
}