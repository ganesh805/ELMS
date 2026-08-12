package com.elms.service;

import com.elms.exception.BusinessRuleException;
import com.elms.exception.ResourceNotFoundException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

@Service
public class AttachmentService {

    private final Path fileStorageLocation;

    public AttachmentService() {
        this.fileStorageLocation = Paths.get("uploads").toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new BusinessRuleException("Could not create directory for medical attachment uploads");
        }
    }

    public String storeFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessRuleException("Cannot upload empty file");
        }

        String originalFileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        if (originalFileName.contains("..")) {
            throw new BusinessRuleException("Filename contains invalid path sequence: " + originalFileName);
        }

        String storedFileName = UUID.randomUUID().toString() + "_" + originalFileName;

        try {
            Path targetLocation = this.fileStorageLocation.resolve(storedFileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return storedFileName;
        } catch (IOException ex) {
            throw new BusinessRuleException("Could not store file " + originalFileName + ". Please try again!");
        }
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new ResourceNotFoundException("File not found: " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new ResourceNotFoundException("File not found: " + fileName);
        }
    }
}
