package com.example.connect.domain.userimage.repository;

import com.example.connect.domain.userimage.entity.UserImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserImageRepository extends JpaRepository<UserImage, Long> {
}
