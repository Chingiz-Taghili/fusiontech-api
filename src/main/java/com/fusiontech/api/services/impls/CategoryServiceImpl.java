package com.fusiontech.api.services.impls;

import com.fusiontech.api.dtos.category.CategoryCreateDto;
import com.fusiontech.api.dtos.category.CategoryDto;
import com.fusiontech.api.dtos.category.CategoryUpdateDto;
import com.fusiontech.api.exceptions.ResourceAlreadyExistsException;
import com.fusiontech.api.exceptions.ResourceNotFoundException;
import com.fusiontech.api.models.Category;
import com.fusiontech.api.models.Subcategory;
import com.fusiontech.api.models.SubcategoryName;
import com.fusiontech.api.payloads.ApiResponse;
import com.fusiontech.api.payloads.DataResponse;
import com.fusiontech.api.payloads.MessageResponse;
import com.fusiontech.api.repositories.CategoryRepository;
import com.fusiontech.api.services.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional(readOnly = true)
    @Override
    public ApiResponse getAllCategories() {
        List<Category> findCategories = categoryRepository.findAll();
        if (findCategories.isEmpty()) {
            return new MessageResponse("No categories available");
        }
        List<CategoryDto> categories = findCategories.stream().map(
                category -> modelMapper.map(category, CategoryDto.class)).toList();
        return new DataResponse<>(categories);
    }

    @Transactional(readOnly = true)
    @Override
    public ApiResponse getSearchCategories(String keyword) {
        List<Category> findCategories = categoryRepository.findByNameContainingIgnoreCase(keyword);
        if (findCategories.isEmpty()) {
            return new MessageResponse("No categories found for the keyword:" + keyword);
        }
        List<CategoryDto> categories = findCategories.stream().map(
                category -> modelMapper.map(category, CategoryDto.class)).toList();
        return new DataResponse<>(categories);
    }

    @Transactional(readOnly = true)
    @Override
    public ApiResponse getCategoryById(Long id) {
        Category findCategory = categoryRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Category", "id", id));
        CategoryDto category = modelMapper.map(findCategory, CategoryDto.class);
        return new DataResponse<>(category);
    }

    @Transactional
    @Override
    public ApiResponse createCategory(CategoryCreateDto createDto) {
        if (categoryRepository.existsByName(createDto.getName())) {
            throw new ResourceAlreadyExistsException("Category", "name", createDto.getName());
        }
        Category category = modelMapper.map(createDto, Category.class);
        List<Subcategory> subcategories = new ArrayList<>();
        for (String name : createDto.getSubcategoryNames()) {
            SubcategoryName subcategoryName = new SubcategoryName();
            subcategoryName.setName(name);
            Subcategory subcategory = new Subcategory();
            subcategory.setSubcategoryName(subcategoryName);
            subcategory.setCategory(category);
            subcategories.add(subcategory);
        }
        category.setSubcategories(subcategories);
        categoryRepository.save(category);
        return new MessageResponse("Category created successfully");
    }

    @Transactional
    @Override
    public ApiResponse updateCategory(Long id, CategoryUpdateDto updateDto) {
        Category findCategory = categoryRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Category", "id", id));
        if (categoryRepository.existsByName(updateDto.getName()) && !findCategory.getName().equals(updateDto.getName())) {
            throw new ResourceAlreadyExistsException("Category", "name", updateDto.getName());
        }
        findCategory.setName(updateDto.getName());
        findCategory.setImageUrl(updateDto.getImageUrl());

        List<Subcategory> newSubcategories = new ArrayList<>();
        for (String name : updateDto.getSubcategoriesName()) {
            SubcategoryName subcategoryName = new SubcategoryName();
            subcategoryName.setName(name);
            Subcategory subcategory = new Subcategory();
            subcategory.setSubcategoryName(subcategoryName);
            subcategory.setCategory(findCategory);
            newSubcategories.add(subcategory);
        }
        // Kolleksiyanı yeniləyir
        findCategory.getSubcategories().clear(); // Köhnə alt kateqoriyaları silmək
        findCategory.getSubcategories().addAll(newSubcategories); // Yeniləri əlavə etmək

        categoryRepository.save(findCategory);
        return new MessageResponse("Category updated successfully");
    }

    @Transactional
    @Override
    public ApiResponse deleteCategory(Long id) {
        Category findCategory = categoryRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Category", "id", id));
        categoryRepository.delete(findCategory);
        return new MessageResponse("Category deleted successfully");
    }

    @Transactional(readOnly = true)
    @Override
    public ApiResponse getTotalCount() {
        return new DataResponse<>(categoryRepository.count());
    }
}
