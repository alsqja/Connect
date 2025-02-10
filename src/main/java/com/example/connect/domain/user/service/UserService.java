package com.example.connect.domain.user.service;

import com.example.connect.domain.review.entity.Review;
import com.example.connect.domain.review.repository.ReviewRepository;
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

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisTokenRepository redisTokenRepository;
    private final ReviewRepository reviewRepository;

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

    @Transactional
    public void deleteUser(RedisUserDto me) {

        redisTokenRepository.deleteUser(me.getEmail());
        redisTokenRepository.deleteRefreshToken(me.getEmail());

        User user = userRepository.findByIdOrElseThrow(me.getId());

        userRepository.delete(user);
    }

    @CheckMembership
    public UserSimpleResDto findById(Long id) {

        User user = userRepository.findByIdOrElseThrow(id);

        List<Review> reviews = reviewRepository.findByToUser(user);

        int sum = 0;

        for (Review review : reviews) {
            sum += review.getRate();
        }
        double rateAvg = (double) sum / reviews.size();

        return new UserSimpleResDto(user, rateAvg);
    }
}
