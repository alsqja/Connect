package com.example.connect.domain.userimage.repository;

import com.example.connect.domain.userimage.entity.UserImage;
import com.example.connect.global.error.errorcode.ErrorCode;
import com.example.connect.global.error.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserImageRepository extends JpaRepository<UserImage, Long> {

    Page<UserImage> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    Optional<UserImage> findByUserIdAndId(Long userId, Long id);

    default UserImage findByUserIdAndIdOrElseThrow(Long userId, Long id) {
        return findByUserIdAndId(userId, id).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND));
    }
}
