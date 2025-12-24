package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemoryRequest {
    private String title;
    private String content;
    private LocalDate memoryDate;
    private String location;
    private Integer displayOrder;
}
