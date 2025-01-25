package com.example.connect.domain.user.service;

import com.example.connect.domain.membership.repository.MembershipRepository;
import com.example.connect.domain.point.repository.PointRepository;
import com.example.connect.domain.user.dto.RedisUserDto;
import com.example.connect.domain.user.dto.UpdateUserServiceDto;
import com.example.connect.domain.user.dto.UserSimpleResDto;
import com.example.connect.domain.user.entity.User;
import com.example.connect.domain.user.repository.RedisTokenRepository;
import com.example.connect.domain.user.repository.UserRepository;
import com.example.connect.global.aop.annotation.CheckMembership;
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
    private final PointRepository pointRepository;

    @Transactional
    public RedisUserDto updateMe(UpdateUserServiceDto serviceDto) {

        User me = userRepository.findByIdOrElseThrow(serviceDto.getSessionUser().getId());

        if (serviceDto.getOldPassword() != null && serviceDto.getNewPassword() != null) {

            if (!passwordEncoder.matches(serviceDto.getOldPassword(), me.getPassword())) {
                throw new UnAuthorizedException(ErrorCode.UNAUTHORIZED_PASSWORD);
            }

            me.updatePassword(passwordEncoder.encode(serviceDto.getNewPassword()));
        }

        me.updateUserFields(serviceDto.getName(), serviceDto.getProfileUrl(), serviceDto.getIsActiveMatching());

        RedisUserDto redisUserDto = new RedisUserDto(me);

        redisUserDto.updateMembership(serviceDto.getSessionUser().getMembershipType(), serviceDto.getSessionUser().getExpiredDate());
        redisTokenRepository.saveUser(redisUserDto);

        return redisUserDto;
    }

    public void checkPassword(RedisUserDto me, String password) {

        if (!passwordEncoder.matches(password, me.getPassword())) {
            throw new UnAuthorizedException(ErrorCode.UNAUTHORIZED_PASSWORD);
        }
    }

    @Transactional
    public void deleteUser(RedisUserDto me) {

        redisTokenRepository.deleteUser(me.getEmail());
        redisTokenRepository.deleteRefreshToken(me.getEmail());

        userRepository.deleteById(me.getId());
    }

    @CheckMembership
    public UserSimpleResDto findById(Long id, RedisUserDto me) {

        User user = userRepository.findByIdOrElseThrow(id);

        return new UserSimpleResDto(user);
    }
}
