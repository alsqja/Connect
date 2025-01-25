package com.example.connect.domain.userimage.service;

import com.example.connect.domain.user.entity.User;
import com.example.connect.domain.user.repository.UserRepository;
import com.example.connect.domain.userimage.dto.UserImageDetailResDto;
import com.example.connect.domain.userimage.dto.UserImagePageResDto;
import com.example.connect.domain.userimage.dto.UserImageResDto;
import com.example.connect.domain.userimage.entity.UserImage;
import com.example.connect.domain.userimage.repository.UserImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserImageService {

    private final UserImageRepository userImageRepository;
    private final UserRepository userRepository;

    @Transactional
    public UserImageResDto saveImage(Long userId, UserImage userImage) {

        User user = userRepository.findByIdOrElseThrow(userId);

        userImage.updateUser(user);

        return new UserImageResDto(userImageRepository.save(userImage));
    }

    public UserImagePageResDto findUserImages(Long userId, int page, int size) {

        Pageable pageable = PageRequest.of(page - 1, size);

        Page<UserImage> userImagePage = userImageRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);

        return new UserImagePageResDto(userImagePage);
    }

    @Transactional
    public UserImageResDto updateImage(Long userId, Long id, String url, String description) {

        UserImage userImage = userImageRepository.findByUserIdAndIdOrElseThrow(userId, id);

        userImage.updateFields(url, description);

        return new UserImageResDto(userImage);
    }

    @Transactional
    public void deleteImage(Long userId, Long id) {

        UserImage userImage = userImageRepository.findByUserIdAndIdOrElseThrow(userId, id);

        userImageRepository.delete(userImage);
    }

    public UserImageDetailResDto findById(Long userId, Long id) {

        UserImage userImage = userImageRepository.findByUserIdAndIdOrElseThrow(userId, id);

        return new UserImageDetailResDto(userImage);
    }
}
