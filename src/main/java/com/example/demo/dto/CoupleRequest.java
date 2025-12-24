package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CoupleRequest {
    private String partner1Name;
    private String partner2Name;
    private LocalDate anniversaryDate;
    private String description;
    private String backgroundImageUrl;
}
