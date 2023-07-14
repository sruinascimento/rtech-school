package br.com.alura.school.course;

import br.com.alura.school.section.Section;
import br.com.alura.school.video.NewVideoResponse;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.stream.Collectors;

public class CourseReportResponse {
    @JsonProperty
    private final String courseName;

    @JsonProperty
    private final String sectionTile;

    @JsonProperty
    private final String sectionAuthor;

    @JsonProperty
    private final List<NewVideoResponse> videos;

    public CourseReportResponse(Section section) {
        this.courseName = section.getCourse().getName();
        this.sectionTile = section.getTitle();
        this.sectionAuthor = section.getAuthor().getUsername();
        this.videos = section.getVideos().stream()
                .map(NewVideoResponse::new)
                .collect(Collectors.toList());
    }
}
