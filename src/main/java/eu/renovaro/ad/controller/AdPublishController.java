package eu.renovaro.ad.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.renovaro.ad.domain.ImagePayload;
import eu.renovaro.ad.domain.PublishAdRequest;
import eu.renovaro.ad.domain.ServiceRequestPayload;
import eu.renovaro.ad.service.AdPublishService;
import eu.renovaro.user.service.UserProfileService;
import eu.renovaro.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ad")
public class AdPublishController {

    private final AdPublishService adPublishService;
    private final UserProfileService userProfileService;
    private final UserService userService;
    private final ObjectMapper objectMapper;

    @PostMapping(value = "/publish", consumes = "multipart/form-data")
    public Long publishAd(@RequestPart("payload") String payload,
                          @RequestPart("files") List<MultipartFile> files)
            throws JsonProcessingException {

        PublishAdRequest request =
                objectMapper.readValue(payload, PublishAdRequest.class);

        if (request.getImages() == null) {
            request.setImages(new ArrayList<>());
        }

        List<ImagePayload> images = request.getImages();

        IntStream.range(0, files.size())
                .forEach(i -> images.get(i).setFile(files.get(i)));

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userProfileId = userProfileService.getUserProfileIdByEmail(auth.getName());

        return adPublishService.publishAd(request, userProfileId);
    }

    @PostMapping("/service-request")
    public Long createServiceRequest(@RequestBody ServiceRequestPayload payload) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = userService.getUserIdByEmail(auth.getName());
        return adPublishService.createServiceRequest(payload, userId);
    }

}
