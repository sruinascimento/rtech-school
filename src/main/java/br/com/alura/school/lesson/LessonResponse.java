package br.com.alura.school.lesson;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LessonResponse {
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

    public LessonResponse(Lesson lesson) {
        this.id = lesson.getId();
        this.code = lesson.getCode();
        this.title = lesson.getTitle();
        this.authorUsername = lesson.getAuthor().getUsername();
        this.courseName = lesson.getCourse().getName();
    }
}
