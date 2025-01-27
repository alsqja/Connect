package com.example.connect.domain.userimage.controller;

import com.example.connect.domain.user.dto.RedisUserDto;
import com.example.connect.domain.userimage.dto.UserImageDetailResDto;
import com.example.connect.domain.userimage.dto.UserImagePageResDto;
import com.example.connect.domain.userimage.dto.UserImageReqDto;
import com.example.connect.domain.userimage.dto.UserImageResDto;
import com.example.connect.domain.userimage.entity.UserImage;
import com.example.connect.domain.userimage.service.UserImageService;
import com.example.connect.global.common.dto.CommonResDto;
import com.example.connect.global.config.auth.UserDetailsImpl;
import com.example.connect.global.error.errorcode.ErrorCode;
import com.example.connect.global.error.exception.ForbiddenException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
            throw new ForbiddenException(ErrorCode.FORBIDDEN_PERMISSION);
        }

        UserImage inputImage = new UserImage(dto.getUrl(), dto.getDescription());

        UserImageResDto result = userImageService.saveImage(userId, inputImage);

        return new ResponseEntity<>(new CommonResDto<>("피드 저장 완료", result), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<CommonResDto<UserImagePageResDto>> findImages(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "12") int size
    ) {

        UserImagePageResDto results = userImageService.findUserImages(userId, page, size);

        return new ResponseEntity<>(new CommonResDto<>("피드 조회 완료", results), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CommonResDto<UserImageResDto>> updateImage(
            @PathVariable Long userId,
            @PathVariable Long id,
            @RequestBody UserImageReqDto dto,
            Authentication authentication
    ) {

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        RedisUserDto user = userDetails.getUser();

        if (!user.getId().equals(userId)) {
            throw new ForbiddenException(ErrorCode.FORBIDDEN_PERMISSION);
        }

        UserImageResDto result = userImageService.updateImage(userId, id, dto.getUrl(), dto.getDescription());

        return new ResponseEntity<>(new CommonResDto<>("피드 수정 완료", result), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteImage(
            @PathVariable Long userId,
            @PathVariable Long id,
            Authentication authentication
    ) {

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        RedisUserDto user = userDetails.getUser();

        if (!user.getId().equals(userId)) {
            throw new ForbiddenException(ErrorCode.FORBIDDEN_PERMISSION);
        }

        userImageService.deleteImage(userId, id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommonResDto<UserImageDetailResDto>> findImageById(
            @PathVariable Long userId,
            @PathVariable Long id
    ) {

        UserImageDetailResDto result = userImageService.findById(userId, id);

        return new ResponseEntity<>(new CommonResDto<>("피드 단일 조회 완료", result), HttpStatus.OK);
    }
}
