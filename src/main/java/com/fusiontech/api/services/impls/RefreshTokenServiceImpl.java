package com.fusiontech.api.services.impls;

import com.fusiontech.api.models.RefreshToken;
import com.fusiontech.api.models.UserEntity;
import com.fusiontech.api.repositories.RefreshTokenRepository;
import com.fusiontech.api.repositories.UserRepository;
import com.fusiontech.api.services.RefreshTokenService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    public RefreshTokenServiceImpl(RefreshTokenRepository refreshTokenRepository, UserRepository userRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }

    public RefreshToken createRefreshToken(String email){
        RefreshToken refreshToken = RefreshToken.builder()
                .user(userRepository.findByEmail(email).orElseThrow(()-> new RuntimeException("User not found for email: " + email)))
                .refreshToken(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(24 * 60 * 60 * 1000)) //1 gün
                .build();
        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public boolean deleteRefreshToken(String email) {
        try {
            Optional<UserEntity> optionalUser = userRepository.findByEmail(email);
            if (optionalUser.isPresent()){
                RefreshToken refreshToken = refreshTokenRepository.findByUserId(optionalUser.get().getId()).orElseThrow();
                refreshTokenRepository.delete(refreshToken);
                return true;
            }
            return false;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    public Optional<RefreshToken> findByRefreshToken(String token){
        return refreshTokenRepository.findByRefreshToken(token);
    }

    public RefreshToken verifyExpiration(RefreshToken token){
        if(token.getExpiryDate().compareTo(Instant.now())<0){
            refreshTokenRepository.delete(token);
            throw new RuntimeException(token.getRefreshToken() + " Refresh token is expired. Please make a new login..!");
        }
        return token;
    }
}
