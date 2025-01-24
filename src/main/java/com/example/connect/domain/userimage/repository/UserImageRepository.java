package com.example.connect.domain.userimage.repository;

import com.example.connect.domain.user.entity.User;
import com.example.connect.domain.userimage.entity.UserImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserImageRepository extends JpaRepository<UserImage, Long> {
    List<UserImage> user(User user);

    Page<UserImage> findByUserId(Long userId, Pageable pageable);
}
