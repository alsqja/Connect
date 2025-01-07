package com.example.connect.domain.user.service;

import com.example.connect.domain.membership.repository.MembershipRepository;
import com.example.connect.domain.user.dto.RedisUserDto;
import com.example.connect.domain.user.dto.UpdateUserServiceDto;
import com.example.connect.domain.user.dto.UserResDto;
import com.example.connect.domain.user.entity.User;
import com.example.connect.domain.user.repository.RedisTokenRepository;
import com.example.connect.domain.user.repository.UserRepository;
import com.example.connect.global.error.errorcode.ErrorCode;
import com.example.connect.global.error.exception.UnAuthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final MembershipRepository membershipRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisTokenRepository redisTokenRepository;

    @Transactional
    public UserResDto updateMe(UpdateUserServiceDto serviceDto) {

        User me = userRepository.findByIdOrElseThrow(serviceDto.getSessionUser().getId());

        if (serviceDto.getOldPassword() != null && serviceDto.getNewPassword() != null) {

            if (!passwordEncoder.matches(serviceDto.getOldPassword(), me.getPassword())) {
                throw new UnAuthorizedException(ErrorCode.UNAUTHORIZED_PASSWORD);
            }

            me.updatePassword(passwordEncoder.encode(serviceDto.getNewPassword()));
        }

        me.updateUserFields(serviceDto.getName(), serviceDto.getProfileUrl(), serviceDto.getIsActiveMatching());

        RedisUserDto redisUserDto = new RedisUserDto(me);

        redisUserDto.updateMembership(serviceDto.getSessionUser().getMembershipType(), serviceDto.getSessionUser().getExpiredAt());
        redisTokenRepository.saveUser(redisUserDto);

        return new UserResDto(redisUserDto);
    }

    public void checkPassword(RedisUserDto me, String password) {

        if (!passwordEncoder.matches(password, me.getPassword())) {
            throw new UnAuthorizedException(ErrorCode.UNAUTHORIZED_PASSWORD);
        }
    }
}
