package com.scentalux.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/upload")
@CrossOrigin(origins = "*")
public class FileUploadController {

    // =========================
    // Constants
    // =========================
    private static final String DEFAULT_UPLOAD_DIR = "uploads";
    private static final String ERROR_KEY = "error";
    private static final String MESSAGE_KEY = "message";

    private static final String ERROR_IMAGE_ONLY = "Solo se permiten archivos de imagen";
    private static final String ERROR_IMAGE_PDF_ONLY = "Solo se permiten archivos de imagen (JPG, PNG, etc.) o PDF";
    private static final String ERROR_FILE_TOO_LARGE = "El archivo no puede ser mayor a 5MB";
    private static final String ERROR_UPLOAD_FAILED = "Error al subir la imagen";
    private static final String ERROR_RECEIPT_FAILED = "Error al subir el comprobante: ";
    private static final String ERROR_FILE_REQUIRED = "Nombre de archivo requerido";
    private static final String ERROR_FILE_NOT_FOUND = "Archivo no encontrado";
    private static final String ERROR_FILE_DELETE = "Error al eliminar el archivo: ";

    private static final String URL_PREFIX = "/uploads/";
    private static final long MAX_FILE_SIZE = 5L * 1024 * 1024; // 5MB

    // =========================
    // Configuration
    // =========================
    @Value("${file.upload-dir:" + DEFAULT_UPLOAD_DIR + "}")
    private String uploadDir;

    // =========================
    // Upload image
    // =========================
    @PostMapping("/image")
    public ResponseEntity<Map<String, Object>> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            Path uploadPath = getUploadPath();

            // Validate file type
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return ResponseEntity.badRequest().body(Map.of(ERROR_KEY, ERROR_IMAGE_ONLY));
            }

            // Save file
            String fileName = generateFileName(file);
            Files.copy(file.getInputStream(), uploadPath.resolve(fileName));

            // Return URL
            return ResponseEntity.ok(Map.of("url", URL_PREFIX + fileName));

        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(Map.of(ERROR_KEY, ERROR_UPLOAD_FAILED));
        }
    }

    // =========================
    // Upload receipt (image or PDF)
    // =========================
    @PostMapping("/receipt")
    public ResponseEntity<Map<String, Object>> uploadReceipt(@RequestParam("file") MultipartFile file)
 {
        try {
            Path uploadPath = getUploadPath();

            // Validate type
            String contentType = file.getContentType();
            if (contentType == null || (!contentType.startsWith("image/") && !contentType.equals("application/pdf"))) {
                return ResponseEntity.badRequest().body(Map.of(ERROR_KEY, ERROR_IMAGE_PDF_ONLY));
            }

            // Validate size
            if (file.getSize() > MAX_FILE_SIZE) {
                return ResponseEntity.badRequest().body(Map.of(ERROR_KEY, ERROR_FILE_TOO_LARGE));
            }

            // Save file
            String fileName = "receipt_" + generateFileName(file);
            Files.copy(file.getInputStream(), uploadPath.resolve(fileName));

            Map<String, String> response = new HashMap<>();
            response.put("url", URL_PREFIX + fileName);
            response.put("fileName", fileName);
            response.put("fileType", contentType);
            response.put("size", String.valueOf(file.getSize()));

            return ResponseEntity.ok(Map.<String, Object>of(MESSAGE_KEY, "Archivo eliminado correctamente"));

        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(Map.of(ERROR_KEY, ERROR_RECEIPT_FAILED + e.getMessage()));
        }
    }

    // =========================
    // Delete file
    // =========================
    @DeleteMapping("/file")
    public ResponseEntity<Map<String, Object>> deleteFile(@RequestBody Map<String, String> request)
 {
        try {
            String fileName = request.get("fileName");
            if (fileName == null || fileName.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(ERROR_KEY, ERROR_FILE_REQUIRED));
            }

            Path filePath = Paths.get(uploadDir).resolve(fileName);
            if (Files.exists(filePath)) {
                Files.delete(filePath);
                return ResponseEntity.ok(Map.of(MESSAGE_KEY, "Archivo eliminado correctamente"));
            } else {
                return ResponseEntity.status(404).body(Map.of(ERROR_KEY, ERROR_FILE_NOT_FOUND));
            }
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(Map.of(ERROR_KEY, ERROR_FILE_DELETE + e.getMessage()));
        }
    }

    // =========================
    // Helper methods
    // =========================
    private Path getUploadPath() throws IOException {
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        return uploadPath;
    }

    private String generateFileName(MultipartFile file) {
        String originalFileName = file.getOriginalFilename();
        String extension = "";
        if (originalFileName != null && originalFileName.contains(".")) {
            extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        }
        return UUID.randomUUID().toString() + extension;
    }
}
