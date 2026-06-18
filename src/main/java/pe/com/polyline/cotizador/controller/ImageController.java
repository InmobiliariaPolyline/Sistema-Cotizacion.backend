package pe.com.polyline.cotizador.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pe.com.polyline.cotizador.service.CloudinaryService;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageController {

    private final CloudinaryService cloudinaryService;

    @PostMapping("/upload-image")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) {

        String url = cloudinaryService.uploadFile(file);

        return ResponseEntity.ok(url);
    }
}
