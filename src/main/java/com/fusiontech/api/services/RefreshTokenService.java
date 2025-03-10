package com.fusiontech.api.services;

import com.fusiontech.api.models.RefreshToken;

import java.util.Optional;

public interface RefreshTokenService {
    RefreshToken createRefreshToken(String username);
    boolean deleteRefreshToken(String email);
    Optional<RefreshToken> findByRefreshToken(String token);
    RefreshToken verifyExpiration(RefreshToken token);
}
