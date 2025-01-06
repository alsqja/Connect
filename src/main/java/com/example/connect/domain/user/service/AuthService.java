package com.example.connect.domain.user.service;

import com.example.connect.domain.membership.repository.MembershipRepository;
import com.example.connect.domain.user.dto.SignupResDto;
import com.example.connect.domain.user.dto.SignupServiceDto;
import com.example.connect.domain.user.entity.User;
import com.example.connect.domain.user.repository.UserRepository;
import com.example.connect.global.common.dto.TokenDto;
import com.example.connect.global.error.errorcode.ErrorCode;
import com.example.connect.global.error.exception.BadRequestException;
import com.example.connect.global.error.exception.UnAuthorizedException;
import com.example.connect.global.util.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final MembershipRepository membershipRepository;

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    public SignupResDto signup(SignupServiceDto signupServiceDto) {

        boolean isExist = userRepository.existsByEmail(signupServiceDto.getEmail()) > 0;

        if (isExist) {
            throw new BadRequestException(ErrorCode.INVALID_EMAIL);
        }

        User user = signupServiceDto.toUser();

        user.updatePassword(passwordEncoder.encode(user.getPassword()));

        User savedUser = userRepository.save(user);

        return new SignupResDto(savedUser);
    }

    public TokenDto login(String email, String password) {

        User user = userRepository.findByEmailOrElseThrow(email);

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new UnAuthorizedException(ErrorCode.UNAUTHORIZED_PASSWORD);
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        return jwtProvider.generateToken(authentication);
    }

    public TokenDto refresh(String refreshToken) {

        String email = jwtProvider.getUsername(refreshToken);

        if (!jwtProvider.validRefreshToken(refreshToken, email)) {
            throw new UnAuthorizedException(ErrorCode.EXPIRED_TOKEN);
        }

        return jwtProvider.generateToken(email);
    }
}
