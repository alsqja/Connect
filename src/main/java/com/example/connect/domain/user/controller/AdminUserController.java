package com.example.connect.domain.user.controller;

import com.example.connect.domain.user.dto.AdminUserListResDto;
import com.example.connect.domain.user.service.AdminUserService;
import com.example.connect.global.common.dto.CommonResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/users")
public class AdminUserController {

    private final AdminUserService adminUserService;

    @GetMapping
    public ResponseEntity<CommonResDto<AdminUserListResDto>> findAllUser(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        AdminUserListResDto results = adminUserService.findAllUser(page, size);

        return new ResponseEntity<>(new CommonResDto<>("유저 전체 조회 완료", results), HttpStatus.OK);
    }
}
