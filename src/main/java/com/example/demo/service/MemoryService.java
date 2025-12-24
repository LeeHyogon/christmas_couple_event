package com.example.demo.service;

import com.example.demo.domain.Memory;
import com.example.demo.dto.MemoryRequest;
import com.example.demo.dto.MemoryResponse;
import com.example.demo.repository.MemoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemoryService {
    
    private final MemoryRepository memoryRepository;
    
    public List<MemoryResponse> getAllMemories() {
        return memoryRepository.findAllActiveMemories().stream()
                .map(MemoryResponse::from)
                .collect(Collectors.toList());
    }
    
    public MemoryResponse getMemory(Long id) {
        Memory memory = memoryRepository.findActiveById(id)
                .orElseThrow(() -> new RuntimeException("추억을 찾을 수 없습니다."));
        return MemoryResponse.from(memory);
    }
    
    @Transactional
    public MemoryResponse createMemory(MemoryRequest request) {
        Memory memory = Memory.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .memoryDate(request.getMemoryDate())
                .location(request.getLocation())
                .displayOrder(request.getDisplayOrder())
                .build();
        
        Memory saved = memoryRepository.save(memory);
        return MemoryResponse.from(saved);
    }
    
    @Transactional
    public MemoryResponse updateMemory(Long id, MemoryRequest request) {
        Memory memory = memoryRepository.findActiveById(id)
                .orElseThrow(() -> new RuntimeException("추억을 찾을 수 없습니다."));
        
        memory.update(request.getTitle(), request.getContent(), 
                     request.getMemoryDate(), request.getLocation());
        return MemoryResponse.from(memory);
    }
    
    @Transactional
    public void deleteMemory(Long id) {
        Memory memory = memoryRepository.findActiveById(id)
                .orElseThrow(() -> new RuntimeException("추억을 찾을 수 없습니다."));
        memory.delete();
    }
}
