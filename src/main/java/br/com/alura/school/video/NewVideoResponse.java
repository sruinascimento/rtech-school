package br.com.alura.school.video;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NewVideoResponse {
    @JsonProperty
    private final String video;

    public NewVideoResponse(Video video) {
        this.video = video.getUrl();
    }
}
