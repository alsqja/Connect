package com.example.connect.domain.userimage.repository;

import com.example.connect.domain.userimage.entity.UserImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserImageRepository extends JpaRepository<UserImage, Long> {

    Page<UserImage> findByUserId(Long userId, Pageable pageable);

    Optional<UserImage> findByUserIdAndId(Long userId, Long id);
}
