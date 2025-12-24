package com.example.demo.repository;

import com.example.demo.domain.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {
    
    @Query("SELECT p FROM Photo p WHERE p.deleted = false ORDER BY p.displayOrder ASC, p.createdAt DESC")
    List<Photo> findAllActivePhotos();
    
    @Query("SELECT p FROM Photo p WHERE p.id = :id AND p.deleted = false")
    Optional<Photo> findActiveById(Long id);
}
