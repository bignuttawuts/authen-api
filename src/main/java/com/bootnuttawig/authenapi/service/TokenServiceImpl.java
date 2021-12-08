package com.bootnuttawig.authenapi.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class TokenServiceImpl implements TokenService {

    @Override
    public String createAccessToken(String username, List<String> authorities) {
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
        String accessToken = JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + (10 * 60 * 1000))) // 10 minute
                .withIssuer("com.wv.adminapi")
                .withClaim("roles", authorities)
                .sign(algorithm);
        return accessToken;
    }

    @Override
    public String createRefreshToken(String username) {
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
        String refreshToken = JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + (30 * 60 * 1000))) // 30 minute
                .withIssuer("com.wv.adminapi")
                .sign(algorithm);
        return refreshToken;
    }

}
