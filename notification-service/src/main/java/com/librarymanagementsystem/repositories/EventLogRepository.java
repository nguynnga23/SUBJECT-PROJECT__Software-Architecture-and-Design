package com.librarymanagementsystem.repositories;

import com.librarymanagementsystem.entities.EventLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EventLogRepository extends JpaRepository<EventLog, UUID> {
}

