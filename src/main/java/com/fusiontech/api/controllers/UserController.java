package com.fusiontech.api.controllers;

import com.fusiontech.api.dtos.user.UserCreateDto;
import com.fusiontech.api.dtos.user.UserRegisterDto;
import com.fusiontech.api.dtos.user.UserUpdateDto;
import com.fusiontech.api.payloads.ApiResponse;
import com.fusiontech.api.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> getAllUsers(@RequestParam(required = false) Integer pageNumber,
                                                   @RequestParam(required = false) Integer pageSize) {
        ApiResponse response = userService.getAllUsers(pageNumber, pageSize);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-email")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> getUserByEmail(@RequestParam String email) {
        ApiResponse response = userService.getUserByEmail(email);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable Long id) {
        ApiResponse response = userService.getUserById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> getSearchUsers(@RequestParam String keyword,
                                                      @RequestParam(required = false) Integer pageNumber,
                                                      @RequestParam(required = false) Integer pageSize) {
        ApiResponse response = userService.getSearchUsers(keyword, pageNumber, pageSize);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> createUser(@RequestBody UserCreateDto createDto) {
        ApiResponse response = userService.createUser(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> updateUser(@PathVariable Long id, @RequestBody UserUpdateDto updateDto) {
        ApiResponse response = userService.updateUser(id, updateDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long id) {
        ApiResponse response = userService.deleteUser(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/count")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> getTotalCount() {
        ApiResponse response = userService.getTotalCount();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/cart")
    public ResponseEntity<ApiResponse> getUserCart(Principal principal) {
        ApiResponse response = userService.getUserCart(principal.getName());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/cart-size")
    public ResponseEntity<ApiResponse> getUserCartSize(Principal principal) {
        ApiResponse response = userService.getUserCartSize(principal.getName());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/favorite")
    public ResponseEntity<ApiResponse> getUserFavoriteProducts(Principal principal,
                                                               @RequestParam(required = false) Integer pageNumber,
                                                               @RequestParam(required = false) Integer pageSize) {
        ApiResponse response = userService.getUserFavoriteProducts(principal.getName(), pageNumber, pageSize);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/favorite-size")
    public ResponseEntity<ApiResponse> getUserFavoriteSize(Principal principal) {
        ApiResponse response = userService.getUserFavoriteSize(principal.getName());
        return ResponseEntity.ok(response);
    }
}
