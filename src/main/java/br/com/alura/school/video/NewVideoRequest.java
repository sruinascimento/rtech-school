package br.com.alura.school.video;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotBlank;

public class NewVideoRequest {

    @NotBlank(message = "video não pode estar em branco.")
    @JsonProperty
    private final String video;

    @JsonCreator
    public NewVideoRequest(@JsonProperty("video") String video) {
        this.video = video;
    }

    public String getVideo() {
        return video;
    }

    public Video toEntity() {
        return new Video(video);
    }

    @Override
    public String toString() {
        return "NewVideoRequest{" +
                "video='" + video + '\'' +
                '}';
    }
}
