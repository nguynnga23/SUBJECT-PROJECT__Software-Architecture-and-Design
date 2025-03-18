package com.librarymanagementsystem.observers;

import java.util.UUID;

public interface NotificationObserver {
    void update(UUID userId, String message, String type);
}
