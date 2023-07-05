CREATE TABLE Video(
                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      url VARCHAR(500) NOT NULL UNIQUE,
                      section_id BIGINT NOT NULL,
                      FOREIGN KEY (section_id) REFERENCES Section (id)
);