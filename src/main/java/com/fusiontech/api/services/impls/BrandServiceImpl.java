package com.fusiontech.api.services.impls;

import com.fusiontech.api.dtos.brand.BrandCreateDto;
import com.fusiontech.api.dtos.brand.BrandDto;
import com.fusiontech.api.dtos.brand.BrandUpdateDto;
import com.fusiontech.api.exceptions.ResourceAlreadyExistsException;
import com.fusiontech.api.exceptions.ResourceNotFoundException;
import com.fusiontech.api.models.Brand;
import com.fusiontech.api.payloads.ApiResponse;
import com.fusiontech.api.payloads.DataResponse;
import com.fusiontech.api.payloads.MessageResponse;
import com.fusiontech.api.repositories.BrandRepository;
import com.fusiontech.api.services.BrandService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BrandServiceImpl implements BrandService {
    private final BrandRepository brandRepository;
    private final ModelMapper modelMapper;

    public BrandServiceImpl(BrandRepository brandRepository, ModelMapper modelMapper) {
        this.brandRepository = brandRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional(readOnly = true)
    @Override
    public ApiResponse getAllBrands() {
        List<Brand> findBrands = brandRepository.findAll();
        if (findBrands.isEmpty()) {
            return new MessageResponse("No brands available");
        }
        List<BrandDto> brands = findBrands.stream().map(
                brand -> modelMapper.map(brand, BrandDto.class)).toList();
        return new DataResponse<>(brands);
    }

    @Transactional(readOnly = true)
    @Override
    public ApiResponse getBrandById(Long id) {
        Brand findBrand = brandRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Brand", "id", id));
        BrandDto brand = modelMapper.map(findBrand, BrandDto.class);
        return new DataResponse<>(brand);
    }

    @Transactional(readOnly = true)
    @Override
    public ApiResponse getSearchBrands(String keyword) {
        List<Brand> findBrands = brandRepository.findByNameContainingIgnoreCase(keyword);
        if (findBrands.isEmpty()) {
            return new MessageResponse("No brands found for the keyword:" + keyword);
        }
        List<BrandDto> brands = findBrands.stream().map(
                brand -> modelMapper.map(brand, BrandDto.class)).toList();
        return new DataResponse<>(brands);
    }

    @Transactional(readOnly = true)
    @Override
    public ApiResponse getTotalCount() {
        return new DataResponse<>(brandRepository.count());
    }

    @Transactional
    @Override
    public ApiResponse createBrand(BrandCreateDto createDto) {
        if (brandRepository.existsByName(createDto.getName())) {
            throw new ResourceAlreadyExistsException("Brand", "name", createDto.getName());
        }
        Brand brand = modelMapper.map(createDto, Brand.class);
        brandRepository.save(brand);
        return new MessageResponse("Brand created successfully");
    }

    @Transactional
    @Override
    public ApiResponse updateBrand(Long id, BrandUpdateDto updateDto) {
        Brand findBrand = brandRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Brand", "id", id));
        if (brandRepository.existsByName(updateDto.getName())
                && !findBrand.getName().equals(updateDto.getName())) {
            throw new ResourceAlreadyExistsException("Brand", "name", updateDto.getName());
        }
        findBrand.setName(updateDto.getName());
        brandRepository.save(findBrand);
        return new MessageResponse("Brand updated successfully");
    }

    @Transactional
    @Override
    public ApiResponse deleteBrand(Long id) {
        Brand findBrand = brandRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Brand", "id", id));
        brandRepository.delete(findBrand);
        return new MessageResponse("Brand deleted successfully");
    }
}
