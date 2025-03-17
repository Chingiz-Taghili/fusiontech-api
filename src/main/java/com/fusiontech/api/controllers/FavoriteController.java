package com.fusiontech.api.controllers;

import com.fusiontech.api.payloads.ApiResponse;
import com.fusiontech.api.services.FavoritesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("api/favorites")
public class FavoriteController {
    private final FavoritesService favoritesService;

    public FavoriteController(FavoritesService favoritesService) {
        this.favoritesService = favoritesService;
    }

    @PostMapping("/{id}")
    public ResponseEntity<ApiResponse> addToFavorites(@PathVariable Long id, Principal principal) {
        ApiResponse response = favoritesService.addToFavorites(id, principal.getName());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteFavorite(@PathVariable Long id, Principal principal) {
        ApiResponse response = favoritesService.deleteFavorite(id, principal.getName());
        return ResponseEntity.ok(response);
    }
}
