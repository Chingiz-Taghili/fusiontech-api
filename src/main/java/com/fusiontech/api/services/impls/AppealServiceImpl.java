package com.fusiontech.api.services.impls;

import com.fusiontech.api.dtos.singledtos.AppealDto;
import com.fusiontech.api.exceptions.ResourceNotFoundException;
import com.fusiontech.api.models.Appeal;
import com.fusiontech.api.payloads.ApiResponse;
import com.fusiontech.api.payloads.DataResponse;
import com.fusiontech.api.payloads.MessageResponse;
import com.fusiontech.api.payloads.Paged;
import com.fusiontech.api.repositories.AppealRepository;
import com.fusiontech.api.services.AppealService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AppealServiceImpl implements AppealService {
    private final AppealRepository appealRepository;
    private final ModelMapper modelMapper;

    public AppealServiceImpl(AppealRepository appealRepository, ModelMapper modelMapper) {
        this.appealRepository = appealRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    @Override
    public ApiResponse createAppeal(AppealDto appealDto) {
        Appeal appeal = modelMapper.map(appealDto, Appeal.class);
        appealRepository.save(appeal);
        return new MessageResponse("Appeal created successfully");
    }

    @Transactional
    @Override
    public ApiResponse deleteAppeal(Long id) {
        Appeal findAppeal = appealRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Appeal", "id", id));
        appealRepository.delete(findAppeal);
        return new MessageResponse("Appeal deleted successfully");
    }

    @Transactional(readOnly = true)
    @Override
    public ApiResponse getAppealById(Long id) {
        Appeal findAppeal = appealRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Appeal", "id", id));
        AppealDto appeal = modelMapper.map(findAppeal, AppealDto.class);
        return new DataResponse<>(appeal);
    }

    @Transactional(readOnly = true)
    @Override
    public ApiResponse getSearchAppeals(String keyword, Integer pageNumber, Integer pageSize) {
        pageNumber = (pageNumber == null || pageNumber < 1) ? 1 : pageNumber;
        pageSize = (pageSize == null || pageSize < 1) ? 10 : pageSize;
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, Sort.by("id"));
        Page<Appeal> findAppeals = appealRepository.findByKeywordInColumnsIgnoreCase(keyword, pageable);
        if (findAppeals.getContent().isEmpty()) {
            return new MessageResponse("No appeals found for the keyword: " + keyword);
        }
        List<AppealDto> appeals = findAppeals.getContent().stream().map(
                appeal -> modelMapper.map(appeal, AppealDto.class)).toList();
        return new DataResponse<>(new Paged<>(appeals, pageNumber, findAppeals.getTotalPages()));
    }

    @Transactional(readOnly = true)
    @Override
    public ApiResponse getAllAppeals(Integer pageNumber, Integer pageSize) {
        pageNumber = (pageNumber == null || pageNumber < 1) ? 1 : pageNumber;
        pageSize = (pageSize == null || pageSize < 1) ? 10 : pageSize;
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, Sort.by("id"));
        Page<Appeal> findAppeals = appealRepository.findAll(pageable);
        if (findAppeals.getContent().isEmpty()) {
            return new MessageResponse("No appeals available");
        }
        List<AppealDto> appeals = findAppeals.getContent().stream().map(
                appeal -> modelMapper.map(appeal, AppealDto.class)).toList();
        return new DataResponse<>(new Paged<>(appeals, pageNumber, findAppeals.getTotalPages()));
    }

    @Transactional(readOnly = true)
    @Override
    public ApiResponse getTotalCount() {
        return new DataResponse<>(appealRepository.count());
    }
}
