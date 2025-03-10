package com.fusiontech.api.services;

import com.fusiontech.api.dtos.user.UserCreateDto;
import com.fusiontech.api.dtos.user.UserRegisterDto;
import com.fusiontech.api.dtos.user.UserUpdateDto;
import com.fusiontech.api.payloads.ApiResponse;

public interface UserService {

    ApiResponse getAllUsers(Integer pageNumber, Integer pageSize);

    ApiResponse getSearchUsers(String keyword, Integer pageNumber, Integer pageSize);

    ApiResponse getUserByEmail(String email);

    ApiResponse getUserById(Long id);

    ApiResponse createUser(UserCreateDto createDto);

    ApiResponse registerUser(UserRegisterDto registerDto);

    ApiResponse updateUser(Long id, UserUpdateDto updateDto);

    ApiResponse deleteUser(Long id);

    ApiResponse getUserCart(String userEmail);

    ApiResponse getUserCartSize(String userEmail);

    ApiResponse getUserFavoriteProducts(String userEmail, Integer pageNumber, Integer pageSize);

    ApiResponse getUserFavoriteSize(String userEmail);

    ApiResponse getTotalCount();

//    ApiResponse confirmUser(String email, String token);
}
