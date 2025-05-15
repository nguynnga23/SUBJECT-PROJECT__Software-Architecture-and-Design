package vn.edu.iuh.fit.borrowingservice.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import vn.edu.iuh.fit.borrowingservice.config.FeignConfig;
import vn.edu.iuh.fit.borrowingservice.dto.UserDTO;

import java.util.UUID;

@FeignClient(name = "gateway-service", configuration = FeignConfig.class)
public interface UserServiceClient {
    @GetMapping("/api/v1/user-service/users/profile/{userId}")
    UserDTO getUserProfile(@PathVariable("userId") UUID userId);
}
