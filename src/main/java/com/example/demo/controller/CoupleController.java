package com.example.demo.controller;

import com.example.demo.dto.CoupleRequest;
import com.example.demo.dto.CoupleResponse;
import com.example.demo.service.CoupleService;
import com.example.demo.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/couple")
@RequiredArgsConstructor
public class CoupleController {
    
    private final CoupleService coupleService;
    private final FileStorageService fileStorageService;
    
    @GetMapping
    public ResponseEntity<CoupleResponse> getCouple() {
        return ResponseEntity.ok(coupleService.getCouple());
    }
    
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CoupleResponse> createOrUpdateCouple(
            @RequestParam("partner1Name") String partner1Name,
            @RequestParam("partner2Name") String partner2Name,
            @RequestParam("anniversaryDate") String anniversaryDate,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) MultipartFile photo) {
        
        try {
            String photoUrl = null;
            
            // 사진이 있으면 저장
            if (photo != null && !photo.isEmpty()) {
                String storedFileName = fileStorageService.storeFile(photo);
                photoUrl = "/api/couple/photo/" + storedFileName;
            }
            
            CoupleRequest request = new CoupleRequest(
                    partner1Name,
                    partner2Name,
                    java.time.LocalDate.parse(anniversaryDate),
                    description,
                    photoUrl
            );
            
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(coupleService.createCouple(request));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCouple(@PathVariable Long id) {
        try {
            coupleService.deleteCouple(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/photo/{fileName:.+}")
    public ResponseEntity<org.springframework.core.io.Resource> getPhoto(@PathVariable String fileName) {
        try {
            java.nio.file.Path filePath = fileStorageService.loadFileAsPath(fileName);
            org.springframework.core.io.Resource resource = new org.springframework.core.io.UrlResource(filePath.toUri());
            
            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .header(org.springframework.http.HttpHeaders.CONTENT_DISPOSITION, 
                                "inline; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
