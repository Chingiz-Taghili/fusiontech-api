package com.fusiontech.api.controllers;

import com.fusiontech.api.dtos.common.AppealDto;
import com.fusiontech.api.payloads.ApiResponse;
import com.fusiontech.api.services.AppealService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/appeal")
public class AppealController {

    private final AppealService appealService;

    public AppealController(AppealService appealService) {
        this.appealService = appealService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createAppeal(@RequestBody AppealDto appealDto) {
        ApiResponse response = appealService.createAppeal(appealDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteAppeal(@PathVariable Long id) {
        ApiResponse response = appealService.deleteAppeal(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getAppealById(@PathVariable Long id) {
        ApiResponse response = appealService.getAppealById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllAppeals(@RequestParam(required = false) Integer pageNumber,
                                                     @RequestParam(required = false) Integer pageSize) {
        ApiResponse response = appealService.getAllAppeals(pageNumber, pageSize);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse> getSearchAppeals(@RequestParam String keyword,
                                                        @RequestParam(required = false) Integer pageNumber,
                                                        @RequestParam(required = false) Integer pageSize) {
        ApiResponse response = appealService.getSearchAppeals(keyword, pageNumber, pageSize);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/count")
    public ResponseEntity<ApiResponse> getTotalCount() {
        ApiResponse response = appealService.getTotalCount();
        return ResponseEntity.ok(response);
    }
}
