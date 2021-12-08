package com.bootnuttawig.authenapi.controller;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.bootnuttawig.authenapi.entity.Role;
import com.bootnuttawig.authenapi.entity.User;
import com.bootnuttawig.authenapi.service.TokenService;
import com.bootnuttawig.authenapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final TokenService tokenService;

    @GetMapping("/user")
    public ResponseEntity<List<User>> getAll() {
        return ResponseEntity.ok().body(userService.getUsers());
    }

    @PostMapping("/user/save")
    public ResponseEntity<User> saveUser(@RequestBody User user) {
        URI uri = URI.create(ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/user/save")
                .toUriString());
        return ResponseEntity.created(uri).body(userService.saveUser(user));
    }

    @PostMapping("/token/refresh")
    public ResponseEntity<Map<String, String>> refreshToken(HttpServletRequest request) {

        try {
            String refreshToken = request.getParameter("refresh_token");

            Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(refreshToken);
            String username = decodedJWT.getSubject();


            User user = userService.getUser(username);
            List<String> authorities = user.getRoles()
                    .stream()
                    .map(Role::getName)
                    .collect(Collectors.toList());


            String accessToken = tokenService.createAccessToken(user.getUsername(), authorities);

            Map<String, String> tokens = new HashMap<>();
            tokens.put("access_token", accessToken);

            return ResponseEntity.ok().body(tokens);

        } catch (Exception ex) {
            Map<String, String> error = new HashMap<>();
            error.put("error_message", "invalid token");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
        }

    }

}