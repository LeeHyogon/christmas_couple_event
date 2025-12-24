package com.example.demo.dto;

import com.example.demo.domain.Couple;
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
public class CoupleResponse {
    private Long id;
    private String partner1Name;
    private String partner2Name;
    private LocalDate anniversaryDate;
    private String description;
    private String backgroundImageUrl;
    private Long daysTogether;
    private LocalDateTime createdAt;
    
    public static CoupleResponse from(Couple couple) {
        return CoupleResponse.builder()
                .id(couple.getId())
                .partner1Name(couple.getPartner1Name())
                .partner2Name(couple.getPartner2Name())
                .anniversaryDate(couple.getAnniversaryDate())
                .description(couple.getDescription())
                .backgroundImageUrl(couple.getBackgroundImageUrl())
                .daysTogether(couple.getDaysTogether())
                .createdAt(couple.getCreatedAt())
                .build();
    }
}
