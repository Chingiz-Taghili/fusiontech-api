package com.fusiontech.api.services.impls;

import com.fusiontech.api.exceptions.ResourceNotFoundException;
import com.fusiontech.api.models.Favorite;
import com.fusiontech.api.models.Product;
import com.fusiontech.api.models.UserEntity;
import com.fusiontech.api.payloads.ApiResponse;
import com.fusiontech.api.payloads.MessageResponse;
import com.fusiontech.api.repositories.FavoritesRepository;
import com.fusiontech.api.repositories.ProductRepository;
import com.fusiontech.api.repositories.UserRepository;
import com.fusiontech.api.services.FavoritesService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FavoritesServiceImpl implements FavoritesService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final FavoritesRepository favoritesRepository;

    public FavoritesServiceImpl(UserRepository userRepository, ProductRepository productRepository, FavoritesRepository favoritesRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.favoritesRepository = favoritesRepository;
    }

    @Transactional
    @Override
    public ApiResponse addToFavorites(Long productId, String userEmail) {
        Product findProduct = productRepository.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException("Product", "id", productId));
        UserEntity findUser = userRepository.findByEmail(userEmail).orElseThrow(
                () -> new ResourceNotFoundException("User", "email", userEmail));
        if (favoritesRepository.existsByUserIdAndProductId(findUser.getId(), productId)) {
            return new MessageResponse("Product is already in favorites.");
        }
        Favorite favorite = new Favorite();
        favorite.setUser(findUser);
        favorite.setProduct(findProduct);
        favoritesRepository.save(favorite);
        return new MessageResponse("Product successfully added to favorites.");
    }

    @Transactional
    @Override
    public ApiResponse deleteFavorite(Long productId, String userEmail) {
        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException("Product", "id", productId);
        }
        UserEntity findUser = userRepository.findByEmail(userEmail).orElseThrow(
                () -> new ResourceNotFoundException("User", "email", userEmail));
        Favorite findFavorite = favoritesRepository.findByUserIdAndProductId(
                findUser.getId(), productId).orElseThrow(() -> new ResourceNotFoundException(
                "Favorite", "userId", findUser.getId() + ", productId: " + productId));
        favoritesRepository.delete(findFavorite);
        return new MessageResponse("Favorite deleted successfully");
    }
}
