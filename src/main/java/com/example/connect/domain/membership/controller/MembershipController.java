package com.example.connect.domain.membership.controller;

import com.example.connect.domain.membership.service.MembershipService;
import com.example.connect.domain.user.dto.RedisUserDto;
import com.example.connect.global.config.auth.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/memberships")
public class MembershipController {
    private final MembershipService membershipService;

    @DeleteMapping
    public ResponseEntity deleteMembership(
            Authentication authentication
    ) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        RedisUserDto me = userDetails.getUser();

        membershipService.deleteMembership(me.getId());

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
