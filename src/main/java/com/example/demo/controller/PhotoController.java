package com.example.demo.controller;

import com.example.demo.domain.Photo;
import com.example.demo.dto.PhotoResponse;
import com.example.demo.service.FileStorageService;
import com.example.demo.service.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("/api/photos")
@RequiredArgsConstructor
public class PhotoController {
    
    private final PhotoService photoService;
    private final FileStorageService fileStorageService;
    
    @GetMapping
    public ResponseEntity<List<PhotoResponse>> getAllPhotos() {
        return ResponseEntity.ok(photoService.getAllPhotos());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<PhotoResponse> getPhoto(@PathVariable Long id) {
        return ResponseEntity.ok(photoService.getPhoto(id));
    }
    
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PhotoResponse> uploadPhoto(
            @RequestParam("file") MultipartFile file,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) Integer displayOrder) {
        
        try {
            String storedFileName = fileStorageService.storeFile(file);
            Path filePath = fileStorageService.loadFileAsPath(storedFileName);
            
            Photo photo = Photo.builder()
                    .originalFileName(file.getOriginalFilename())
                    .storedFileName(storedFileName)
                    .filePath("/api/photos/files/" + storedFileName)
                    .fileSize(file.getSize())
                    .contentType(file.getContentType())
                    .description(description)
                    .displayOrder(displayOrder != null ? displayOrder : 0)
                    .build();
            
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(photoService.createPhoto(photo));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/files/{fileName:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable String fileName) {
        try {
            Path filePath = fileStorageService.loadFileAsPath(fileName);
            Resource resource = new UrlResource(filePath.toUri());
            
            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePhoto(@PathVariable Long id) {
        photoService.deletePhoto(id);
        return ResponseEntity.noContent().build();
    }
}
