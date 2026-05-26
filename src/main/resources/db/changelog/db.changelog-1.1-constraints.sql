--liquibase formatted sql


-- MODULE ad ---------------

--changeset lina:20251220-fk-ad-image-ad
ALTER TABLE ad_image
ADD CONSTRAINT fk_ad_image_ad_id FOREIGN KEY (ad_id) REFERENCES ad(ad_id);

--changeset lina:20251220-fk-subcategory-category
ALTER TABLE subcategory
ADD CONSTRAINT fk_subcategory_category_id FOREIGN KEY (category_id) REFERENCES category(category_id);

--changeset lina:20251220-fk-consultation-local-service
ALTER TABLE consultation
ADD CONSTRAINT fk_consultation_local_service_id FOREIGN KEY (local_service_id) REFERENCES local_service(local_service_id),
ADD CONSTRAINT uq_consultation_local_service_id UNIQUE (local_service_id);


--changeset lina:20251220-local-service-ad-1to1
ALTER TABLE local_service
ADD CONSTRAINT fk_local_service_ad_id FOREIGN KEY (ad_id) REFERENCES ad(ad_id),
ADD CONSTRAINT uq_local_service_ad_id UNIQUE (ad_id);

--changeset lina:20251220-fk-freelance-package
ALTER TABLE freelance_package
ADD CONSTRAINT fk_freelance_package_package_type_id FOREIGN KEY (package_type_id) REFERENCES package_type(package_type_id),
ADD CONSTRAINT fk_freelance_package_ad_id FOREIGN KEY (ad_id) REFERENCES ad(ad_id);

--changeset lina:20251220-fk-ad
ALTER TABLE ad
ADD CONSTRAINT fk_ad_service_type_id FOREIGN KEY (service_type_id) REFERENCES service_type(service_type_id),
ADD CONSTRAINT fk_ad_ad_status_id FOREIGN KEY (ad_status_id) REFERENCES ad_status(ad_status_id),
ADD CONSTRAINT fk_ad_user_profile_id FOREIGN KEY (user_profile_id) REFERENCES user_profile(user_profile_id),
ADD CONSTRAINT fk_ad_city_id FOREIGN KEY (city_id) REFERENCES city(city_id),
ADD CONSTRAINT fk_ad_subcategory_id FOREIGN KEY (subcategory_id) REFERENCES subcategory(subcategory_id),
ADD CONSTRAINT fk_ad_ad_pricing_plan_id FOREIGN KEY (ad_pricing_plan_id) REFERENCES ad_pricing_plan(ad_pricing_plan_id),
ADD CONSTRAINT fk_ad_pricing_unit_id FOREIGN KEY (pricing_unit_id) REFERENCES pricing_unit(pricing_unit_id);

--changeset lina:20251220-fk-client-service-request
ALTER TABLE client_service_request
ADD CONSTRAINT fk_client_service_request_category_id FOREIGN KEY (category_id) REFERENCES category(category_id),
ADD CONSTRAINT fk_client_service_request_user_id FOREIGN KEY (user_id) REFERENCES users(user_id);


-- MODULE service_reservation ---------------


--changeset lina:20251220-fk-service-system-time-slot
ALTER TABLE service_system_time_slot
ADD CONSTRAINT fk_service_system_time_slot_service_time_slot_id FOREIGN KEY (service_time_slot_id) REFERENCES service_time_slot(service_time_slot_id),
ADD CONSTRAINT fk_service_system_time_slot_system_time_slot_id FOREIGN KEY (system_time_slot_id) REFERENCES system_time_slot(system_time_slot_id);

--changeset lina:20251220-fk-ad-service-time-slot
ALTER TABLE ad_service_time_slot
ADD CONSTRAINT fk_ad_service_time_slot_service_time_slot_id FOREIGN KEY (service_time_slot_id) REFERENCES service_time_slot(service_time_slot_id),
ADD CONSTRAINT fk_ad_service_time_slot_ad_id FOREIGN KEY (ad_id) REFERENCES ad(ad_id);

--changeset lina:20251220-fk-service-time-slot
ALTER TABLE service_time_slot
ADD CONSTRAINT fk_service_time_slot_user_profile_id FOREIGN KEY (user_profile_id) REFERENCES user_profile(user_profile_id),
ADD CONSTRAINT fk_service_time_slot_time_slot_status_id FOREIGN KEY (time_slot_status_id) REFERENCES time_slot_status(time_slot_status_id);

--changeset lina:20251220-fk-service-service_reservation
ALTER TABLE service_reservation
ADD CONSTRAINT fk_service_reservation_address_id FOREIGN KEY (address_id) REFERENCES address(address_id),
ADD CONSTRAINT fk_service_reservation_user_id FOREIGN KEY (user_id) REFERENCES users(user_id),
ADD CONSTRAINT fk_service_reservation_service_time_slot_id FOREIGN KEY (service_time_slot_id) REFERENCES service_time_slot(service_time_slot_id),
ADD CONSTRAINT uq_service_reservation_service_time_slot_id UNIQUE (service_time_slot_id),
ADD CONSTRAINT fk_service_reservation_rs_status_id FOREIGN KEY (re_status_id) REFERENCES re_status(re_status_id);

--changeset lina:20251225-uq-re-status-code
ALTER TABLE re_status
ADD CONSTRAINT uq_re_status_code UNIQUE (re_status_code);


--changeset lina:20251225-fk-freelance-request
ALTER TABLE freelance_request
ADD CONSTRAINT fk_freelance_request_re_status_id FOREIGN KEY (re_status_id) REFERENCES re_status(re_status_id),
ADD CONSTRAINT fk_freelance_request_package_id FOREIGN KEY (package_id) REFERENCES freelance_package(package_id),
ADD CONSTRAINT fk_freelance_request_user_id FOREIGN KEY (user_id) REFERENCES users(user_id);

