CREATE TABLE Section (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        code VARCHAR(255) NOT NULL,
                        title VARCHAR(255) NOT NULL,
                        author_id BIGINT NOT NULL,
                        course_id BIGINT NOT NULL,
                        FOREIGN KEY (author_id) REFERENCES User (id),
                        FOREIGN KEY (course_id) REFERENCES Course (id)
);


