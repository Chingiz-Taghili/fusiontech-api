package com.fusiontech.api.services.impls;

import com.fusiontech.api.dtos.cart.CartItemCreateDto;
import com.fusiontech.api.exceptions.ResourceNotFoundException;
import com.fusiontech.api.models.CartItem;
import com.fusiontech.api.models.Product;
import com.fusiontech.api.models.UserEntity;
import com.fusiontech.api.payloads.ApiResponse;
import com.fusiontech.api.payloads.MessageResponse;
import com.fusiontech.api.repositories.CartItemRepository;
import com.fusiontech.api.repositories.ProductRepository;
import com.fusiontech.api.repositories.UserRepository;
import com.fusiontech.api.services.CartItemService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CartItemServiceImpl implements CartItemService {
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public CartItemServiceImpl(CartItemRepository cartItemRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    @Override
    public ApiResponse addToCart(CartItemCreateDto createDto, String userEmail) {
        UserEntity findUser = userRepository.findByEmail(userEmail).orElseThrow(
                () -> new ResourceNotFoundException("User", "email", userEmail));
        Product findProduct = productRepository.findById(createDto.getProductId()).orElseThrow(
                () -> new ResourceNotFoundException("Product", "id", createDto.getProductId()));
        Optional<CartItem> optionalItem = cartItemRepository
                .findByUserIdAndProductId(findUser.getId(), findProduct.getId());
        if (optionalItem.isEmpty()) {
            CartItem item = new CartItem();
            item.setUser(findUser);
            item.setProduct(findProduct);
            item.setQuantity(createDto.getQuantity());
            cartItemRepository.save(item);
            return new MessageResponse("Product successfully added to the cart.");
        } else {
            CartItem item = optionalItem.get();
            item.setQuantity(item.getQuantity() + createDto.getQuantity());
            cartItemRepository.save(item);
            return new MessageResponse("Product quantity updated in the cart.");
        }
    }

    @Transactional
    @Override
    public ApiResponse deleteCartItem(Long productId, String userEmail) {
        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException("Product", "id", productId);
        }
        UserEntity findUser = userRepository.findByEmail(userEmail).orElseThrow(
                () -> new ResourceNotFoundException("User", "email", userEmail));
        CartItem cartItem = cartItemRepository.findByUserIdAndProductId(
                findUser.getId(), productId).orElseThrow(() -> new ResourceNotFoundException(
                "Cart item", "userId", findUser.getId() + ", productId: " + productId));
        cartItemRepository.delete(cartItem);
        return new MessageResponse("Cart item deleted successfully");
    }
}
