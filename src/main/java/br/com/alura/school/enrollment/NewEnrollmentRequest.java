package br.com.alura.school.enrollment;

import br.com.alura.school.course.Course;
import br.com.alura.school.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

public class NewEnrollmentRequest {
    @NotBlank(message = "The username cant be blank")
    @JsonProperty
    private final String username;

    @JsonIgnore
    private final LocalDate dateEnrollment = LocalDate.now();

    public NewEnrollmentRequest(@JsonProperty("username") String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public Enrollment toEntity(User user, Course course) {
        return new Enrollment(dateEnrollment, user, course);
    }

    @Override
    public String toString() {
        return "NewEnrollmentRequest{" +
                "username='" + username + '\'' +
                ", dateEnrollment=" + dateEnrollment +
                '}';
    }
}
