package com.fusiontech.api.services;

import com.fusiontech.api.dtos.common.AppealDto;
import com.fusiontech.api.payloads.ApiResponse;

public interface AppealService {

    ApiResponse createAppeal(AppealDto appealDto);

    ApiResponse deleteAppeal(Long id);

    ApiResponse getAppealById(Long id);

    ApiResponse getSearchAppeals(String keyword, Integer pageNumber, Integer pageSize);

    ApiResponse getAllAppeals(Integer pageNumber, Integer pageSize);

    ApiResponse getTotalCount();
}