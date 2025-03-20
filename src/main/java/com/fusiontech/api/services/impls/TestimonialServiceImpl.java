package com.fusiontech.api.services.impls;

import com.fusiontech.api.dtos.common.TestimonialDto;
import com.fusiontech.api.models.Testimonial;
import com.fusiontech.api.payloads.ApiResponse;
import com.fusiontech.api.payloads.DataResponse;
import com.fusiontech.api.payloads.MessageResponse;
import com.fusiontech.api.repositories.TestimonialRepository;
import com.fusiontech.api.services.TestimonialService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TestimonialServiceImpl implements TestimonialService {
    private final TestimonialRepository testimonialRepository;
    private final ModelMapper modelMapper;

    public TestimonialServiceImpl(TestimonialRepository testimonialRepository, ModelMapper modelMapper) {
        this.testimonialRepository = testimonialRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional(readOnly = true)
    @Override
    public ApiResponse getAllTestimonials() {
        List<Testimonial> findTestimonials = testimonialRepository.findAll();
        if (findTestimonials.isEmpty()) {
            return new MessageResponse("No testimonials available");
        }
        List<TestimonialDto> testimonials = findTestimonials.stream().map(testimonial ->
                modelMapper.map(testimonial, TestimonialDto.class)).toList();
        return new DataResponse<>(testimonials);
    }
}
