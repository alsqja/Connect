package com.example.connect.domain.payment.service;

import com.example.connect.domain.payment.dto.PortoneToken;
import com.example.connect.global.error.errorcode.ErrorCode;
import com.example.connect.global.error.exception.UnAuthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PortoneService {
    private final WebClient webClient;

    @Value("${portone.api.secret.v2}")
    private String secret;

    public String portoneToken() {
        Map<String, String> requestBody = new HashMap<>();

        requestBody.put("apiSecret", secret);

        Mono<PortoneToken> postToken = webClient.post()
                .uri("/login/api-secret")
                .body(Mono.just(requestBody), Map.class)
                .retrieve()
                .bodyToMono(PortoneToken.class);

        PortoneToken portoneToken = postToken.block();

        if (Objects.requireNonNull(portoneToken).getAccessToken() != null) {
            return portoneToken.getAccessToken();
        }

        throw new UnAuthorizedException(ErrorCode.FAILED_GET_TOKEN);
    }
}
