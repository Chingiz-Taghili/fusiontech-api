package com.fusiontech.api.services;

import com.fusiontech.api.dtos.slider.SliderCreateDto;
import com.fusiontech.api.dtos.slider.SliderUpdateDto;
import com.fusiontech.api.payloads.ApiResponse;

public interface SliderService {

    ApiResponse getAllSliders();

    ApiResponse getSearchSliders(String keyword);

    ApiResponse getSliderById(Long id);

    ApiResponse getTotalCount();

    ApiResponse createSlider(SliderCreateDto createDto);

    ApiResponse updateSlider(Long id, SliderUpdateDto updateDto);

    ApiResponse deleteSlider(Long id);
}
