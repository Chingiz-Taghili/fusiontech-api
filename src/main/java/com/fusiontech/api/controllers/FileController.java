package com.fusiontech.api.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("api/upload")
public class FileController {

    private static final String UPLOAD_DIR = "uploads/";

    @PostMapping("/image")
    public ResponseEntity<String> uploadImage(@RequestParam MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("No file was uploaded.");
        }

        try {
            if (file.getContentType() == null || !file.getContentType().startsWith("image/")) {
                throw new IllegalArgumentException("The uploaded file must be an image!");
            }
            if (file.getSize() > 2 * 1024 * 1024) {
                throw new IllegalArgumentException("The image size must not exceed 2MB.");
            }
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(UPLOAD_DIR + fileName);
            Files.createDirectories(filePath.getParent());
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            String imageUrl = ServletUriComponentsBuilder
                    .fromCurrentContextPath().path("/uploads/").path(fileName).toUriString();
            return ResponseEntity.ok(imageUrl);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while uploading the file.");
        }
    }

    @PostMapping("/images")
    public ResponseEntity<?> uploadImages(@RequestParam MultipartFile[] files) {
        List<String> imageUrls = new ArrayList<>();
        List<Map<String, String>> failedFiles = new ArrayList<>();

        if (files == null || files.length == 0 || files[0].isEmpty()) {
            throw new IllegalArgumentException("No files were uploaded.");
        }

        for (MultipartFile file : files) {
            try {
                if (file.getContentType() == null || !file.getContentType().startsWith("image/")) {
                    failedFiles.add(Map.of("file", file.getOriginalFilename(), "error", "File must be an image!"));
                    continue;
                }
                if (file.getSize() > 2 * 1024 * 1024) {
                    failedFiles.add(Map.of("file", file.getOriginalFilename(), "error", "File size must not exceed 2MB."));
                    continue;
                }
                String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                Path filePath = Paths.get(UPLOAD_DIR + fileName);
                Files.createDirectories(filePath.getParent());
                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                String imageUrl = ServletUriComponentsBuilder
                        .fromCurrentContextPath().path("/uploads/").path(fileName).toUriString();
                imageUrls.add(imageUrl);

            } catch (IOException e) {
                failedFiles.add(Map.of("file", file.getOriginalFilename(), "error", "Failed to upload due to server error."));
            }
        }
        return ResponseEntity.ok(Map.of("uploaded", imageUrls, "failed", failedFiles));
    }
}
