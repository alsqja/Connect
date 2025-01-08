package com.example.connect.domain.user.service;

import com.example.connect.domain.user.dto.AdminUserListResDto;
import com.example.connect.domain.user.dto.AdminUserResDto;
import com.example.connect.domain.user.dto.UpdateUserResDto;
import com.example.connect.domain.user.entity.User;
import com.example.connect.domain.user.repository.UserRepository;
import com.example.connect.global.enums.UserRole;
import com.example.connect.global.enums.UserStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final UserRepository userRepository;

    public AdminUserListResDto findAllUser(int page, int size) {

        Pageable pageable = PageRequest.of(page - 1, size);
        Page<AdminUserResDto> userDetails = userRepository.findAdminUserDetails(pageable);

        return new AdminUserListResDto(page, size, userDetails.getTotalElements(), userDetails.getTotalPages(), userDetails.getContent());
    }

    @Transactional
    public UpdateUserResDto updateUser(Long id, UserRole role, UserStatus status, Boolean isDeleted) {

        User user = userRepository.findByIdOrElseThrow(id);

        if (role != null) {
            user.updateRole(role);
        }
        if (status != null) {
            user.updateStatus(status);
        }
        if (isDeleted != null) {
            user.updateIsDeleted(isDeleted);
        }

        return new UpdateUserResDto(user);
    }
}
