package br.com.alura.school.video;

import br.com.alura.school.course.CourseRepository;
import br.com.alura.school.section.Section;
import br.com.alura.school.section.SectionRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.*;

@RestController
public class VideoController {
    private final CourseRepository courseRepository;
    private final SectionRepository sectionRepository;
    private final VideoRepository videoRepository;

    public VideoController(CourseRepository courseRepository, SectionRepository sectionRepository, VideoRepository videoRepository) {
        this.courseRepository = courseRepository;
        this.sectionRepository = sectionRepository;
        this.videoRepository = videoRepository;
    }

    @PostMapping("/courses/{courseCode}/sections/{sectionCode}")
    ResponseEntity<?> newVideo(@PathVariable("courseCode") String courseCode, @PathVariable("sectionCode") String sectionCode,
                               @Valid @RequestBody NewVideoRequest newVideoRequest) {

        courseRepository.findByCode(courseCode)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, format("Course with code %s not found", courseCode)));

        Section section = sectionRepository.findByCode(sectionCode)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, format("Section with code %s not found", sectionCode)));

        videoRepository.findByUrl(newVideoRequest.getVideo())
                .ifPresent(existingVideo -> {
                    throw new ResponseStatusException(CONFLICT, format("Video %s already registred", newVideoRequest.getVideo()));
                });

        Video video = newVideoRequest.toEntity(section);
//        video.setSection(section);
        videoRepository.save(video);
//        section.addVideo(video);
//        sectionRepository.save(section);
        return ResponseEntity.ok(new NewVideoResponse(video));
    }
}
