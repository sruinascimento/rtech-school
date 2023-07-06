package br.com.alura.school.report;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ReportResponse {
    @JsonProperty
    private final String courseName;

    @JsonProperty
    private final String sectionTitle;
    @JsonProperty
    private final String authorName;
    @JsonProperty
    private final Long totalVideos;

    public ReportResponse(String courseName, String sectionTitle, String authorName, Long totalVideos) {
        this.courseName = courseName;
        this.sectionTitle = sectionTitle;
        this.authorName = authorName;
        this.totalVideos = totalVideos;
    }

    @Override
    public String toString() {
        return "ReportResponse{" +
                "courseName='" + courseName + '\'' +
                ", sectionTitle='" + sectionTitle + '\'' +
                ", authorName='" + authorName + '\'' +
                ", totalVideos=" + totalVideos +
                '}';
    }
}
