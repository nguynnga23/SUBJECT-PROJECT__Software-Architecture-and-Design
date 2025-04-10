package com.example.inventoryservice.controller;

import com.example.inventoryservice.dto.BookDTO;
import com.example.inventoryservice.entity.BookCopy;
import com.example.inventoryservice.entity.Inventory;
import com.example.inventoryservice.enums.Status;
import com.example.inventoryservice.service.BookCopyService;
import com.example.inventoryservice.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.awt.print.Book;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/inventory-service")
public class InventoryController {
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private BookCopyService bookCopyService;
    @Autowired
    private RestTemplate restTemplate;

    @PostMapping("/add")
    public ResponseEntity<?> addBookCopy(@RequestBody BookCopy request) {
//        try {
//            String url = "http://localhost:8080/api/v1/book-service/books/" + request.getBookId();
//            String token = "eyJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJ1c2VyLXNlcnZpY2UiLCJzdWIiOiJodXluZ3V5ZW4iLCJyb2xlIjoiQURNSU4iLCJpZCI6IjBkY2UzNTczLTg4NzktNDMwMS05OTc4LTM1ZjIxZGI2NzVmYyIsImV4cCI6MTc0NDA0ODAzMiwidHlwZSI6ImFjY2VzcyJ9.ZiV9nX0CAXAbmfgRKJBMOUPXJre9VL_2MRaFNgtKEOPMf3Vqm_nlqWOEP4BZAj4WJy2wvrydxZwcpjiHSsFN28_3mRi0pT6NwbsMQEZxoaCvQeqSIsFdQfRVGbgfoPVsOeo9QRZSlRCcdrpkl7fmxLCMjAob9EaRBQQW3fDY24DOM5ydXDo-gBgjTsC_kIfqAVHL1wCu87tMM4A_BzUuZJmWvNq1g9-S0FqmTX3ZV4l2UXAk56iX5c4GjJDWn4rY9oaXbOJLEG6huHB87rPfVJsF1jK31sW_U2j2KzSKMIzBeEFp5xvmlqTSmGOfb5Wh5-pKSL552gtjDfkQQ8UMNg";
//            HttpHeaders headers = new HttpHeaders();
//            headers.setBearerAuth(token);
//
//            HttpEntity<Void> entity = new HttpEntity<>(headers);
//
//            ResponseEntity<BookDTO> response = restTemplate.exchange(
//                    url,
//                    HttpMethod.GET,
//                    entity,
//                    BookDTO.class
//            );
//
//            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                BookCopy bookCopy = request;
                String latestCode = bookCopyService.findLatestCopyCode(); // Ví dụ: BC_015
                int nextNumber = 1;
                if (latestCode != null && latestCode.startsWith("BC_")) {
                    nextNumber = Integer.parseInt(latestCode.substring(3)) + 1;
                }
                String formattedCode = String.format("BC_%03d", nextNumber);
                bookCopy.setCopyCode(formattedCode);

                Inventory inventory = inventoryService.getInventoryByBookId(bookCopy.getBookId());

                if(inventory != null){
                    Status status = bookCopy.getStatus();
                    switch (status) {
                        case BORROWED:
                            inventory.setBorrowed(inventory.getBorrowed() + 1);
                            break;
                        case AVAILABLE: // Tra sach
                            inventory.setAvailable(inventory.getAvailable() + 1);
                            break;
                        case LOST:
                            inventory.setLost(inventory.getLost() + 1);
                            break;
                        case DAMAGED:
                            inventory.setDamaged(inventory.getDamaged() + 1);
                            break;
                    }
                    inventory.setTotalQuantity(inventory.getTotalQuantity() + 1);
                    bookCopy.setInventory(inventoryService.saveInventory(inventory));
                }else {
                    Inventory newInventory = new Inventory();
                    newInventory.setAvailable(1);
                    newInventory.setTotalQuantity(1);
                    newInventory.setLost(0);
                    newInventory.setDamaged(0);
                    newInventory.setBorrowed(0);
                    newInventory.setBookId(bookCopy.getBookId());
                    bookCopy.setInventory(inventoryService.saveInventory(newInventory));
                }

                bookCopyService.addBookCopy(bookCopy);
                return ResponseEntity.ok(bookCopy);
//            } else {
//                return ResponseEntity.status(400).body(Map.of("error","Book does not exist"));
//            }
//        } catch (Exception e) {
//            return ResponseEntity.status(500).body(Map.of("error","Cannot check book's information"));
//        }
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<?> getBookCopy(@PathVariable UUID bookId) {
        Inventory inventory = inventoryService.getInventoryByBookId(bookId);
        if(inventory != null){
            return ResponseEntity.ok(inventory);
        }
        return ResponseEntity.status(400).body(Map.of("error","Book does not exist"));
    }

    @DeleteMapping("/remove/{bookId}")
    public ResponseEntity<?> deleteBook(@PathVariable UUID bookId){
        boolean isDeleted = inventoryService.deleteInventory(bookId);

        if(isDeleted){
            return ResponseEntity.ok(Map.of("message", "Inventory deleted successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "BookId not found"));
        }
    }

    @PutMapping("/update/{bookId}")
    public ResponseEntity<?> updateInventory(@PathVariable UUID bookId, @RequestBody Inventory newInventory){
        Inventory inventory = inventoryService.getInventoryByBookId(bookId);
        if(inventory != null){
            inventoryService.updateInventory(bookId, newInventory);
            return ResponseEntity.ok(inventory);
        }
        return ResponseEntity.ok(Map.of("error", "Inventory not found for bookId"));
    }
    @PutMapping("/update-action/{action}/{bookId}")
    public ResponseEntity<?> updateActionInventory(@PathVariable Status action, @PathVariable UUID bookId){
        Inventory inventory = inventoryService.getInventoryByBookId(bookId);
        if(inventory != null){
            inventoryService.updateActionInventory(bookId, action);
            return ResponseEntity.ok(inventory);
        }
        return ResponseEntity.ok(Map.of("error", "Inventory not found for bookId"));
    }

}
