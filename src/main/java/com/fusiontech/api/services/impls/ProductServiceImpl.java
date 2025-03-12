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
import com.fusiontech.api.repositories.BrandRepository;
import com.fusiontech.api.repositories.CategoryRepository;
import com.fusiontech.api.repositories.ProductRepository;
import com.fusiontech.api.repositories.SubcategoryRepository;
import com.fusiontech.api.services.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;

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
        if (findProduct.getDiscountDate() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
            product.setFormattedDiscountDate(findProduct.getDiscountDate().format(formatter));
        }
        return new DataResponse<>(product);
    }

    @Transactional(readOnly = true)
    @Override
    public ApiResponse getAllProducts(Integer pageNumber, Integer pageSize) {
        pageNumber = (pageNumber == null || pageNumber < 1) ? 1 : pageNumber;
        pageSize = (pageSize == null || pageSize < 1) ? 1 : pageSize;
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, Sort.by("id"));

        Page<Product> findProducts = productRepository.findAll(pageable);
        if (findProducts.getContent().isEmpty()) {
            return new MessageResponse("No products available");
        }
        List<ProductDto> products = findProducts.getContent().stream().map(
                product -> modelMapper.map(product, ProductDto.class)).toList();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        for (int i = 0; i < products.size(); i++) {
            if (findProducts.getContent().get(i).getDiscountDate() != null) {
                products.get(i).setFormattedDiscountDate(findProducts.getContent().get(i)
                        .getDiscountDate().format(formatter));
            }
            if (findProducts.getContent().get(i).getImages() != null && !findProducts.getContent().get(i).getImages().isEmpty()) {
                products.get(i).setImageUrl(findProducts.getContent().get(i).getImages().get(0).getUrl());
            }
        }
        Paged<ProductDto> pagedProducts = new Paged<>(products, pageNumber, findProducts.getTotalPages());
        return new DataResponse<>(pagedProducts);
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
