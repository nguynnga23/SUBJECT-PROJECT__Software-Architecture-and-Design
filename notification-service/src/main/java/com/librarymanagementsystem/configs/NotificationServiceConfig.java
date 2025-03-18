package com.librarymanagementsystem.configs;

import com.librarymanagementsystem.observers.impl.DatabaseNotificationObserver;
import com.librarymanagementsystem.observers.impl.EmailNotificationObserver;
import com.librarymanagementsystem.observers.impl.LoggingNotificationObserver;
import com.librarymanagementsystem.services.NotificationService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NotificationServiceConfig {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private DatabaseNotificationObserver databaseObserver;

    @Autowired
    private EmailNotificationObserver emailObserver;

    @Autowired
    private LoggingNotificationObserver loggingObserver;

    @PostConstruct
    public void init() {
        // Register observers with the subject (NotificationService)
        notificationService.attach(databaseObserver);
        notificationService.attach(emailObserver);
        notificationService.attach(loggingObserver);
    }
}