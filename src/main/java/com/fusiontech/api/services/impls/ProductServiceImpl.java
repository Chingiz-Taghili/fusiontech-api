package com.fusiontech.api.services.impls;

import com.fusiontech.api.dtos.product.ProductCreateDto;
import com.fusiontech.api.dtos.product.ProductDto;
import com.fusiontech.api.dtos.product.ProductUpdateDto;
import com.fusiontech.api.exceptions.ResourceAlreadyExistsException;
import com.fusiontech.api.exceptions.ResourceNotFoundException;
import com.fusiontech.api.models.*;
import com.fusiontech.api.payloads.ApiResponse;
import com.fusiontech.api.payloads.DataResponse;
import com.fusiontech.api.payloads.MessageResponse;
import com.fusiontech.api.payloads.Paged;
import com.fusiontech.api.repositories.*;
import com.fusiontech.api.services.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final CategoryRepository categoryRepository;
    private final SubcategoryRepository subcategoryRepository;
    private final BrandRepository brandRepository;
    private final ReviewRepository reviewRepository;

    public ProductServiceImpl(ProductRepository productRepository, ModelMapper modelMapper, CategoryRepository categoryRepository, SubcategoryRepository subcategoryRepository, BrandRepository brandRepository, ReviewRepository reviewRepository) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
        this.categoryRepository = categoryRepository;
        this.subcategoryRepository = subcategoryRepository;
        this.brandRepository = brandRepository;
        this.reviewRepository = reviewRepository;
    }

    @Transactional
    @Override
    public ApiResponse createProduct(ProductCreateDto createDto) {
        if (productRepository.existsByName(createDto.getName())) {
            throw new ResourceAlreadyExistsException("Product", "name", createDto.getName());
        }
        Product newProduct = modelMapper.map(createDto, Product.class);

        if (createDto.getBrandId() != null) {
            Brand brand = brandRepository.findById(createDto.getBrandId()).orElseThrow(
                    () -> new ResourceNotFoundException("Brand", "id", createDto.getBrandId()));
            newProduct.setBrand(brand);
        }
        Category category = categoryRepository.findById(createDto.getCategoryId()).orElseThrow(
                () -> new ResourceNotFoundException("Category", "id", createDto.getCategoryId()));
        Subcategory subcategory = subcategoryRepository.findById(createDto.getSubcategoryId()).orElseThrow(
                () -> new ResourceNotFoundException("Subcategory", "id", createDto.getSubcategoryId()));
        newProduct.setCategory(category);
        newProduct.setSubcategory(subcategory);

        newProduct.setImages(createDto.getImageUrls().stream().map(url -> {
            Image image = new Image();
            image.setUrl(url);
            image.setProduct(newProduct);
            return image;
        }).toList());

        productRepository.save(newProduct);
        return new MessageResponse("Product created successfully");
    }

    @Transactional
    @Override
    public ApiResponse updateProduct(Long id, ProductUpdateDto updateDto) {
        Product findProduct = productRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Product", "id", id));
        if (productRepository.existsByName(updateDto.getName()) && !findProduct.getName().equals(updateDto.getName())) {
            throw new ResourceAlreadyExistsException("Product", "name", updateDto.getName());
        }

        findProduct.setName(updateDto.getName());
        findProduct.setPrice(updateDto.getPrice());
        findProduct.setDescription(updateDto.getDescription());
        findProduct.setMoreDetail(updateDto.getMoreDetail());
        findProduct.setDiscountPrice(updateDto.getDiscountPrice());
        findProduct.setDiscountDate(updateDto.getDiscountDate());
        findProduct.setFeatured(updateDto.isFeatured());
        findProduct.setOffered(updateDto.isOffered());

        List<Image> updatedImages = updateDto.getImageUrls().stream().map(url -> {
            Image image = new Image();
            image.setUrl(url);
            image.setProduct(findProduct);
            return image;
        }).toList();
        // Kolleksiyanı yeniləyir
        findProduct.getImages().clear(); // Köhnə şəkilləri silmək
        findProduct.getImages().addAll(updatedImages); // Yeni şəkilləri əlavə etmək

        if (updateDto.getBrandId() != null) {
            findProduct.setBrand(brandRepository.findById(updateDto.getBrandId()).orElseThrow(
                    () -> new ResourceNotFoundException("Brand", "id", updateDto.getBrandId())));
        }
        findProduct.setCategory(categoryRepository.findById(updateDto.getCategoryId()).orElseThrow(
                () -> new ResourceNotFoundException("Category", "id", updateDto.getCategoryId())));
        findProduct.setSubcategory(subcategoryRepository.findById(updateDto.getSubcategoryId()).orElseThrow(
                () -> new ResourceNotFoundException("Subcategory", "id", updateDto.getSubcategoryId())));

        productRepository.save(findProduct);
        return new MessageResponse("Product updated successfully");
    }

    @Transactional
    @Override
    public ApiResponse deleteProduct(Long id) {
        Product findProduct = productRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Product", "id", id));
        productRepository.delete(findProduct);
        return new MessageResponse("Product deleted successfully");
    }

    @Transactional(readOnly = true)
    @Override
    public ApiResponse getProductById(Long id) {
        Product findProduct = productRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Product", "id", id));
        ProductDto product = modelMapper.map(findProduct, ProductDto.class);

        if (findProduct.getImages() != null && !findProduct.getImages().isEmpty()) {
            product.setImageUrl(findProduct.getImages().get(0).getUrl());
        }
        return new DataResponse<>(product);
    }

    @Transactional(readOnly = true)
    @Override
    public ApiResponse getAllProducts(Integer pageNumber, Integer pageSize) {
        pageNumber = (pageNumber == null || pageNumber < 1) ? 1 : pageNumber;
        pageSize = (pageSize == null || pageSize < 1) ? 10 : pageSize;
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, Sort.by("id"));
        Page<Product> findProducts = productRepository.findAll(pageable);
        if (findProducts.getContent().isEmpty()) {
            return new MessageResponse("No products available");
        }

        List<ProductDto> products = findProducts.getContent().stream().map(entity -> {
            ProductDto dto = modelMapper.map(entity, ProductDto.class);

            if (entity.getImages() != null && !entity.getImages().isEmpty()) {
                dto.setImageUrl(entity.getImages().get(0).getUrl());
            }
            return dto;
        }).toList();
        return new DataResponse<>(new Paged<>(products, pageNumber, findProducts.getTotalPages()));
    }

    @Transactional(readOnly = true)
    @Override
    public ApiResponse getFilteredProducts(Integer price, Long categoryId, Long brandId, Integer pageNumber, Integer pageSize) {
        pageNumber = (pageNumber == null || pageNumber < 1) ? 1 : pageNumber;
        pageSize = (pageSize == null || pageSize < 1) ? 10 : pageSize;
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, Sort.by("id"));
        Page<Product> findProducts;

        int priceMin = 0;
        int priceMax = Integer.MAX_VALUE;
        switch (price) {
            case 1:
                priceMax = 100;
                break;
            case 2:
                priceMin = 100;
                priceMax = 1000;
                break;
            case 3:
                priceMin = 1000;
                priceMax = 2000;
                break;
            case 4:
                priceMin = 2000;
                priceMax = 3000;
                break;
            case 5:
                priceMin = 3000;
                break;
        }

        if (categoryId == 0 && brandId == 0) {
            findProducts = productRepository.findByPriceBetween(priceMin, priceMax, pageable);
        } else if (categoryId != 0 && brandId == 0) {
            findProducts = productRepository.findByPriceBetweenAndCategoryId(priceMin, priceMax, categoryId, pageable);
        } else if (categoryId == 0) {
            findProducts = productRepository.findByPriceBetweenAndBrandId(priceMin, priceMax, brandId, pageable);
        } else {
            findProducts = productRepository.findByPriceBetweenAndCategoryIdAndBrandId(
                    priceMin, priceMax, categoryId, brandId, pageable);
        }

        if (findProducts.getContent().isEmpty()) {
            return new MessageResponse("No products found for the selected filter(s).");
        }

        List<ProductDto> products = findProducts.getContent().stream().map(entity -> {
            ProductDto dto = modelMapper.map(entity, ProductDto.class);

            if (entity.getImages() != null && !entity.getImages().isEmpty()) {
                dto.setImageUrl(entity.getImages().get(0).getUrl());
            }
            return dto;
        }).toList();
        return new DataResponse<>(new Paged<>(products, pageNumber, findProducts.getTotalPages()));
    }

    @Transactional(readOnly = true)
    @Override
    public ApiResponse getCatalogProducts(Long categoryId, Long subcategoryId, Integer pageNumber, Integer pageSize) {
        pageNumber = (pageNumber == null || pageNumber < 1) ? 1 : pageNumber;
        pageSize = (pageSize == null || pageSize < 1) ? 10 : pageSize;
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, Sort.by("id"));
        Page<Product> findProducts;
        if (categoryId != 0) {
            findProducts = productRepository.findByCategoryId(categoryId, pageable);
        } else {
            findProducts = productRepository.findBySubcategoryId(subcategoryId, pageable);
        }

        if (findProducts.getContent().isEmpty()) {
            return new MessageResponse("No products found for the selected filter(s).");
        }

        List<ProductDto> products = findProducts.getContent().stream().map(entity -> {
            ProductDto dto = modelMapper.map(entity, ProductDto.class);

            if (entity.getImages() != null && !entity.getImages().isEmpty()) {
                dto.setImageUrl(entity.getImages().get(0).getUrl());
            }
            return dto;
        }).toList();
        return new DataResponse<>(new Paged<>(products, pageNumber, findProducts.getTotalPages()));
    }

    @Transactional(readOnly = true)
    @Override
    public ApiResponse getSearchProducts(String keyword, Integer pageNumber, Integer pageSize) {
        pageNumber = (pageNumber == null || pageNumber < 1) ? 1 : pageNumber;
        pageSize = (pageSize == null || pageSize < 1) ? 10 : pageSize;
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, Sort.by("id"));
        Page<Product> findProducts = productRepository.findByKeywordInColumnsIgnoreCase(keyword, pageable);
        if (findProducts.getContent().isEmpty()) {
            return new MessageResponse("No products found for the keyword: " + keyword);
        }

        List<ProductDto> products = findProducts.getContent().stream().map(entity -> {
            ProductDto dto = modelMapper.map(entity, ProductDto.class);

            if (entity.getImages() != null && !entity.getImages().isEmpty()) {
                dto.setImageUrl(entity.getImages().get(0).getUrl());
            }
            return dto;
        }).toList();
        return new DataResponse<>(new Paged<>(products, pageNumber, findProducts.getTotalPages()));
    }

    @Transactional(readOnly = true)
    @Override
    public ApiResponse getFeaturedProducts() {
        List<Product> findProducts = productRepository.findByFeaturedTrue();
        if (findProducts.isEmpty()) {
            return new MessageResponse("No featured products found.");
        }

        List<ProductDto> products = findProducts.stream().map(entity -> {
            ProductDto dto = modelMapper.map(entity, ProductDto.class);

            if (entity.getImages() != null && !entity.getImages().isEmpty()) {
                dto.setImageUrl(entity.getImages().get(0).getUrl());
            }
            return dto;
        }).toList();
        return new DataResponse<>(products);
    }

    @Transactional(readOnly = true)
    @Override
    public ApiResponse getRelatedProducts(Long categoryId, Long productId) {
        List<Product> findProducts = productRepository.findByCategoryId(categoryId)
                .stream().filter(product -> !product.getId().equals(productId)).limit(5).toList();
        if (findProducts.isEmpty()) {
            return new MessageResponse("No related products found for category id: " + categoryId);
        }

        List<ProductDto> products = findProducts.stream().map(entity -> {
            ProductDto dto = modelMapper.map(entity, ProductDto.class);

            if (entity.getImages() != null && !entity.getImages().isEmpty()) {
                dto.setImageUrl(entity.getImages().get(0).getUrl());
            }
            return dto;
        }).toList();
        return new DataResponse<>(products);
    }

    @Transactional(readOnly = true)
    @Override
    public ApiResponse getOfferedProducts() {
        List<Product> findProducts = productRepository.findByOfferedTrue();
        if (findProducts.isEmpty()) {
            return new MessageResponse("No offered products found.");
        }

        List<ProductDto> products = findProducts.stream().map(entity -> {
            ProductDto dto = modelMapper.map(entity, ProductDto.class);

            double percent = (double) Math.round(
                    (entity.getPrice() - entity.getDiscountPrice()) / entity.getPrice() * 100 * 100) / 100;
            dto.setDiscountPercent(percent);
            if (entity.getImages() != null && !entity.getImages().isEmpty()) {
                dto.setImageUrl(entity.getImages().get(0).getUrl());
            }
            return dto;
        }).toList();
        return new DataResponse<>(products);
    }

    @Transactional(readOnly = true)
    @Override
    public ApiResponse getTotalCount() {
        return new DataResponse<>(productRepository.count());
    }

    @Transactional(readOnly = true)
    @Override
    public ApiResponse getCountByPriceRanges() {
        Map<Integer, Long> countByPriceMap = new HashMap<>();
        countByPriceMap.put(1, productRepository.countByPriceLessThanEqual(100));
        countByPriceMap.put(2, productRepository.countByPriceBetween(100, 1000));
        countByPriceMap.put(3, productRepository.countByPriceBetween(1000, 2000));
        countByPriceMap.put(4, productRepository.countByPriceBetween(2000, 3000));
        countByPriceMap.put(5, productRepository.countByPriceGreaterThan(3000));
        return new DataResponse<>(countByPriceMap);
    }

    @Transactional
    @Override
    public ApiResponse updateProductRating(Long productId) {
        Product findProduct = productRepository.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException("Product", "id", productId));
        List<Review> reviews = reviewRepository.findByProductId(productId);
        Double newRating = reviews.isEmpty() ? null : reviews.stream()
                .mapToDouble(Review::getRating).average().orElse(0.0);
        findProduct.setRating(newRating);
        productRepository.save(findProduct);
        return new MessageResponse("Product rating updated successfully.");
    }
}
