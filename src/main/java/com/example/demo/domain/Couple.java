package com.example.demo.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "couples")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Couple extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 50)
    private String partner1Name; // 첫 번째 사람 이름
    
    @Column(nullable = false, length = 50)
    private String partner2Name; // 두 번째 사람 이름
    
    @Column(nullable = false)
    private LocalDate anniversaryDate; // 기념일 (만난 날)
    
    @Column(length = 200)
    private String description; // 커플 소개
    
    @Column(length = 500)
    private String backgroundImageUrl; // 배경 이미지 URL
    
    @Builder
    public Couple(String partner1Name, String partner2Name, LocalDate anniversaryDate, 
                  String description, String backgroundImageUrl) {
        this.partner1Name = partner1Name;
        this.partner2Name = partner2Name;
        this.anniversaryDate = anniversaryDate;
        this.description = description;
        this.backgroundImageUrl = backgroundImageUrl;
    }
    
    public void update(String partner1Name, String partner2Name, LocalDate anniversaryDate, 
                       String description, String backgroundImageUrl) {
        this.partner1Name = partner1Name;
        this.partner2Name = partner2Name;
        this.anniversaryDate = anniversaryDate;
        this.description = description;
        this.backgroundImageUrl = backgroundImageUrl;
    }
    
    // 만난 지 며칠 계산
    public long getDaysTogether() {
        return java.time.temporal.ChronoUnit.DAYS.between(anniversaryDate, LocalDate.now());
    }
}
