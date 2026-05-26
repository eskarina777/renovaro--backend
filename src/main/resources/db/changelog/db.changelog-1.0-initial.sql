--liquibase formatted sql


-- MODULE ad ---------------


--changeset lina:20240701-create-package-type
CREATE TABLE package_type
(
    package_type_id   SERIAL PRIMARY KEY,
    package_type_code VARCHAR(50) NOT NULL,
    package_type_label VARCHAR(50)
);

--changeset lina:20251220-create-ad-image
CREATE TABLE ad_image
(
    ad_image_id   SERIAL PRIMARY KEY,
    ad_id         INT NOT NULL,
	is_primary    BOOLEAN,
    ad_image_url  TEXT,
    ad_image_url_id     VARCHAR(255),
    created_at    TIMESTAMP NOT NULL
);

--changeset lina:20251220-create-ad-status
CREATE TABLE ad_status
(
    ad_status_id SERIAL PRIMARY KEY,
    ad_status_code VARCHAR(30) NOT NULL
);

--changeset lina:20251220-create-ad-pricing-plan
CREATE TABLE ad_pricing_plan
(
    ad_pricing_plan_id SERIAL PRIMARY KEY,
    plan_code          VARCHAR(50) NOT NULL,
    plan_name          VARCHAR(30) NOT NULL,
    credits            INT NOT NULL
);

--changeset lina:20251220-create-service-type
CREATE TABLE service_type
(
    service_type_id    SERIAL PRIMARY KEY,
    service_type_code  VARCHAR(50) NOT NULL,
    service_type_label VARCHAR(50) NOT NULL
);

--changeset lina:20251220-create-pricing-unit
CREATE TABLE pricing_unit
(
    pricing_unit_id    SERIAL PRIMARY KEY,
    pricing_unit_code  VARCHAR(50) NOT NULL,
    pricing_unit_label VARCHAR(50) NOT NULL
);

--changeset lina:20251220-create-subcategory
CREATE TABLE subcategory
(
    subcategory_id   SERIAL PRIMARY KEY,
    category_id      INT NOT NULL,
    subcategory_code VARCHAR(40) NOT NULL,
    subcategory_name VARCHAR(50) NOT NULL
);

--changeset lina:20251220-create-category
CREATE TABLE category
(
    category_id   SERIAL PRIMARY KEY,
    category_code VARCHAR(40) NOT NULL,
    category_name VARCHAR(50) NOT NULL
);

--changeset lina:20251220-create-consultation
CREATE TABLE consultation
(
    consultation_id        SERIAL PRIMARY KEY,
    local_service_id       INT,
    consultation_price_min DECIMAL(8, 2),
    consultation_price_max DECIMAL(8, 2)
);

--changeset lina:20251220-create-local-service
CREATE TABLE local_service
(
    local_service_id    SERIAL PRIMARY KEY,
    ad_id               INT NOT NULL,
    service_price_min   DECIMAL(8,2),
    service_price_max   DECIMAL(8,2),
    provider_question   TEXT,
    important_info      TEXT
);

--changeset lina:20251220-create-freelance-package
CREATE TABLE freelance_package
(
    package_id            SERIAL PRIMARY KEY,
    package_type_id       INT NOT NULL,
    ad_id                 INT NOT NULL,
    package_title         VARCHAR(80) ,
    package_description   TEXT ,
    package_price         DECIMAL(8,2),
    delivery_days         INT NOT NULL,
    revision_count        VARCHAR(50),
    render_count          INT,
    detail_drawing_count  INT,
    has_source_file       BOOLEAN,
    has_3d_model          BOOLEAN,
    has_2d_drawings       BOOLEAN,
    important_info        TEXT
);

--changeset lina:20251220-create-ad
CREATE TABLE ad
(
    ad_id                SERIAL PRIMARY KEY,
    city_id              INT,
    user_profile_id      INT NOT NULL,
    ad_status_id         INT NOT NULL,
    ad_pricing_plan_id   INT NOT NULL,
    service_type_id      INT NOT NULL,
    pricing_unit_id      INT,
    subcategory_id       INT NOT NULL,
    created_at           TIMESTAMP NOT NULL,
    ad_expiration_date   TIMESTAMP NOT NULL,
    view_count           INT,
    ad_title             VARCHAR(150) NOT NULL,
    ad_description       TEXT,
    important_info       TEXT,
    is_archived          BOOLEAN
);

--changeset lina:20251220-create-client-service-request
CREATE TABLE client_service_request
(
    request_id          SERIAL PRIMARY KEY,
    category_id         INT NOT NULL,
    user_id             INT NOT NULL,
    request_title       VARCHAR(150) NOT NULL,
    request_description TEXT NOT NULL
);



