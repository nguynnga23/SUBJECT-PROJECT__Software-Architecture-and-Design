package com.librarymanagementsystem.repositories;

import com.librarymanagementsystem.entities.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {
}
