package com.fusiontech.api.controllers;

import com.fusiontech.api.dtos.user.UserCreateDto;
import com.fusiontech.api.dtos.user.UserRegisterDto;
import com.fusiontech.api.dtos.user.UserUpdateDto;
import com.fusiontech.api.payloads.ApiResponse;
import com.fusiontech.api.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllUsers(@RequestParam Integer pageNumber, @RequestParam Integer pageSize) {
        ApiResponse response = userService.getAllUsers(pageNumber, pageSize);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-email")
    public ResponseEntity<ApiResponse> getUserByEmail(@RequestParam String email) {
        ApiResponse response = userService.getUserByEmail(email);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable Long id) {
        ApiResponse response = userService.getUserById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse> getSearchUsers(
            @RequestParam String keyword, @RequestParam Integer pageNumber, @RequestParam Integer pageSize) {
        ApiResponse response = userService.getSearchUsers(keyword, pageNumber, pageSize);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createUser(@RequestBody UserCreateDto createDto) {
        ApiResponse response = userService.createUser(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse> updateUser(@PathVariable Long id, @RequestBody UserUpdateDto updateDto) {
        ApiResponse response = userService.updateUser(id, updateDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long id) {
        ApiResponse response = userService.deleteUser(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/total-count")
    public ResponseEntity<ApiResponse> getTotalCount() {
        ApiResponse response = userService.getTotalCount();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/cart")
    public ResponseEntity<ApiResponse> getUserCart(@RequestParam String email) {
        ApiResponse response = userService.getUserCart(email);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/cart-size")
    public ResponseEntity<ApiResponse> getUserCartSize(@RequestParam String email) {
        ApiResponse response = userService.getUserCartSize(email);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/favorite")
    public ResponseEntity<ApiResponse> getUserFavoriteProducts(
            @RequestParam String email, @RequestParam Integer pageNumber, @RequestParam Integer pageSize) {
        ApiResponse response = userService.getUserFavoriteProducts(email, pageNumber, pageSize);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/favorite-size")
    public ResponseEntity<ApiResponse> getUserFavoriteSize(@RequestParam String email) {
        ApiResponse response = userService.getUserFavoriteSize(email);
        return ResponseEntity.ok(response);
    }
}
