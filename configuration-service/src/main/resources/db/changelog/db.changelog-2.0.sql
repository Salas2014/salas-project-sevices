--liquibase formatted sql

--changeset hasporian:1
CREATE TABLE IF NOT EXISTS tags (
    id SERIAL PRIMARY KEY,
    tag_name VARCHAR(64) NOT NULL,
    priority INT NOT NULL UNIQUE
    );

--changeset hasporian:2
CREATE TABLE IF NOT EXISTS tags_configuration (
    id SERIAL PRIMARY KEY,
    platform_type VARCHAR(16) NOT NULL,
    message VARCHAR(64)
    );

--changeset hasporian:3
CREATE TABLE IF NOT EXISTS tags_configuration_mapping (
    config_id INT NOT NULL,
    tag_id INT NOT NULL,
    CONSTRAINT fk_config FOREIGN KEY (config_id) REFERENCES tags_configuration(id),
    CONSTRAINT fk_tag FOREIGN KEY (tag_id) REFERENCES tags(id)
    );

--changeset hasporian:4
INSERT INTO tags (tag_name, priority) VALUES
                                          ('news', 1),
                                          ('sports', 2),
                                          ('weather', 3),
                                          ('finance', 4),
                                          ('tech', 5),
                                          ('travel', 6),
                                          ('health', 7),
                                          ('education', 8),
                                          ('culture', 9),
                                          ('offers', 10);

--changeset hasporian:5
INSERT INTO tags_configuration (platform_type, message) VALUES
                                                            ('mobile', 'News & Sports highlights'),
                                                            ('desktop', 'Weather + Finance dashboard'),
                                                            ('mobile', 'Tech & Travel onboarding'),
                                                            ('desktop', 'Health + Education block'),
                                                            ('mobile', 'Culture and Offers promo');

--changeset hasporian:6
INSERT INTO tags_configuration_mapping (config_id, tag_id) VALUES
                                                               (1, 1), (1, 2),    -- news + sports
                                                               (2, 3), (2, 4),    -- weather + finance
                                                               (3, 5), (3, 6),    -- tech + travel
                                                               (4, 7), (4, 8),    -- health + education
                                                               (5, 9), (5, 10);   -- culture + offers


