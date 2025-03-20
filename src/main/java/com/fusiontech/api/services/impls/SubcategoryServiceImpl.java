package com.fusiontech.api.services.impls;

import com.fusiontech.api.dtos.common.SubcategoryDto;
import com.fusiontech.api.exceptions.ResourceNotFoundException;
import com.fusiontech.api.models.Subcategory;
import com.fusiontech.api.payloads.ApiResponse;
import com.fusiontech.api.payloads.DataResponse;
import com.fusiontech.api.payloads.MessageResponse;
import com.fusiontech.api.repositories.SubcategoryRepository;
import com.fusiontech.api.services.SubcategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SubcategoryServiceImpl implements SubcategoryService {
    private final SubcategoryRepository subcategoryRepository;
    private final ModelMapper modelMapper;

    public SubcategoryServiceImpl(SubcategoryRepository subcategoryRepository, ModelMapper modelMapper) {
        this.subcategoryRepository = subcategoryRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional(readOnly = true)
    @Override
    public ApiResponse getAllSubcategories() {
        List<Subcategory> findSubcategories = subcategoryRepository.findAll();
        if (findSubcategories.isEmpty()) {
            return new MessageResponse("No subcategories available");
        }
        List<SubcategoryDto> subcategories = findSubcategories.stream().map(subcategory ->
                modelMapper.map(subcategory, SubcategoryDto.class)).toList();
        return new DataResponse<>(subcategories);
    }

    @Transactional(readOnly = true)
    @Override
    public ApiResponse getSubcategoryById(Long id) {
        Subcategory findSubcategory = subcategoryRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Subcategory", "id", id));
        SubcategoryDto subcategory = modelMapper.map(findSubcategory, SubcategoryDto.class);
        return new DataResponse<>(subcategory);
    }
}
