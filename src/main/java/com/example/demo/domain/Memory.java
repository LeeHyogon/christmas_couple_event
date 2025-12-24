package com.example.demo.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "memories")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Memory extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String content;
    
    @Column(nullable = false)
    private LocalDate memoryDate; // 추억 날짜
    
    @Column(length = 100)
    private String location; // 장소
    
    @Column(nullable = false)
    private Integer displayOrder = 0;
    
    @Builder
    public Memory(String title, String content, LocalDate memoryDate, 
                  String location, Integer displayOrder) {
        this.title = title;
        this.content = content;
        this.memoryDate = memoryDate;
        this.location = location;
        this.displayOrder = displayOrder != null ? displayOrder : 0;
    }
    
    public void update(String title, String content, LocalDate memoryDate, String location) {
        this.title = title;
        this.content = content;
        this.memoryDate = memoryDate;
        this.location = location;
    }
    
    public void updateDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }
    
    public void delete() {
        this.deleted = true;
    }
}
