package br.com.alura.school.video;

import br.com.alura.school.course.Course;
import br.com.alura.school.course.CourseRepository;
import br.com.alura.school.section.Section;
import br.com.alura.school.section.SectionRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

import static br.com.alura.school.support.validation.ErrorMessageValidation.getErrorMessageValidation;

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
                               @Valid @RequestBody NewVideoRequest newVideoRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(getErrorMessageValidation(bindingResult));
        }

        Optional<Course> optionalCourse = courseRepository.findByCode(courseCode);
        if (optionalCourse.isEmpty()) {
            return ResponseEntity.badRequest().body("Curso não encontrado");
        }

        Optional<Section> optionalSection = sectionRepository.findByCode(sectionCode);
        if (optionalSection.isEmpty()) {
            return ResponseEntity.badRequest().body("Aula não encontrada");
        }

        Section section = optionalSection.get();
        if (section.videoExists(newVideoRequest.getVideo())) {
            return ResponseEntity.badRequest().body("Vídeo repetido");
        }

        Video video = newVideoRequest.toEntity();
        video.setSection(section);
        videoRepository.save(video);
        section.addVideo(video);
        sectionRepository.save(section);
        System.out.println(video);
        System.out.println(section);

        return ResponseEntity.ok(new NewVideoResponse(video));
    }
}