-- MODULE rating ---------------

--changeset lina:20251225-review-constraints
ALTER TABLE review
    ADD CONSTRAINT fk_review_freelance_request_id FOREIGN KEY (freelance_request_id) REFERENCES freelance_request(freelance_request_id),
    ADD CONSTRAINT fk_review_service_reservation_id FOREIGN KEY (service_reservation_id) REFERENCES service_reservation(service_reservation_id),
    ADD CONSTRAINT uq_review_freelance_request_id UNIQUE (freelance_request_id),
    ADD CONSTRAINT uq_review_service_reservation_id UNIQUE (service_reservation_id),
    ADD CONSTRAINT check_review_one_parent
        CHECK (
            (freelance_request_id IS NOT NULL AND service_reservation_id IS NULL)
         OR (freelance_request_id IS NULL AND service_reservation_id IS NOT NULL)
        );

--changeset lina:20251220-fk-user-rating-criteria
ALTER TABLE review_rating_criteria
ADD CONSTRAINT fk_review_rating_criteria_user_rating_id FOREIGN KEY (review_id) REFERENCES review(review_id),
ADD CONSTRAINT fk_review_rating_criteria_rating_criteria_id FOREIGN KEY (rating_criteria_id) REFERENCES rating_criteria(rating_criteria_id);

-- MODULE user ---------------

--changeset lina:20251220-company-user-profile-
ALTER TABLE company
ADD CONSTRAINT fk_company_user_profile_id FOREIGN KEY (user_profile_id) REFERENCES user_profile(user_profile_id),
ADD CONSTRAINT uq_company_user_profile_id UNIQUE (user_profile_id);

--changeset lina:20251220-fk-user-role
ALTER TABLE user_role
ADD CONSTRAINT fk_user_role_role_id FOREIGN KEY (role_id) REFERENCES role(role_id),
ADD CONSTRAINT fk_user_role_user_id FOREIGN KEY (user_id) REFERENCES users(user_id);

--changeset lina:20251220-fk-refresh-token
ALTER TABLE refresh_token
ADD CONSTRAINT fk_refresh_token_user_id FOREIGN KEY (user_id) REFERENCES users(user_id);

--changeset lina:20251220-fk-user-profile-city
ALTER TABLE user_profile
ADD CONSTRAINT fk_user_profile_user_id FOREIGN KEY (user_id) REFERENCES users(user_id),
ADD CONSTRAINT uq_user_profile_user_id UNIQUE (user_id);

--changeset lina:20251220-fk-address-city
ALTER TABLE address
ADD CONSTRAINT fk_address_city_id FOREIGN KEY (city_id) REFERENCES city(city_id),
ADD CONSTRAINT fk_address_user_id FOREIGN KEY (user_id) REFERENCES users(user_id);



--changeset lina:20251220-fk-confimation-token-user
ALTER TABLE confirmation_token
ADD CONSTRAINT fk_confimation_token_user_id FOREIGN KEY (user_id) REFERENCES users(user_id);



-- MODULE notification -----------


--changeset lina:20251220-fk-notification
ALTER TABLE notification
ADD CONSTRAINT fk_notification_notification_type_id FOREIGN KEY (notification_type_id) REFERENCES notification_type(notification_type_id),
ADD CONSTRAINT fk_notification_recipient_user_id FOREIGN KEY (recipient_user_id) REFERENCES users(user_id),
ADD CONSTRAINT fk_notification_notification_object_id FOREIGN KEY (notification_object_id) REFERENCES notification_object(notification_object_id);

--changeset lina:20251220-fk-notification-object
ALTER TABLE notification_object
ADD CONSTRAINT fk_notification_object_created_by_user_id FOREIGN KEY (created_by_user_id) REFERENCES users(user_id),
ADD CONSTRAINT fk_notification_object_entity_type_id FOREIGN KEY (entity_type_id) REFERENCES entity_type(entity_type_id);

--changeset lina:20251225-constraints-user-notification-type
ALTER TABLE user_notification_type
ADD CONSTRAINT fk_user_notification_type_user_id FOREIGN KEY (user_id) REFERENCES users(user_id),
ADD CONSTRAINT fk_user_notification_type_notification_type_id FOREIGN KEY (notification_type_id) REFERENCES notification_type(notification_type_id),
ADD CONSTRAINT uq_user_notification_type_user_notification_type UNIQUE (user_id, notification_type_id);


-- MODULE chat -----------


--changeset lina:20251220-fk-chat-user
ALTER TABLE chat_user
ADD CONSTRAINT fk_chat_user_user_id FOREIGN KEY (user_id) REFERENCES users(user_id),
ADD CONSTRAINT fk_chat_user_chat_id FOREIGN KEY (chat_id) REFERENCES chat(chat_id);

--changeset lina:20251220-fk-message
ALTER TABLE message
ADD CONSTRAINT fk_message_chat_id FOREIGN KEY (chat_id) REFERENCES chat(chat_id),
ADD CONSTRAINT fk_message_user_id FOREIGN KEY (user_id) REFERENCES users(user_id);



-- -- changeset lina:20250705-chat-user-is-active-drop-null
-- ALTER TABLE chat_user ALTER COLUMN is_active DROP NOT NULL;

-- -- changeset lina:20250706-add-notification-text-column
-- ALTER TABLE notification ADD COLUMN notification_text TEXT;

-- -- changeset lina:20250706-add-notification-code
-- ALTER TABLE notification_type ADD COLUMN notification_code VARCHAR(30);

