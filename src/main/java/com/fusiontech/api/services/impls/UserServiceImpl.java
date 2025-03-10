package com.fusiontech.api.services.impls;

import com.fusiontech.api.dtos.cart.CartDto;
import com.fusiontech.api.dtos.cart.CartItemDto;
import com.fusiontech.api.dtos.product.ProductDto;
import com.fusiontech.api.dtos.user.UserCreateDto;
import com.fusiontech.api.dtos.user.UserDto;
import com.fusiontech.api.dtos.user.UserRegisterDto;
import com.fusiontech.api.dtos.user.UserUpdateDto;
import com.fusiontech.api.exceptions.ResourceAlreadyExistsException;
import com.fusiontech.api.exceptions.ResourceNotFoundException;
import com.fusiontech.api.models.*;
import com.fusiontech.api.payloads.ApiResponse;
import com.fusiontech.api.payloads.DataResponse;
import com.fusiontech.api.payloads.MessageResponse;
import com.fusiontech.api.payloads.Paged;
import com.fusiontech.api.repositories.RoleRepository;
import com.fusiontech.api.repositories.UserRepository;
import com.fusiontech.api.services.UserService;
import org.apache.catalina.User;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder encoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, ModelMapper modelMapper, BCryptPasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;
        this.encoder = encoder;
    }

    @Transactional(readOnly = true)
    @Override
    public ApiResponse getAllUsers(Integer pageNumber, Integer pageSize) {
        pageNumber = (pageNumber == null || pageNumber < 1) ? 1 : pageNumber;
        pageSize = (pageSize == null || pageSize < 1) ? 1 : pageSize;
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, Sort.by("id"));

        Page<UserEntity> findUsers = userRepository.findAll(pageable);
        if (findUsers.getContent().isEmpty()) {
            return new MessageResponse("No users available");
        }
        List<UserDto> users = findUsers.getContent().stream().map(
                user -> modelMapper.map(user, UserDto.class)).toList();

        Paged<UserDto> pagedUsers = new Paged<>(users, pageNumber, findUsers.getTotalPages());
        return new DataResponse<>(pagedUsers);
    }

    @Transactional(readOnly = true)
    @Override
    public ApiResponse getSearchUsers(String keyword, Integer pageNumber, Integer pageSize) {
        pageNumber = (pageNumber == null || pageNumber < 1) ? 1 : pageNumber;
        pageSize = (pageSize == null || pageSize < 1) ? 1 : pageSize;
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, Sort.by("id"));

        Page<UserEntity> findUsers = userRepository.findByKeywordInColumnsIgnoreCase(keyword, pageable);
        if (findUsers.getContent().isEmpty()) {
            return new MessageResponse("No users found for the keyword: " + keyword);
        }
        List<UserDto> users = findUsers.getContent().stream().map(
                user -> modelMapper.map(user, UserDto.class)).toList();
        Paged<UserDto> pagedUsers = new Paged<>(users, pageNumber, findUsers.getTotalPages());
        return new DataResponse<>(pagedUsers);
    }

    @Transactional(readOnly = true)
    @Override
    public ApiResponse getUserByEmail(String email) {
        Optional<UserEntity> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            throw new ResourceNotFoundException("User", "email", email);
        }
        UserDto user = modelMapper.map(optionalUser.get(), UserDto.class);
        return new DataResponse<>(user);
    }

    @Transactional(readOnly = true)
    @Override
    public ApiResponse getUserById(Long id) {
        UserEntity findUser = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", id));
        UserDto user = modelMapper.map(findUser, UserDto.class);
        return new DataResponse<>(user);
    }

    @Transactional
    @Override
    public ApiResponse createUser(UserCreateDto createDto) {
        Optional<UserEntity> optionalUser = userRepository.findByEmail(createDto.getEmail());
        if (optionalUser.isPresent()) {
            throw new ResourceAlreadyExistsException("User", "email", createDto.getEmail());
        }
        UserEntity newUser = modelMapper.map(createDto, UserEntity.class);
        String password = encoder.encode(createDto.getPassword());
        newUser.setPassword(password);

        if (createDto.getImage() == null || createDto.getImage().isEmpty()) {
            newUser.setImage("/default-profile-picture.svg");
        }
        if (createDto.getRoles() == null || createDto.getRoles().isEmpty()) {
            Role role = roleRepository.findByName("ROLE_USER").orElseGet(() ->
            {
                Role newRole = new Role();
                newRole.setName("ROLE_USER");
                return roleRepository.save(newRole);
            });
            newUser.setRoles(Collections.singletonList(role));
        }

        userRepository.save(newUser);
        return new MessageResponse("User created successfully");
    }

    @Transactional
    @Override
    public ApiResponse registerUser(UserRegisterDto registerDto) {
        Optional<UserEntity> optionalUser = userRepository.findByEmail(registerDto.getEmail());
        if (optionalUser.isPresent()) {
            throw new ResourceAlreadyExistsException("User", "email", registerDto.getEmail());
        }
        UserEntity newUser = modelMapper.map(registerDto, UserEntity.class);
        String password = encoder.encode(registerDto.getPassword());
        newUser.setPassword(password);

        if (registerDto.getImage() == null || registerDto.getImage().isEmpty()) {
            newUser.setImage("/default-profile-picture.svg");
        }

        Role role = roleRepository.findByName("ROLE_USER").orElseGet(() ->
        {
            Role newRole = new Role();
            newRole.setName("ROLE_USER");
            return roleRepository.save(newRole);
        });
        newUser.setRoles(Collections.singletonList(role));

//        String confirmToken = String.valueOf(UUID.randomUUID());
//        user.setConfirmationToken(token);
        userRepository.save(newUser);

//        emailService.sendConfirmEmail(registerDto.getEmail(), token);
        return new MessageResponse("User registered successfully");
    }

    @Transactional
    @Override
    public ApiResponse updateUser(Long id, UserUpdateDto updateDto) {
        UserEntity findUser = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", id));
        Optional<UserEntity> sameUser = userRepository.findByEmail(updateDto.getEmail());
        if (sameUser.isPresent() && !sameUser.get().getId().equals(id)) {
            throw new ResourceAlreadyExistsException("User", "email", updateDto.getEmail());
        }
        findUser.setName(updateDto.getName());
        findUser.setSurname(updateDto.getSurname());
        findUser.setBirthdate(updateDto.getBirthdate());
        findUser.setEmail(updateDto.getEmail());
        findUser.setGender(updateDto.getGender());
        if (updateDto.getImage() != null && !updateDto.getImage().isEmpty()) {
            findUser.setImage(updateDto.getImage());
        } else {
            findUser.setImage("/default-profile-picture.svg");
        }
        if (updateDto.getPassword() != null && !updateDto.getPassword().isEmpty()) {
            String password = encoder.encode(updateDto.getPassword());
            findUser.setPassword(password);
        }
        userRepository.save(findUser);
        return new MessageResponse("User updated successfully");
    }

    @Transactional
    @Override
    public ApiResponse deleteUser(Long id) {
        UserEntity findUser = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", id));
        userRepository.delete(findUser);
        return new MessageResponse("User deleted successfully");
    }

    @Transactional(readOnly = true)
    @Override
    public ApiResponse getUserCart(String userEmail) {
        UserEntity findUser = userRepository.findByEmail(userEmail).orElseThrow(
                () -> new ResourceNotFoundException("User", "email", userEmail));

        List<CartItemDto> cartItems = findUser.getCartItems().stream().map(item -> {
            Product product = item.getProduct();
            return new CartItemDto(item.getId(), product.getName(), product.getPrice(), product.getDiscountPrice(),
                    !product.getImages().isEmpty() ? product.getImages().get(0).getUrl() : null, item.getQuantity(),
                    (double) Math.round(product.getDiscountPrice() * item.getQuantity() * 100) / 100);
        }).toList();

        double subtotal = (double) Math.round(cartItems.stream().mapToDouble(c -> c.getDiscountPrice() * c.getQuantity()).sum() * 100) / 100;
        double shipping = subtotal > 0 ? 19.99 : 0;
        double total = (double) Math.round((subtotal + shipping) * 100) / 100;

        CartDto userCart = new CartDto(cartItems, subtotal, shipping, total);
        return new DataResponse<>(userCart);
    }

    @Transactional(readOnly = true)
    @Override
    public ApiResponse getUserCartSize(String userEmail) {
        UserEntity findUser = userRepository.findByEmail(userEmail).orElseThrow(
                () -> new ResourceNotFoundException("User", "email", userEmail));
        return new DataResponse<>(findUser.getCartItems().size());
    }

    @Transactional(readOnly = true)
    @Override
    public ApiResponse getUserFavoriteProducts(String userEmail, Integer pageNumber, Integer pageSize) {
        UserEntity findUser = userRepository.findByEmail(userEmail).orElseThrow(
                () -> new ResourceNotFoundException("User", "email", userEmail));

        pageNumber = (pageNumber == null || pageNumber < 1) ? 1 : pageNumber;
        pageSize = (pageSize == null || pageSize < 1) ? 1 : pageSize;

        List<Product> findProducts = findUser.getFavorites().stream().map(Favorite::getProduct).toList();
        int totalCount = findProducts.size();
        int start = (pageNumber - 1) * pageSize;
        int end = Math.min(start + pageSize, totalCount);

        if (start >= totalCount) {
            throw new IllegalArgumentException("The combination of page number and " +
                    "page size exceeds the total product count. Total product count: " + totalCount);
        }

        List<ProductDto> favoriteProducts = findProducts.subList(start, end).stream().map(product -> {
            ProductDto dto = modelMapper.map(product, ProductDto.class);
            List<Image> images = product.getImages();
            dto.setImage(!images.isEmpty() ? images.get(0).getUrl() : null);
            return dto;
        }).toList();

        int totalPages = (int) Math.ceil((double) totalCount / pageSize);

        Paged<ProductDto> products = new Paged<>(favoriteProducts, pageNumber, totalPages);
        return new DataResponse<>(products);
    }

    @Transactional(readOnly = true)
    @Override
    public ApiResponse getUserFavoriteSize(String userEmail) {
        UserEntity findUser = userRepository.findByEmail(userEmail).orElseThrow(
                () -> new ResourceNotFoundException("User", "email", userEmail));
        return new DataResponse<>(findUser.getFavorites().size());
    }

    @Transactional(readOnly = true)
    @Override
    public ApiResponse getTotalCount() {
        return new DataResponse<>(userRepository.count());
    }

//    @Override
//    public ApiResponse confirmUser(String email, String token) {
//        UserEntity findUser = userRepository.findByEmail(email);
//        if (findUser == null) {
//            return new ErrorResponse("User don't exist");
//        }
//        if (!findUser.getConfirmationToken().equals(token)) {
//            return new ErrorResponse("Token is expired");
//        }
//        findUser.setEmailConfirmed(true);
//        userRepository.save(findUser);
//        return new MessageResponse("Email is activated successfully");
//    }
}
