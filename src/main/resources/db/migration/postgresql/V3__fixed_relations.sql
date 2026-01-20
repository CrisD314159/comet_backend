
CREATE TABLE post_comments
(
    post_id     UUID NOT NULL,
    comments_id UUID NOT NULL
);

CREATE TABLE post_media
(
    post_id  UUID         NOT NULL,
    media_id VARCHAR(255) NOT NULL
);

CREATE TABLE post_reactions
(
    post_id      UUID NOT NULL,
    reactions_id UUID NOT NULL,
    CONSTRAINT pk_post_reactions PRIMARY KEY (post_id, reactions_id)
);


CREATE TABLE users_user_posts
(
    user_id       UUID NOT NULL,
    user_posts_id UUID NOT NULL
);

ALTER TABLE post_comments
    ADD CONSTRAINT uc_post_comments_comments UNIQUE (comments_id);

ALTER TABLE post_media
    ADD CONSTRAINT uc_post_media_media UNIQUE (media_id);

ALTER TABLE post_reactions
    ADD CONSTRAINT uc_post_reactions_reactions UNIQUE (reactions_id);

ALTER TABLE users_user_posts
    ADD CONSTRAINT uc_users_user_posts_userposts UNIQUE (user_posts_id);

ALTER TABLE post_comments
    ADD CONSTRAINT fk_poscom_on_comment FOREIGN KEY (comments_id) REFERENCES comment (id);

ALTER TABLE post_comments
    ADD CONSTRAINT fk_poscom_on_post FOREIGN KEY (post_id) REFERENCES post (id);

ALTER TABLE post_media
    ADD CONSTRAINT fk_posmed_on_image FOREIGN KEY (media_id) REFERENCES image (id);

ALTER TABLE post_media
    ADD CONSTRAINT fk_posmed_on_post FOREIGN KEY (post_id) REFERENCES post (id);

ALTER TABLE post_reactions
    ADD CONSTRAINT fk_posrea_on_post FOREIGN KEY (post_id) REFERENCES post (id);

ALTER TABLE post_reactions
    ADD CONSTRAINT fk_posrea_on_reaction FOREIGN KEY (reactions_id) REFERENCES reaction (id);

ALTER TABLE users_user_posts
    ADD CONSTRAINT fk_useusepos_on_post FOREIGN KEY (user_posts_id) REFERENCES post (id);

ALTER TABLE users_user_posts
    ADD CONSTRAINT fk_useusepos_on_user FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE users
    ALTER COLUMN created_with_google DROP NOT NULL;

ALTER TABLE users
    ALTER COLUMN is_verified DROP NOT NULL;