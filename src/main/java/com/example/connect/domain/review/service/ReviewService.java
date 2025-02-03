package com.example.connect.domain.review.service;

import com.example.connect.domain.match.entity.Matching;
import com.example.connect.domain.match.repository.MatchingRepository;
import com.example.connect.domain.review.dto.ReviewResDto;
import com.example.connect.domain.review.entity.Review;
import com.example.connect.domain.review.repository.ReviewRepository;
import com.example.connect.domain.user.entity.User;
import com.example.connect.domain.user.repository.UserRepository;
import com.example.connect.global.error.errorcode.ErrorCode;
import com.example.connect.global.error.exception.BadRequestException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MatchingRepository matchingRepository;
    private final UserRepository userRepository;

    @Transactional
    public ReviewResDto createReview(Long fromId, Long toId, Long matchingId, Integer rate) {

        if (Objects.equals(fromId, toId)) {
            throw new BadRequestException(ErrorCode.BAD_REQUEST);
        }

        User fromUser = userRepository.findByIdOrElseThrow(fromId);
        User toUser = userRepository.findByIdOrElseThrow(toId);
        Matching matching = matchingRepository.findByIdOrElseThrow(matchingId);

        if (!((matching.getFromSchedule().getUser() == fromUser && matching.getToSchedule().getUser() == toUser)
                || (matching.getFromSchedule().getUser() == toUser && matching.getToSchedule().getUser() == fromUser))) {
            throw new BadRequestException(ErrorCode.BAD_REQUEST);
        }

        if (reviewRepository.existsByFromUserAndToUserAndMatching(fromUser, toUser, matching)) {
            throw new BadRequestException(ErrorCode.BAD_REQUEST);
        }

        Review review = new Review(rate, matching, fromUser, toUser);

        reviewRepository.save(review);

        return new ReviewResDto(review);
    }
}