-- MODULE reservation ---------------


--changeset lina:20251220-create-system-time-slot
CREATE TABLE system_time_slot
(
    system_time_slot_id SERIAL PRIMARY KEY,
    time_slot           TIME        NOT NULL,
    time_slot_label     VARCHAR(10) NOT NULL
);


--changeset lina:20251220-create-service-system-time-slot
CREATE TABLE service_system_time_slot
(
    system_time_slot_id INT NOT NULL,
    service_time_slot_id INT NOT NULL
);

--changeset lina:20251220-create-ad-service-time-slot
CREATE TABLE ad_service_time_slot
(
    ad_id               INT NOT NULL,
    service_time_slot_id INT NOT NULL
);

--changeset lina:20251220-create-service-time-slot
CREATE TABLE service_time_slot
(
    service_time_slot_id SERIAL PRIMARY KEY,
    user_profile_id      INT NOT NULL,
    time_slot_status_id  INT,
    start_time_label     VARCHAR(10),
    end_time_label       VARCHAR(10),
    date                 DATE NOT NULL
);

--changeset lina:20251220-create-time-slot-status
CREATE TABLE time_slot_status
(
    time_slot_status_id   SERIAL PRIMARY KEY,
    time_slot_status_code VARCHAR(50) NOT NULL,
    description    VARCHAR(150)
);

--changeset lina:20251220-create-service-service_reservation
CREATE TABLE service_reservation
(
    service_reservation_id   SERIAL PRIMARY KEY,
    user_id              INT NOT NULL,
    service_time_slot_id INT NOT NULL,
    re_status_id         INT NOT NULL,
    address_id           INT NOT NULL,
    created_at           TIMESTAMP NOT NULL,
    is_archived          BOOLEAN
);

--changeset lina:20251223-create-re_status
CREATE TABLE re_status (
                           re_status_id SERIAL PRIMARY KEY,
                           re_status_code VARCHAR(30),
                           description VARCHAR(200)
);

--changeset lina:20251225-create-freelance-request
CREATE TABLE freelance_request
(
    freelance_request_id SERIAL PRIMARY KEY,
    re_status_id         INT NOT NULL,
    package_id           INT NOT NULL,
    user_id              INT NOT NULL,
    due_date             DATE,
    created_at           TIMESTAMP NOT NULL,
    is_archived          BOOLEAN
);


-- MODULE rating ---------------


--changeset lina:20251226-create-review
CREATE TABLE review
(
    review_id               SERIAL PRIMARY KEY,
    freelance_request_id    INT,
    service_reservation_id  INT,
    created_at              DATE NOT NULL,
    review_content          TEXT,
    service_start_date      DATE,
    service_end_date        DATE,
    service_price           DECIMAL(8,2),
    review_image_url        TEXT,
	review_image_id			VARCHAR(255),
    rating_general          INT NOT NULL,
    provider_answer         TEXT
);

--changeset lina:20251220-create-user-rating-criteria
CREATE TABLE review_rating_criteria
(
    review_id       INT NOT NULL,
    rating_criteria_id   INT NOT NULL,
    rating_by_criteria   INT NOT NULL
);

--changeset lina:20251220-create-rating-criteria
CREATE TABLE rating_criteria
(
    rating_criteria_id   SERIAL PRIMARY KEY,
    rating_criteria_code VARCHAR(30) NOT NULL,
    rating_criteria_label VARCHAR(30) NOT NULL
);


-- MODULE user ---------------


--changeset lina:20251220-create-company
CREATE TABLE company
(
    company_id         SERIAL PRIMARY KEY,
    user_profile_id    INT NOT NULL,
    company_name       VARCHAR(50) NOT NULL,
    established_on     DATE NOT NULL,
    number_employees   INT,
    field_of_operation VARCHAR(50) NOT NULL
);

--changeset lina:20251220-create-role
CREATE TABLE role
(
    role_id   SERIAL PRIMARY KEY,
    role_name VARCHAR(50) NOT NULL,
    role_code VARCHAR(50) NOT NULL,
    role_label VARCHAR(50) NOT NULL
);

--changeset lina:20251220-create-user-role
CREATE TABLE user_role
(
    user_id INT NOT NULL,
    role_id INT NOT NULL
);

--changeset lina:20251220-create-refresh-token
CREATE TABLE refresh_token
(
    refresh_token_id SERIAL PRIMARY KEY,
    user_id          INT NOT NULL,
    token_hash       VARCHAR(255) NOT NULL,
    expires_at       TIMESTAMP NOT NULL,
    revoked_at       TIMESTAMP
);

