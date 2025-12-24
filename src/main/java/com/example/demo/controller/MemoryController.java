package com.example.demo.controller;

import com.example.demo.dto.MemoryRequest;
import com.example.demo.dto.MemoryResponse;
import com.example.demo.service.MemoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/memories")
@RequiredArgsConstructor
public class MemoryController {
    
    private final MemoryService memoryService;
    
    @GetMapping
    public ResponseEntity<List<MemoryResponse>> getAllMemories() {
        return ResponseEntity.ok(memoryService.getAllMemories());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<MemoryResponse> getMemory(@PathVariable Long id) {
        return ResponseEntity.ok(memoryService.getMemory(id));
    }
    
    @PostMapping
    public ResponseEntity<MemoryResponse> createMemory(@RequestBody MemoryRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(memoryService.createMemory(request));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<MemoryResponse> updateMemory(
            @PathVariable Long id,
            @RequestBody MemoryRequest request) {
        return ResponseEntity.ok(memoryService.updateMemory(id, request));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMemory(@PathVariable Long id) {
        memoryService.deleteMemory(id);
        return ResponseEntity.noContent().build();
    }
}
