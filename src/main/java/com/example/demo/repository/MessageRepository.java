package com.example.demo.repository;

import com.example.demo.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    
    @Query("SELECT m FROM Message m WHERE m.deleted = false AND m.isPublic = true ORDER BY m.createdAt DESC")
    List<Message> findAllPublicMessages();
    
    @Query("SELECT m FROM Message m WHERE m.deleted = false ORDER BY m.createdAt DESC")
    List<Message> findAllActiveMessages();
    
    @Query("SELECT m FROM Message m WHERE m.id = :id AND m.deleted = false")
    Optional<Message> findActiveById(Long id);
}
