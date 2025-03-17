package com.fusiontech.api.services;

import com.fusiontech.api.payloads.ApiResponse;

public interface FavoritesService {

    ApiResponse addToFavorites(Long productId, String userEmail);

    ApiResponse deleteFavorite(Long productId, String userEmail);
}
