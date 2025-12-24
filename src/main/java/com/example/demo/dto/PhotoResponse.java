package com.example.demo.dto;

import com.example.demo.domain.Photo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PhotoResponse {
    private Long id;
    private String originalFileName;
    private String filePath;
    private String contentType;
    private String description;
    private Integer displayOrder;
    private LocalDateTime createdAt;
    
    public static PhotoResponse from(Photo photo) {
        return PhotoResponse.builder()
                .id(photo.getId())
                .originalFileName(photo.getOriginalFileName())
                .filePath(photo.getFilePath())
                .contentType(photo.getContentType())
                .description(photo.getDescription())
                .displayOrder(photo.getDisplayOrder())
                .createdAt(photo.getCreatedAt())
                .build();
    }
}
