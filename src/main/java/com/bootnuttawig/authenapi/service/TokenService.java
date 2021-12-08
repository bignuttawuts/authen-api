package com.bootnuttawig.authenapi.service;

import java.util.List;

public interface TokenService {
    public String createAccessToken(String username, List<String> authorities);

    public String createRefreshToken(String username);
}
