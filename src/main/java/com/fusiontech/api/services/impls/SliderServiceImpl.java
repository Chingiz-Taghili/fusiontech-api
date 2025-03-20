package com.fusiontech.api.services.impls;

import com.fusiontech.api.dtos.slider.SliderCreateDto;
import com.fusiontech.api.dtos.slider.SliderDto;
import com.fusiontech.api.dtos.slider.SliderUpdateDto;
import com.fusiontech.api.exceptions.ResourceNotFoundException;
import com.fusiontech.api.models.Slider;
import com.fusiontech.api.payloads.ApiResponse;
import com.fusiontech.api.payloads.DataResponse;
import com.fusiontech.api.payloads.MessageResponse;
import com.fusiontech.api.repositories.SliderRepository;
import com.fusiontech.api.services.SliderService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SliderServiceImpl implements SliderService {

    private final SliderRepository sliderRepository;
    private final ModelMapper modelMapper;

    public SliderServiceImpl(SliderRepository sliderRepository, ModelMapper modelMapper) {
        this.sliderRepository = sliderRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional(readOnly = true)
    @Override
    public ApiResponse getAllSliders() {
        List<Slider> findSliders = sliderRepository.findAll();
        if (findSliders.isEmpty()) {
            return new MessageResponse("No sliders available");
        }
        List<SliderDto> sliders = findSliders.stream().map(slider ->
                modelMapper.map(slider, SliderDto.class)).toList();
        return new DataResponse<>(sliders);
    }

    @Transactional(readOnly = true)
    @Override
    public ApiResponse getSearchSliders(String keyword) {
        List<Slider> findSliders = sliderRepository.findByKeywordInColumnsIgnoreCase(keyword);
        if (findSliders.isEmpty()) {
            return new MessageResponse("No sliders found for the keyword: " + keyword);
        }
        List<SliderDto> sliders = findSliders.stream().map(slider ->
                modelMapper.map(slider, SliderDto.class)).toList();
        return new DataResponse<>(sliders);
    }

    @Transactional(readOnly = true)
    @Override
    public ApiResponse getSliderById(Long id) {
        Slider findSlider = sliderRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Slider", "id", id));
        SliderDto slider = modelMapper.map(findSlider, SliderDto.class);
        return new DataResponse<>(slider);
    }

    @Transactional(readOnly = true)
    @Override
    public ApiResponse getTotalCount() {
        return new DataResponse<>(sliderRepository.count());
    }

    @Transactional
    @Override
    public ApiResponse createSlider(SliderCreateDto createDto) {
        Slider slider = modelMapper.map(createDto, Slider.class);
        sliderRepository.save(slider);
        return new MessageResponse("Slider created successfully");
    }

    @Transactional
    @Override
    public ApiResponse updateSlider(Long id, SliderUpdateDto updateDto) {
        Slider findSlider = sliderRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Slider", "id", id));
        findSlider.setTitle(updateDto.getTitle());
        findSlider.setDescription(updateDto.getDescription());
        findSlider.setImageUrl(updateDto.getImageUrl());
        findSlider.setUrl(updateDto.getUrl());
        findSlider.setActive(updateDto.isActive());
        sliderRepository.save(findSlider);
        return new MessageResponse("Slider updated successfully");
    }

    @Transactional
    @Override
    public ApiResponse deleteSlider(Long id) {
        Slider findSlider = sliderRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Slider", "id", id));
        sliderRepository.delete(findSlider);
        return new MessageResponse("Slider deleted successfully");
    }
}
