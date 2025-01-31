package com.example.connect.domain.review.repository;

import com.example.connect.domain.match.entity.Matching;
import com.example.connect.domain.review.entity.Review;
import com.example.connect.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    boolean existsByFromUserAndToUserAndMatching(User fromUser, User toUser, Matching matching);
}
