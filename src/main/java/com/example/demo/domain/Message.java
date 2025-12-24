package com.example.demo.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "messages")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Message extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String title;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;
    
    @Column(nullable = false, length = 50)
    private String author; // 작성자 이름
    
    @Column(length = 20)
    private String password; // 비밀번호 (선택적, 수정/삭제용)
    
    @Column(nullable = false)
    private Boolean isPublic = true; // 공개 여부
    
    @Builder
    public Message(String title, String content, String author, String password, Boolean isPublic) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.password = password;
        this.isPublic = isPublic != null ? isPublic : true;
    }
    
    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
    
    public void delete() {
        this.deleted = true;
    }
}
