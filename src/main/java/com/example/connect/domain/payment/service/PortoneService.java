package com.example.connect.domain.payment.service;

import com.example.connect.domain.payment.dto.PortoneToken;
import com.example.connect.global.error.errorcode.ErrorCode;
import com.example.connect.global.error.exception.UnAuthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PortoneService {
    private final RestTemplate restTemplate;

    @Value("${portone.api.secret.v2}")
    private String secret;

    public String portoneToken() {
        String url = "https://api.portone.io/login/api-secret";
        Map<String, String> requestBody = new HashMap<>();

        requestBody.put("apiSecret", secret);
        PortoneToken portoneToken = restTemplate.postForEntity(url, requestBody, PortoneToken.class).getBody();

        if (portoneToken != null) {
            return portoneToken.getAccessToken();
        }

        throw new UnAuthorizedException(ErrorCode.FAILED_GET_TOKEN);
    }

    /**
     * portone 요청을 위한 header 생성
     */
    public HttpHeaders makeHeaders() {
        String token = portoneToken();
        HttpHeaders headers = new HttpHeaders();

        headers.set("Authorization", "Bearer " + token);
        headers.set("Content-Type", "application/json");

        return headers;
    }
}
