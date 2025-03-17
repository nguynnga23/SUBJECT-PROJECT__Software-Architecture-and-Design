package com.librarymanagementsystem.repositories;

import com.librarymanagementsystem.entities.Template;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TemplateRepository extends JpaRepository<Template, UUID> {
    Optional<Template> findByType(String type);
}
