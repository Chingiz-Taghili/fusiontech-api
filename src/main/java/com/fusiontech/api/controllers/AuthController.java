package com.fusiontech.api.controllers;

import com.fusiontech.api.security.jwtdtos.JwtResponseDto;
import com.fusiontech.api.security.jwtdtos.RefreshTokenDto;
import com.fusiontech.api.dtos.user.UserLoginDto;
import com.fusiontech.api.dtos.user.UserRegisterDto;
import com.fusiontech.api.models.RefreshToken;
import com.fusiontech.api.payloads.ApiResponse;
import com.fusiontech.api.security.JwtService;
import com.fusiontech.api.services.RefreshTokenService;
import com.fusiontech.api.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;

    public AuthController(UserService userService, AuthenticationManager authenticationManager, RefreshTokenService refreshTokenService, JwtService jwtService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.refreshTokenService = refreshTokenService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> registerUser(@RequestBody UserRegisterDto registerDto) {
        ApiResponse response = userService.registerUser(registerDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public JwtResponseDto loginAndGetToken(@RequestBody UserLoginDto loginDto) {
        //Login edən istifadəçinin köhnə refresh token-i silinir
        refreshTokenService.deleteRefreshToken(loginDto.getEmail());
        //Login edən istifadəçi üçün yeni Authentication obyekti yaradılır
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
        //Əgər istifadəçi məlumatları doğrulansa (isAuthenticated), ona JwtResponseDto obyekti
        //daxilində həm access token,həm də refresh token təqdim edilir.
        if (authentication.isAuthenticated()) {
            //refresh token yaradılır
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(loginDto.getEmail());
            //JwtResponseDto yaradılır və qaytarılır
            return JwtResponseDto.builder()
                    .accessToken(jwtService.generateToken(loginDto.getEmail()))
                    .refreshToken(refreshToken.getRefreshToken())
                    .build();
        } else {
            throw new UsernameNotFoundException("invalid user request..!!");
        }
    }

    @PostMapping("/refresh-token")
    public JwtResponseDto refreshToken(@RequestBody RefreshTokenDto refreshTokenDto) {
        //İstifadəçinin təqdim etdiyi refresh token database-də axtarılır
        return refreshTokenService.findByRefreshToken(refreshTokenDto.getRefreshToken())
                //Database-də tapılsa istifadə müddəti yoxlanılır
                .map(refreshTokenService::verifyExpiration)
                //İstifadə müddəti bitməyibsə obyektdən username alınır
                .map(RefreshToken::getUser)
                //Həmin username-ə uyğun yeni access token yaradılır
                .map(userInfo -> {
                    String accessToken = jwtService.generateToken(userInfo.getUsername());
                    //Yeni access token refresh tokenlə bir yerdə istifadəçiyə qaytarılır
                    return JwtResponseDto.builder()
                            .accessToken(accessToken)
                            .refreshToken(refreshTokenDto.getRefreshToken()).build();
                }).orElseThrow(() -> new RuntimeException("Refresh token is not in DB..!!"));
    }

    @PostMapping("/logout")
    public String logoutUser(@RequestHeader("Authorization") String authHeader) {
        try {
            String accessToken = authHeader.substring(7);
            String email = jwtService.extractUsername(accessToken);
            boolean isDeleted = refreshTokenService.deleteRefreshToken(email);
            if (isDeleted) {
                return "User successfully logged out";
            } else {
                return "Refresh token not found or could not be deleted";
            }
        } catch (Exception e) {
            return "Invalid access token or error processing request";
        }
    }

//    @GetMapping("/confirm")
//    public ResponseEntity<ApiResponse> confirmEmail(@RequestParam String email, @RequestParam String token) {
//        ApiResponse response = userService.confirmUser(email, token);
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }

//    @PostMapping("/otp/{email}")
//    public ResponseEntity<ApiResponse> confirmEmail(@PathVariable String email) {
//        ApiResponse response = userService.sendOtp(email);
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }
//
//    @PostMapping("/otp")
//    public ResponseEntity<ApiResponse> confirmEmail(@RequestBody OtpDto otpDto) {
//        ApiResponse response = userService.confirmOtp(otpDto);
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }
}
