package vn.edu.iuh.fit.borrowingservice.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.ResponseEntity;
import vn.edu.iuh.fit.borrowingservice.config.FeignConfig;
import vn.edu.iuh.fit.borrowingservice.dto.BookCopyDTO;

import java.util.UUID;

@FeignClient(name = "inventory-service", configuration = FeignConfig.class)
public interface InventoryServiceClient {
    @GetMapping("/api/v1/inventory-service/inventories/check-available/{bookId}")
    ResponseEntity<?> checkCopyAvailable(@PathVariable("bookId") UUID bookId);

    @GetMapping("/api/v1/inventory-service/copies/available-copy/{bookId}")
    ResponseEntity<BookCopyDTO> getAvailableCopy(@PathVariable UUID bookId);

    @GetMapping("/api/v1/inventory-service/copies/{bookCopyId}")
    BookCopyDTO getBookCopyById(@PathVariable UUID bookCopyId);
}
