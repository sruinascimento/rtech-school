package br.com.alura.school.section;

import br.com.alura.school.support.validation.Unique;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class NewSectionRequest {
    @Unique(entity = Section.class, field = "code", message = "Código da aula deve ser único")
    @NotBlank(message = "Código não pode estar em branco")
    @JsonProperty
    private final String code;

    @Size(min=5, message = "Título deve ter no mínimo 5 caracteres")
    @NotBlank(message = "Título não pode estar em branco")
    @JsonProperty
    private final String title;

    @NotBlank(message = "Username do autor não pode estar em branco")
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

    public Section toEntity() {
        return new Section(code, title);
    }
}