--changeset lina:20251220-create-user-profile
CREATE TABLE user_profile
(
    user_profile_id   SERIAL PRIMARY KEY,
    user_id           INT NOT NULL,
    user_details      TEXT,
    rating_average    DECIMAL(2,1),
    rating_count      INT,
    calendar_limit    DATE,
    credit_balance    INT,
    user_website      VARCHAR(255),
    show_phone_number BOOLEAN
);

--changeset lina:20251220-create-users
CREATE TABLE users
(
    user_id           SERIAL PRIMARY KEY,
    first_name        VARCHAR(50)  NOT NULL,
    last_name         VARCHAR(50)  NOT NULL,
    profile_image_url TEXT         ,
    profile_image_id    VARCHAR(255)        ,
    phone_number      VARCHAR(50),
    email_address     VARCHAR(255) NOT NULL,
    password          VARCHAR(255) NOT NULL,
    is_email_confirmed BOOLEAN,
    joined_on         TIMESTAMP    NOT NULL,
    CONSTRAINT uq_user_email UNIQUE (email_address)
);

--changeset lina:20251220-create-city
CREATE TABLE city
(
    city_id   SERIAL PRIMARY KEY,
    city_name VARCHAR(30) NOT NULL
);

--changeset lina:20251220-create-address
CREATE TABLE address
(
    address_id    SERIAL PRIMARY KEY,
    city_id       INT NOT NULL,
    user_id      INT        NOT NULL,
    street        VARCHAR(30) NOT NULL,
    street_number VARCHAR(30) NOT NULL,
    district      VARCHAR(50) NOT NULL
);


--changeset lina:20251220-create-confirmation-token
CREATE TABLE confirmation_token
(
    confirmation_token_id SERIAL PRIMARY KEY,
    user_id               INT NOT NULL,
    token_hash            VARCHAR(255) NOT NULL,
    expires_at            TIMESTAMP NOT NULL,
    used_at               TIMESTAMP
);


-- MODULE notification -----------

--changeset lina:20251225-create-user-notification-type
CREATE TABLE user_notification_type
(
    user_notification_type_id SERIAL PRIMARY KEY,
    user_id                  INT NOT NULL,
    notification_type_id     INT NOT NULL,
    is_enabled               BOOLEAN NOT NULL DEFAULT TRUE
);

--changeset lina:20251220-create-notification-type
CREATE TABLE notification_type
(
    notification_type_id   SERIAL PRIMARY KEY,
    notification_type_name VARCHAR(30) NOT NULL,
    notification_type_code VARCHAR(30) NOT NULL,
    notification_title     VARCHAR(100) NOT NULL,
    is_required            BOOLEAN NOT NULL
);

--changeset lina:20251220-create-entity-type
CREATE TABLE entity_type
(
    entity_type_id SERIAL PRIMARY KEY,
    entity_type_name VARCHAR(30) NOT NULL,
    description TEXT
);

--changeset lina:20251220-create-notification
CREATE TABLE notification
(
    notification_id        SERIAL PRIMARY KEY,
    notification_type_id   INT NOT NULL,
    notification_object_id INT ,
    notification_text      TEXT NOT NULL,
    recipient_user_id      INT ,
    created_at             TIMESTAMP NOT NULL,
    is_read                BOOLEAN DEFAULT FALSE
);

--changeset lina:20251220-create-notification-object
CREATE TABLE notification_object
(
    notification_object_id SERIAL PRIMARY KEY,
    entity_type_id          INT,
    created_by_user_id      INT,
    entity_id               INT,
    created_at              TIMESTAMP
);


-- MODULE chat -----------


--changeset lina:20251220-create-chat-user
CREATE TABLE chat_user
(
    user_id              INT       NOT NULL,
    chat_id              INT       NOT NULL,
    joined_on            TIMESTAMP NOT NULL,
    left_on              TIMESTAMP,
    is_read              BOOLEAN,
    is_starred           BOOLEAN,
	is_archived          BOOLEAN
);

--changeset lina:20251220-create-chat
CREATE TABLE chat
(
    chat_id   SERIAL PRIMARY KEY,
    created_at TIMESTAMP NOT NULL
);

--changeset lina:20251220-create-message
CREATE TABLE message
(
    message_id   SERIAL PRIMARY KEY,
    chat_id      INT NOT NULL,
    user_id      INT NOT NULL,
    message_body TEXT NOT NULL,
    created_at   TIMESTAMP NOT NULL
);

