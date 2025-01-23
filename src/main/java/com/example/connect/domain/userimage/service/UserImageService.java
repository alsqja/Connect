package com.example.connect.domain.userimage.service;

import com.example.connect.domain.user.entity.User;
import com.example.connect.domain.user.repository.UserRepository;
import com.example.connect.domain.userimage.dto.UserImageResDto;
import com.example.connect.domain.userimage.entity.UserImage;
import com.example.connect.domain.userimage.repository.UserImageRepository;
import lombok.RequiredArgsConstructor;
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
}
