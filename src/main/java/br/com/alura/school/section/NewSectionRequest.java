package br.com.alura.school.section;

import br.com.alura.school.course.Course;
import br.com.alura.school.support.validation.Unique;
import br.com.alura.school.user.User;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class NewSectionRequest {
    @Unique(entity = Section.class, field = "code", message = "Section code must be unique")
    @NotBlank(message = "Section code cant be blank")
    @JsonProperty
    private final String code;

    @Size(min=5, message = "Title size must be min five letter")
    @NotBlank(message = "Title cant be blank")
    @JsonProperty
    private final String title;

    @NotBlank(message = "Author username cant be blank")
    @JsonProperty
    private final String authorUsername;

    public NewSectionRequest(String code, String title, String authorUsername) {
        this.code = code;
        this.title = title;
        this.authorUsername = authorUsername;
    }

    public String getAuthorUsername() {
        return authorUsername;
    }

    @Override
    public String toString() {
        return "NewLessonRequest{" +
                "code='" + code + '\'' +
                ", title='" + title + '\'' +
                ", authorUsername='" + authorUsername + '\'' +
                '}';
    }

    public Section toEntity(Course course, User author) {
        return new Section(code, title, author, course);
    }
}
