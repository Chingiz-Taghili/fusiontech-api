package com.fusiontech.api.services.impls;

import com.fusiontech.api.dtos.product.ProductCreateDto;
import com.fusiontech.api.dtos.product.ProductUpdateDto;
import com.fusiontech.api.exceptions.ResourceAlreadyExistsException;
import com.fusiontech.api.exceptions.ResourceNotFoundException;
import com.fusiontech.api.models.*;
import com.fusiontech.api.payloads.ApiResponse;
import com.fusiontech.api.payloads.MessageResponse;
import com.fusiontech.api.repositories.BrandRepository;
import com.fusiontech.api.repositories.CategoryRepository;
import com.fusiontech.api.repositories.ProductRepository;
import com.fusiontech.api.repositories.SubcategoryRepository;
import com.fusiontech.api.services.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final CategoryRepository categoryRepository;
    private final SubcategoryRepository subcategoryRepository;
    private final BrandRepository brandRepository;

    public ProductServiceImpl(ProductRepository productRepository, ModelMapper modelMapper, CategoryRepository categoryRepository, SubcategoryRepository subcategoryRepository, BrandRepository brandRepository) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
        this.categoryRepository = categoryRepository;
        this.subcategoryRepository = subcategoryRepository;
        this.brandRepository = brandRepository;
    }

    @Transactional
    @Override
    public ApiResponse createProduct(ProductCreateDto createDto) {
        Optional<Product> optionalProduct = productRepository.findByName(createDto.getName());
        if (optionalProduct.isPresent()) {
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
        Optional<Product> sameProduct = productRepository.findByName(updateDto.getName());
        if (sameProduct.isPresent() && !sameProduct.get().getId().equals(id)) {
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
        return null;
    }

    @Transactional(readOnly = true)
    @Override
    public ApiResponse getProductById(Long id) {
        return null;
    }

    @Transactional(readOnly = true)
    @Override
    public ApiResponse getAllProducts(Integer pageNumber, Integer pageSize) {
        return null;
    }

    @Transactional(readOnly = true)
    @Override
    public ApiResponse getFilteredProducts(int price, int category, int brand, Integer pageNumber, Integer pageSize) {
        return null;
    }

    @Transactional(readOnly = true)
    @Override
    public ApiResponse getCatalogProducts(Long categoryId, Long subcategoryId, Integer pageNumber, Integer pageSize) {
        return null;
    }

    @Transactional(readOnly = true)
    @Override
    public ApiResponse getSearchProducts(String keyword, Integer pageNumber, Integer pageSize) {
        return null;
    }

    @Transactional(readOnly = true)
    @Override
    public ApiResponse getFeaturedProducts() {
        return null;
    }

    @Transactional(readOnly = true)
    @Override
    public ApiResponse getRelatedProducts(Long categoryId, Long productId) {
        return null;
    }

    @Transactional(readOnly = true)
    @Override
    public ApiResponse getOfferedProducts() {
        return null;
    }

    @Transactional(readOnly = true)
    @Override
    public ApiResponse getTotalProductCount() {
        return null;
    }

    @Transactional(readOnly = true)
    @Override
    public ApiResponse getCountByPriceRanges() {
        return null;
    }

    @Transactional
    @Override
    public ApiResponse updateProductRating(Long productId) {
        return null;
    }
}
