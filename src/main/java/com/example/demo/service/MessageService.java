package com.example.demo.service;

import com.example.demo.domain.Message;
import com.example.demo.dto.MessageRequest;
import com.example.demo.dto.MessageResponse;
import com.example.demo.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MessageService {
    
    private final MessageRepository messageRepository;
    
    public List<MessageResponse> getAllPublicMessages() {
        return messageRepository.findAllPublicMessages().stream()
                .map(MessageResponse::from)
                .collect(Collectors.toList());
    }
    
    public List<MessageResponse> getAllMessages() {
        return messageRepository.findAllActiveMessages().stream()
                .map(MessageResponse::from)
                .collect(Collectors.toList());
    }
    
    public MessageResponse getMessage(Long id) {
        Message message = messageRepository.findActiveById(id)
                .orElseThrow(() -> new RuntimeException("메시지를 찾을 수 없습니다."));
        return MessageResponse.from(message);
    }
    
    @Transactional
    public MessageResponse createMessage(MessageRequest request) {
        Message message = Message.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .author(request.getAuthor())
                .password(request.getPassword())
                .isPublic(request.getIsPublic())
                .build();
        
        Message saved = messageRepository.save(message);
        return MessageResponse.from(saved);
    }
    
    @Transactional
    public MessageResponse updateMessage(Long id, MessageRequest request, String password) {
        Message message = messageRepository.findActiveById(id)
                .orElseThrow(() -> new RuntimeException("메시지를 찾을 수 없습니다."));
        
        // 비밀번호 확인
        if (message.getPassword() != null && !message.getPassword().equals(password)) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }
        
        message.update(request.getTitle(), request.getContent());
        return MessageResponse.from(message);
    }
    
    @Transactional
    public void deleteMessage(Long id, String password) {
        Message message = messageRepository.findActiveById(id)
                .orElseThrow(() -> new RuntimeException("메시지를 찾을 수 없습니다."));
        
        // 비밀번호 확인
        if (message.getPassword() != null && !message.getPassword().equals(password)) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }
        
        message.delete();
    }
}
