package com.example.connect.domain.userimage.service;

import com.example.connect.domain.user.entity.User;
import com.example.connect.domain.user.repository.UserRepository;
import com.example.connect.domain.userimage.dto.UserImagePageResDto;
import com.example.connect.domain.userimage.dto.UserImageResDto;
import com.example.connect.domain.userimage.entity.UserImage;
import com.example.connect.domain.userimage.repository.UserImageRepository;
import com.example.connect.global.error.errorcode.ErrorCode;
import com.example.connect.global.error.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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

        Page<UserImage> userImagePage = userImageRepository.findByUserId(userId, pageable);

        return new UserImagePageResDto(userImagePage);
    }

    @Transactional
    public UserImageResDto updateImage(Long userId, Long id, String url, String description) {

        Optional<UserImage> userImage = userImageRepository.findByUserIdAndId(userId, id);

        if (userImage.isEmpty()) {
            throw new NotFoundException(ErrorCode.NOT_FOUND);
        }

        userImage.get().updateFields(url, description);

        return new UserImageResDto(userImage.get());
    }

    @Transactional
    public void deleteImage(Long userId, Long id) {

        Optional<UserImage> userImage = userImageRepository.findByUserIdAndId(userId, id);

        if (userImage.isEmpty()) {
            throw new NotFoundException(ErrorCode.NOT_FOUND);
        }

        userImageRepository.delete(userImage.get());
    }
}
