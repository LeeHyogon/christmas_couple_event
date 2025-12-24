package com.example.demo.dto;

import com.example.demo.domain.Message;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponse {
    private Long id;
    private String title;
    private String content;
    private String author;
    private Boolean isPublic;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public static MessageResponse from(Message message) {
        return MessageResponse.builder()
                .id(message.getId())
                .title(message.getTitle())
                .content(message.getContent())
                .author(message.getAuthor())
                .isPublic(message.getIsPublic())
                .createdAt(message.getCreatedAt())
                .updatedAt(message.getUpdatedAt())
                .build();
    }
}
