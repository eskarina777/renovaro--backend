package eu.renovaro.common.controller;

import eu.renovaro.common.service.CloudinaryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/public/cloudinary")
public class CloudinaryTestController {

    private final CloudinaryService cloudinaryService;

    public CloudinaryTestController(CloudinaryService cloudinaryService) {
        this.cloudinaryService = cloudinaryService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) {
        CloudinaryService.UploadResult result = cloudinaryService.uploadImage(file);
        return ResponseEntity.ok(result.url());
    }

}