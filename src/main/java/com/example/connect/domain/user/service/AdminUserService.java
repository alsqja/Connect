package com.example.connect.domain.user.service;

import com.example.connect.domain.user.dto.AdminUserListResDto;
import com.example.connect.domain.user.dto.AdminUserResDto;
import com.example.connect.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final UserRepository userRepository;

    public AdminUserListResDto findAllUser(int page, int size) {

        Pageable pageable = PageRequest.of(page - 1, size);
        Page<AdminUserResDto> userDetails = userRepository.findAdminUserDetails(pageable);

        return new AdminUserListResDto(page, size, userDetails.getTotalElements(), userDetails.getTotalPages(), userDetails.getContent());
    }
}
