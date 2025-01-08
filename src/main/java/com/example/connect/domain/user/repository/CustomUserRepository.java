package com.example.connect.domain.user.repository;

import com.example.connect.domain.user.dto.AdminUserResDto;
import com.example.connect.domain.user.entity.UserAdminOnly;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomUserRepository {

    Page<AdminUserResDto> findQueryAdminUserDetails(Pageable pageable);

    UserAdminOnly findByIdWithDeleted(Long id);
}
