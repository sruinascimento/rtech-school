package br.com.alura.school.course;

import br.com.alura.school.video.NewVideoResponse;
import br.com.alura.school.video.Video;
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

    public CourseReportResponse(String courseName, String sectionTile, String sectionAuthor, List<Video> videos) {
        this.courseName = courseName;
        this.sectionTile = sectionTile;
        this.sectionAuthor = sectionAuthor;
        this.videos = videos.stream()
                .map(NewVideoResponse::new)
                .collect(Collectors.toList());
    }
}
