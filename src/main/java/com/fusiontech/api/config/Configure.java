package com.fusiontech.api.config;

import com.fusiontech.api.dtos.product.ProductCreateDto;
import com.fusiontech.api.models.Product;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Configure {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // ProductCreateDto -> Product mapping zamanı `id`-ni atla
        modelMapper.addMappings(new PropertyMap<ProductCreateDto, Product>() {
            @Override
            protected void configure() {
                skip(destination.getId()); // `id` mapping-i dayandır
            }
        });

        return modelMapper;
    }
}
