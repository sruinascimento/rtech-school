package br.com.alura.school.video;

import br.com.alura.school.section.Section;
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

    public Video toEntity(Section section) {
        return new Video(video, section);
    }

    @Override
    public String toString() {
        return "NewVideoRequest{" +
                "video='" + video + '\'' +
                '}';
    }
}
