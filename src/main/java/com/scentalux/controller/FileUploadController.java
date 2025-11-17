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

    @Value("${file.upload-dir:uploads}")
    private String uploadDir;

    @PostMapping("/image")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            // Crear directorio si no existe
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Validar tipo de archivo
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Solo se permiten archivos de imagen");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            // Generar nombre único
            String originalFileName = file.getOriginalFilename();
            String fileExtension = "";
            if (originalFileName != null && originalFileName.contains(".")) {
                fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            }
            String fileName = UUID.randomUUID().toString() + fileExtension;

            // Guardar archivo
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath);

            // URL para acceder a la imagen
            String fileUrl = "/uploads/" + fileName;

            Map<String, String> successResponse = new HashMap<>();
            successResponse.put("url", fileUrl);

            return ResponseEntity.ok(successResponse);

        } catch (IOException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error al subir la imagen");
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
    // 🔥 NUEVO MÉTODO PARA SUBIR COMPROBANTES DE PAGO
    @PostMapping("/receipt")
    public ResponseEntity<?> uploadReceipt(@RequestParam("file") MultipartFile file) {
        try {
            // Crear directorio si no existe
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Validar tipo de archivo (imágenes y PDFs)
            String contentType = file.getContentType();
            if (contentType == null || 
                (!contentType.startsWith("image/") && !contentType.equals("application/pdf"))) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Solo se permiten archivos de imagen (JPG, PNG, etc.) o PDF");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            // Validar tamaño del archivo (máximo 5MB)
            if (file.getSize() > 5 * 1024 * 1024) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "El archivo no puede ser mayor a 5MB");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            // Generar nombre único para comprobante
            String originalFileName = file.getOriginalFilename();
            String fileExtension = "";
            if (originalFileName != null && originalFileName.contains(".")) {
                fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            }
            String fileName = "receipt_" + UUID.randomUUID().toString() + fileExtension;

            // Guardar archivo
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath);

            // URL para acceder al archivo
            String fileUrl = "/uploads/" + fileName;

            Map<String, String> successResponse = new HashMap<>();
            successResponse.put("url", fileUrl);
            successResponse.put("fileName", fileName);
            successResponse.put("fileType", contentType);
            successResponse.put("size", String.valueOf(file.getSize()));

            return ResponseEntity.ok(successResponse);

        } catch (IOException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error al subir el comprobante: " + e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    // Método opcional para eliminar archivos (si lo necesitas)
    @DeleteMapping("/file")
    public ResponseEntity<?> deleteFile(@RequestBody Map<String, String> request) {
        try {
            String fileName = request.get("fileName");
            if (fileName == null || fileName.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Nombre de archivo requerido"));
            }

            Path filePath = Paths.get(uploadDir).resolve(fileName);
            if (Files.exists(filePath)) {
                Files.delete(filePath);
                return ResponseEntity.ok(Map.of("message", "Archivo eliminado correctamente"));
            } else {
                return ResponseEntity.status(404).body(Map.of("error", "Archivo no encontrado"));
            }
        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Error al eliminar el archivo: " + e.getMessage()));
        }
    }
}