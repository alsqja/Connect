package com.example.connect.domain.user.service;

import com.example.connect.domain.membership.entity.Membership;
import com.example.connect.domain.membership.repository.MembershipRepository;
import com.example.connect.domain.user.dto.UserResDto;
import com.example.connect.domain.user.entity.User;
import com.example.connect.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final MembershipRepository membershipRepository;

    @Transactional
    public UserResDto findMe(User me) {

        Membership membership = membershipRepository.findByUserIdAndExpiredAtAfter(me.getId(), LocalDateTime.now()).orElse(null);

        if (membership == null) {
            return new UserResDto(me);
        }

        return new UserResDto(me, membership);
    }
}
