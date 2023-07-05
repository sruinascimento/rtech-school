package br.com.alura.school.enrollment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

public class NewEnrollmentRequest {
    @NotBlank(message = "o Campo nome não pode estar vazio")
    @JsonProperty
    private final String username;

    @JsonIgnore
    private final LocalDate dateEnrollment;

    public NewEnrollmentRequest(@JsonProperty("username") String username) {
        this.username = username;
        this.dateEnrollment = LocalDate.now();
    }

    public String getUsername() {
        return username;
    }

    public LocalDate getDateEnrollment() {
        return dateEnrollment;
    }

    @Override
    public String toString() {
        return "NewEnrollmentRequest{" +
                "username='" + username + '\'' +
                ", dateEnrollment=" + dateEnrollment +
                '}';
    }
}
