package com.fusiontech.api.repositories;

import com.fusiontech.api.models.Testimonial;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestimonialRepository extends JpaRepository<Testimonial, Long> {
}
