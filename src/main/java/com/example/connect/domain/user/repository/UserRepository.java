package com.example.connect.domain.user.repository;

import com.example.connect.domain.user.entity.User;
import com.example.connect.global.error.errorcode.ErrorCode;
import com.example.connect.global.error.exception.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, CustomUserRepository {
    default User findByIdOrElseThrow(Long id) {
        return findById(id).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND));
    }

    Optional<User> findByEmail(String email);

    default User findByEmailOrElseThrow(String email) {
        return findByEmail(email).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND));
    }

    @Query(value = "SELECT COUNT(email) > 0 FROM user WHERE email = :email", nativeQuery = true)
    int existsByEmail(@Param("email") String email);

    @Query("select u from User u where u.birth like concat('%', :birth)")
    List<User> findByBirth(@Param("birth") String birth);
}
