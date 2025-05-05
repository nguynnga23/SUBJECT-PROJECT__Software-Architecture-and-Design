package com.recommendationservice.controller;

//import com.recommendationservice.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@RestController
// Call TensorFlow model for recommendations
@RequestMapping("/api/v1/recommendation-service/")
public class RecommendationController {
//    @Autowired
//    private RecommendationService recommendationService;

//    @GetMapping("/user/{userId}")
//    public List<Long> getRecommendations(@PathVariable Long userId) {
//        return recommendationService.getRecommendations(userId);
//    }
}