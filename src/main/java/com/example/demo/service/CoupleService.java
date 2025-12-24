package com.example.demo.service;

import com.example.demo.domain.Couple;
import com.example.demo.dto.CoupleRequest;
import com.example.demo.dto.CoupleResponse;
import com.example.demo.repository.CoupleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CoupleService {
    
    private final CoupleRepository coupleRepository;
    
    public CoupleResponse getCouple() {
        Couple couple = coupleRepository.findFirstByOrderByIdAsc()
                .orElseThrow(() -> new RuntimeException("커플 정보를 찾을 수 없습니다."));
        return CoupleResponse.from(couple);
    }
    
    @Transactional
    public CoupleResponse createCouple(CoupleRequest request) {
        // 기존 커플 정보가 있으면 업데이트, 없으면 생성
        Couple couple = coupleRepository.findFirstByOrderByIdAsc()
                .orElse(Couple.builder()
                        .partner1Name(request.getPartner1Name())
                        .partner2Name(request.getPartner2Name())
                        .anniversaryDate(request.getAnniversaryDate())
                        .description(request.getDescription())
                        .backgroundImageUrl(request.getBackgroundImageUrl())
                        .build());
        
        if (couple.getId() != null) {
            couple.update(request.getPartner1Name(), request.getPartner2Name(),
                         request.getAnniversaryDate(), request.getDescription(),
                         request.getBackgroundImageUrl());
        } else {
            couple = coupleRepository.save(couple);
        }
        
        return CoupleResponse.from(couple);
    }
    
    @Transactional
    public void deleteCouple(Long id) {
        Couple couple = coupleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("커플 정보를 찾을 수 없습니다."));
        coupleRepository.delete(couple);
    }
}
