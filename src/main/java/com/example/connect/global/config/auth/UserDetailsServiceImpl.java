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

import java.time.LocalDateTime;

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
            System.out.println("seq user query");
            Membership membership = membershipRepository.findByUserIdAndExpiredAtAfter(user.getId(), LocalDateTime.now()).orElse(null);
            System.out.println("seq membership query");

            if (membership == null) {
                sessionUser = new RedisUserDto(user);
            } else {
                sessionUser = new RedisUserDto(user, membership);
            }
        }

        return new UserDetailsImpl(sessionUser);
    }
}
