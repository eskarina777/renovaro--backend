package eu.renovaro.ad.service;

import eu.renovaro.ad.domain.PublishAdRequest;
import eu.renovaro.ad.domain.ServiceRequestPayload;

public interface AdPublishService {
    Long publishAd(PublishAdRequest request, Long userProfileId);
    Long createServiceRequest(ServiceRequestPayload payload, Long userId);
}
