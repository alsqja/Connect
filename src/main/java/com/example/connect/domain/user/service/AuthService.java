package com.example.connect.domain.user.service;

import com.example.connect.domain.address.entity.Address;
import com.example.connect.domain.address.repository.AddressRepository;
import com.example.connect.domain.membership.repository.MembershipRepository;
import com.example.connect.domain.user.dto.SignupServiceDto;
import com.example.connect.domain.user.dto.UserResDto;
import com.example.connect.domain.user.entity.User;
import com.example.connect.domain.user.repository.UserRepository;
import com.example.connect.global.error.errorcode.ErrorCode;
import com.example.connect.global.error.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final MembershipRepository membershipRepository;
    private final AddressRepository addressRepository;

    public UserResDto signup(SignupServiceDto signupServiceDto) {

        boolean isExist = userRepository.existsByEmail(signupServiceDto.getEmail()) > 0;

        if (isExist) {
            throw new BadRequestException(ErrorCode.INVALID_EMAIL);
        }

        User user = signupServiceDto.toUser();
        Address address = signupServiceDto.toAddress();

        User savedUser = userRepository.save(user);

        address.updateUser(savedUser);

        Address savedAddress = addressRepository.save(address);

        return new UserResDto(savedUser, savedAddress);
    }
}
