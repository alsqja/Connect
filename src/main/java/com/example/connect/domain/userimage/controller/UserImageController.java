package com.example.connect.domain.userimage.controller;

import com.example.connect.domain.user.dto.RedisUserDto;
import com.example.connect.domain.userimage.dto.UserImageReqDto;
import com.example.connect.domain.userimage.dto.UserImageResDto;
import com.example.connect.domain.userimage.entity.UserImage;
import com.example.connect.domain.userimage.service.UserImageService;
import com.example.connect.global.common.dto.CommonResDto;
import com.example.connect.global.config.auth.UserDetailsImpl;
import com.example.connect.global.error.errorcode.ErrorCode;
import com.example.connect.global.error.exception.BadRequestException;
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
@RequestMapping("/api/users/{userId}/images")
@RequiredArgsConstructor
public class UserImageController {

    private final UserImageService userImageService;

    @PostMapping
    public ResponseEntity<CommonResDto<UserImageResDto>> saveImage(
            @PathVariable Long userId,
            @RequestBody UserImageReqDto dto,
            Authentication authentication
    ) {

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        RedisUserDto user = userDetails.getUser();

        if (!user.getId().equals(userId)) {
            throw new BadRequestException(ErrorCode.BAD_REQUEST);
        }

        UserImage inputImage = new UserImage(dto.getUrl(), dto.getDescription());

        UserImageResDto result = userImageService.saveImage(userId, inputImage);

        return new ResponseEntity<>(new CommonResDto<>("피드 저장 완료", result), HttpStatus.CREATED);
    }
}
