package com.example.demo.dto;

import com.example.demo.domain.Memory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemoryResponse {
    private Long id;
    private String title;
    private String content;
    private LocalDate memoryDate;
    private String location;
    private Integer displayOrder;
    private LocalDateTime createdAt;
    
    public static MemoryResponse from(Memory memory) {
        return MemoryResponse.builder()
                .id(memory.getId())
                .title(memory.getTitle())
                .content(memory.getContent())
                .memoryDate(memory.getMemoryDate())
                .location(memory.getLocation())
                .displayOrder(memory.getDisplayOrder())
                .createdAt(memory.getCreatedAt())
                .build();
    }
}
