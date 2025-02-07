package com.example.connect.domain.review.controller;

import com.example.connect.domain.review.dto.ReviewReqDto;
import com.example.connect.domain.review.dto.ReviewResDto;
import com.example.connect.domain.review.service.ReviewService;
import com.example.connect.domain.user.dto.RedisUserDto;
import com.example.connect.global.common.dto.CommonResDto;
import com.example.connect.global.config.auth.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/matchings/{matchingId}/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<CommonResDto<ReviewResDto>> createReview(@Valid @RequestBody ReviewReqDto reviewReqDto,
                                                                   @PathVariable Long matchingId,
                                                                   Authentication authentication) {

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        RedisUserDto redisUserDto = userDetails.getUser();

        ReviewResDto result = reviewService.createReview(redisUserDto.getId(), reviewReqDto.getToId(), matchingId, reviewReqDto.getRate());

        return new ResponseEntity<>(new CommonResDto<>("리뷰 등록 완료.", result), HttpStatus.CREATED);
    }
}
