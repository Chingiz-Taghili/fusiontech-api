package com.fusiontech.api.controllers;

import com.fusiontech.api.dtos.slider.SliderCreateDto;
import com.fusiontech.api.dtos.slider.SliderUpdateDto;
import com.fusiontech.api.payloads.ApiResponse;
import com.fusiontech.api.services.SliderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/slider")
public class SliderController {

    private final SliderService sliderService;

    public SliderController(SliderService sliderService) {
        this.sliderService = sliderService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllSliders() {
        ApiResponse response = sliderService.getAllSliders();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getSliderById(@PathVariable Long id) {
        ApiResponse response = sliderService.getSliderById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse> getSearchSliders(@RequestParam String keyword) {
        ApiResponse response = sliderService.getSearchSliders(keyword);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createSlider(@RequestBody SliderCreateDto createDto) {
        ApiResponse response = sliderService.createSlider(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateSlider(@PathVariable Long id, @RequestBody SliderUpdateDto updateDto) {
        ApiResponse response = sliderService.updateSlider(id, updateDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteSlider(@PathVariable Long id) {
        ApiResponse response = sliderService.deleteSlider(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/count")
    public ResponseEntity<ApiResponse> getTotalCount() {
        ApiResponse response = sliderService.getTotalCount();
        return ResponseEntity.ok(response);
    }
}
