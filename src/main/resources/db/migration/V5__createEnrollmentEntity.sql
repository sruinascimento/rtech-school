CREATE TABLE Enrollment(
                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      user_id BIGINT NOT NULL,
                      course_id BIGINT NOT NULL,
                      enrollment_date DATE NOT NULL,
                      UNIQUE(user_id, course_id),
                      FOREIGN KEY (user_id) REFERENCES User (id),
                      FOREIGN KEY (course_id) REFERENCES Course (id)
);