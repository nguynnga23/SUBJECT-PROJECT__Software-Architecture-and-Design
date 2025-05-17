package com.notificationservice.service;

import com.notificationservice.entity.Notification;

public interface NotificationService {
    Notification createNotification(Notification notification);
    Notification updateNotification(Notification notification);
}
