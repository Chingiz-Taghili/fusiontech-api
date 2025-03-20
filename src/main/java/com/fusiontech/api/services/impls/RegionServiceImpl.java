package com.fusiontech.api.services.impls;

import com.fusiontech.api.models.Region;
import com.fusiontech.api.payloads.ApiResponse;
import com.fusiontech.api.payloads.DataResponse;
import com.fusiontech.api.payloads.MessageResponse;
import com.fusiontech.api.repositories.RegionRepository;
import com.fusiontech.api.services.RegionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RegionServiceImpl implements RegionService {
    private final RegionRepository regionRepository;

    public RegionServiceImpl(RegionRepository regionRepository) {
        this.regionRepository = regionRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public ApiResponse getRegions() {
        List<Region> regions = regionRepository.findAllByOrderByNumberAsc();
        if (regions.isEmpty()) {
            return new MessageResponse("No regions available");
        }
        return new DataResponse<>(regions);
    }
}
