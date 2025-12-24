package com.example.demo.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "photos")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Photo extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String originalFileName;
    
    @Column(nullable = false)
    private String storedFileName; // 서버에 저장된 파일명
    
    @Column(nullable = false)
    private String filePath; // 파일 경로
    
    @Column(nullable = false)
    private Long fileSize;
    
    @Column(nullable = false)
    private String contentType; // image/jpeg, image/png 등
    
    @Column(length = 200)
    private String description; // 사진 설명
    
    @Column(nullable = false)
    private Integer displayOrder = 0; // 표시 순서
    
    @Builder
    public Photo(String originalFileName, String storedFileName, String filePath, 
                 Long fileSize, String contentType, String description, Integer displayOrder) {
        this.originalFileName = originalFileName;
        this.storedFileName = storedFileName;
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.contentType = contentType;
        this.description = description;
        this.displayOrder = displayOrder != null ? displayOrder : 0;
    }
    
    public void updateDescription(String description) {
        this.description = description;
    }
    
    public void updateDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }
    
    public void delete() {
        this.deleted = true;
    }
}
