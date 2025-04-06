package vn.edu.iuh.fit.borrowingservice.clients;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "book-service")
public interface BookServiceClient {

}
