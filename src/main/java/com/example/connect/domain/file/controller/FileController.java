package com.example.connect.domain.file.controller;

import com.example.connect.domain.file.dto.UrlReqDto;
import com.example.connect.domain.file.dto.UrlResDto;
import com.example.connect.domain.file.service.FileService;
import com.example.connect.global.common.dto.CommonResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/file-url")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping
    public ResponseEntity<CommonResDto<UrlResDto>> createFileUrl(
            @RequestBody UrlReqDto dto
    ) {

        String preSignedUrl = fileService.createPreSignedUrl(dto.getFileName());

        return new ResponseEntity<>(new CommonResDto<>("url 생성 완료", new UrlResDto(preSignedUrl)), HttpStatus.CREATED);
    }
}
