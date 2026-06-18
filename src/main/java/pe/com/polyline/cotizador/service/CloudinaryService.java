package pe.com.polyline.cotizador.service;

import com.cloudinary.Cloudinary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public String uploadFile(MultipartFile file) {

        // 1. VALIDAR VACÍO
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("El archivo está vacío");
        }

        // 2. VALIDAR TIPO
        String contentType = file.getContentType();

        if (contentType == null || !contentType.startsWith("image/")) {
            throw new RuntimeException("Solo se permiten archivos de tipo imagen");
        }

        // 3. VALIDAR TAMAÑO (ej: 5MB)
        long maxSize = 5 * 1024 * 1024; // 5MB

        if (file.getSize() > maxSize) {
            throw new RuntimeException("El archivo excede el tamaño máximo permitido (5MB)");
        }

        try {

            Map uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    Map.of("folder", "productos") // Crea carpeta automáticamente en Cloudinary
            );

            return uploadResult.get("secure_url").toString();

        } catch (IOException e) {
            throw new RuntimeException("Error subiendo imagen a Cloudinary");
        }
    }
}

