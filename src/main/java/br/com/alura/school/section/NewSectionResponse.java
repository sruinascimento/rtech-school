package br.com.alura.school.section;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NewSectionResponse {
    @JsonProperty
    private final Long id;
    @JsonProperty
    private final String code;
    @JsonProperty
    private final String title;
    @JsonProperty
    private final String authorUsername;
    @JsonProperty
    private final String courseName;

    public NewSectionResponse(Section section) {
        this.id = section.getId();
        this.code = section.getCode();
        this.title = section.getTitle();
        this.authorUsername = section.getAuthor().getUsername();
        this.courseName = section.getCourse().getName();
    }
}
