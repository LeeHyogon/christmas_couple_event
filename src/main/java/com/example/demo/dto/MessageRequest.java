package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MessageRequest {
    private String title;
    private String content;
    private String author;
    private String password;
    private Boolean isPublic;
}
