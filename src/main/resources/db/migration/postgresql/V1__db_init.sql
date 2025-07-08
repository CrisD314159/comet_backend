CREATE TABLE friend_request
(
    id             UUID NOT NULL,
    requester_id   UUID,
    recipient_id   UUID,
    date_requested TIMESTAMP WITHOUT TIME ZONE,
    message        VARCHAR(100),
    state          SMALLINT,
    CONSTRAINT pk_friendrequest PRIMARY KEY (id)
);

CREATE TABLE friendship
(
    id            UUID    NOT NULL,
    requester_id  UUID,
    recipient_id  UUID,
    date_accepted TIMESTAMP WITHOUT TIME ZONE,
    is_blocked    BOOLEAN NOT NULL,
    CONSTRAINT pk_friendship PRIMARY KEY (id)
);

CREATE TABLE session
(
    id         UUID NOT NULL,
    user_id    UUID,
    created_at TIMESTAMP WITHOUT TIME ZONE,
    expires_at TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_session PRIMARY KEY (id)
);

CREATE TABLE users
(
    id                  UUID         NOT NULL,
    name                VARCHAR(60)  NOT NULL,
    biography           VARCHAR(200),
    country             VARCHAR(60),
    email               VARCHAR(255) NOT NULL,
    password            VARCHAR(255) NOT NULL,
    profile_picture     VARCHAR(255) NOT NULL,
    is_verified         BOOLEAN      NOT NULL,
    state               SMALLINT,
    verification_code   VARCHAR(255),
    created_at          TIMESTAMP WITHOUT TIME ZONE,
    last_modified_at    TIMESTAMP WITHOUT TIME ZONE,
    created_with_google BOOLEAN      NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

ALTER TABLE users
    ADD CONSTRAINT uc_users_email UNIQUE (email);

ALTER TABLE users
    ADD CONSTRAINT uc_users_name UNIQUE (name);

ALTER TABLE friend_request
    ADD CONSTRAINT FK_FRIENDREQUEST_ON_RECIPIENT FOREIGN KEY (recipient_id) REFERENCES users (id);

ALTER TABLE friend_request
    ADD CONSTRAINT FK_FRIENDREQUEST_ON_REQUESTER FOREIGN KEY (requester_id) REFERENCES users (id);

ALTER TABLE friendship
    ADD CONSTRAINT FK_FRIENDSHIP_ON_RECIPIENT FOREIGN KEY (recipient_id) REFERENCES users (id);

ALTER TABLE friendship
    ADD CONSTRAINT FK_FRIENDSHIP_ON_REQUESTER FOREIGN KEY (requester_id) REFERENCES users (id);

ALTER TABLE session
    ADD CONSTRAINT FK_SESSION_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);