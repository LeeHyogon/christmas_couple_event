package com.example.demo.repository;

import com.example.demo.domain.Memory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemoryRepository extends JpaRepository<Memory, Long> {
    
    @Query("SELECT m FROM Memory m WHERE m.deleted = false ORDER BY m.memoryDate DESC, m.displayOrder ASC")
    List<Memory> findAllActiveMemories();
    
    @Query("SELECT m FROM Memory m WHERE m.id = :id AND m.deleted = false")
    Optional<Memory> findActiveById(Long id);
}
