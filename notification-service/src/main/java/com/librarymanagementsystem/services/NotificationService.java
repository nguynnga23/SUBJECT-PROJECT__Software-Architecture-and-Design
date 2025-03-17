package com.librarymanagementsystem.services;

import java.util.Map;
import java.util.UUID;

public interface NotificationService {
    void createNotification(UUID userId, String type, Map<String, Object> data);
}
