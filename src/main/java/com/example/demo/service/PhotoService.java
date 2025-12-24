package com.example.demo.service;

import com.example.demo.domain.Photo;
import com.example.demo.dto.PhotoResponse;
import com.example.demo.repository.PhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PhotoService {
    
    private final PhotoRepository photoRepository;
    
    public List<PhotoResponse> getAllPhotos() {
        return photoRepository.findAllActivePhotos().stream()
                .map(PhotoResponse::from)
                .collect(Collectors.toList());
    }
    
    public PhotoResponse getPhoto(Long id) {
        Photo photo = photoRepository.findActiveById(id)
                .orElseThrow(() -> new RuntimeException("사진을 찾을 수 없습니다."));
        return PhotoResponse.from(photo);
    }
    
    @Transactional
    public PhotoResponse createPhoto(Photo photo) {
        Photo saved = photoRepository.save(photo);
        return PhotoResponse.from(saved);
    }
    
    @Transactional
    public void deletePhoto(Long id) {
        Photo photo = photoRepository.findActiveById(id)
                .orElseThrow(() -> new RuntimeException("사진을 찾을 수 없습니다."));
        photo.delete();
    }
}
