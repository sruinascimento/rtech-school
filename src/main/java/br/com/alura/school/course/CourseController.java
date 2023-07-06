package br.com.alura.school.course;

import br.com.alura.school.section.Section;
import br.com.alura.school.section.SectionRepository;
import br.com.alura.school.section.NewSectionResponse;
import br.com.alura.school.section.NewSectionRequest;
import br.com.alura.school.user.User;
import br.com.alura.school.user.UserRepository;
import br.com.alura.school.video.NewVideoRequest;
import br.com.alura.school.video.NewVideoResponse;
import br.com.alura.school.video.Video;
import br.com.alura.school.video.VideoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.validation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
class CourseController {

    private final CourseRepository courseRepository;

    private final SectionRepository sectionRepository;

    private final UserRepository userRepository;

    private final VideoRepository videoRepository;

    public CourseController(CourseRepository courseRepository, SectionRepository sectionRepository, UserRepository userRepository, VideoRepository videoRepository) {
        this.courseRepository = courseRepository;
        this.sectionRepository = sectionRepository;
        this.userRepository = userRepository;
        this.videoRepository = videoRepository;
    }

    @GetMapping("/courses")
    ResponseEntity<List<CourseResponse>> allCourses() {
        List<CourseResponse> courseResponses = courseRepository.findAll().stream()
                .map(CourseResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(courseResponses);
    }

    @GetMapping("/courses/{code}")
    ResponseEntity<CourseResponse> courseByCode(@PathVariable("code") String code) {
        Course course = courseRepository.findByCode(code).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, format("Course with code %s not found", code)));
        return ResponseEntity.ok(new CourseResponse(course));
    }

    @PostMapping("/courses")
    ResponseEntity<Void> newCourse(@RequestBody @Valid NewCourseRequest newCourseRequest) {
        courseRepository.save(newCourseRequest.toEntity());
        URI location = URI.create(format("/courses/%s", newCourseRequest.getCode()));
        return ResponseEntity.created(location).build();
    }

    private List<String> getErrorMessageValidation(BindingResult bindingResult) {
        return bindingResult.getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());
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
