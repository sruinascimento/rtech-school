CREATE TABLE Lesson (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        code VARCHAR(10) NOT NULL UNIQUE,
                        title VARCHAR(20) NOT NULL,
                        author_id BIGINT NOT NULL,
                        course_id BIGINT NOT NULL,
                        FOREIGN KEY (author_id) REFERENCES User(id),
                        FOREIGN KEY (course_id) REFERENCES Course(id)
);