package com.example.connect.global.config.auth;

import com.example.connect.domain.membership.entity.Membership;
import com.example.connect.domain.membership.repository.MembershipRepository;
import com.example.connect.domain.user.dto.RedisUserDto;
import com.example.connect.domain.user.entity.User;
import com.example.connect.domain.user.repository.RedisTokenRepository;
import com.example.connect.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final RedisTokenRepository redisTokenRepository;
    private final UserRepository userRepository;
    private final MembershipRepository membershipRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        RedisUserDto sessionUser = redisTokenRepository.getUser(username);

        if (sessionUser == null) {
            User user = userRepository.findByEmailOrElseThrow(username);
            Membership membership = membershipRepository.findByUserIdAndExpiredDateAfter(user.getId(), LocalDate.now()).orElse(null);

            sessionUser = membership == null ? new RedisUserDto(user) : new RedisUserDto(user, membership);
        }

        return new UserDetailsImpl(sessionUser);
    }
}
