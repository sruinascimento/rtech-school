package br.com.alura.school.course;

import br.com.alura.school.section.Section;
import br.com.alura.school.section.SectionRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
class CourseController {
    private final CourseRepository courseRepository;
    private final SectionRepository sectionRepository;

    public CourseController(CourseRepository courseRepository, SectionRepository sectionRepository) {
        this.courseRepository = courseRepository;
        this.sectionRepository = sectionRepository;
    }

    @GetMapping("/courses")
    ResponseEntity<List<CourseResponse>> allCourses() {
        List<CourseResponse> courseResponses = courseRepository.findAll().stream()
                .map(CourseResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(courseResponses);
    }

    @GetMapping("/courses/{code}")
    ResponseEntity<List<CourseReportResponse>> courseByCode(@PathVariable("code") String code) {
        Course course = courseRepository.findByCode(code).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, format("Course with code %s not found", code)));
        List<Section> sections = sectionRepository.findByCourse_Code(course.getCode());
        List<CourseReportResponse> coursesResponse = sections.stream()
                .map(section ->
                        new CourseReportResponse(section.getCourse().getName(),
                                section.getTitle(),
                                section.getAuthor().getUsername(),
                                section.getVideos()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(coursesResponse);
    }

    @PostMapping("/courses")
    ResponseEntity<Void> newCourse(@RequestBody @Valid NewCourseRequest newCourseRequest) {
        courseRepository.save(newCourseRequest.toEntity());
        URI location = URI.create(format("/courses/%s", newCourseRequest.getCode()));
        return ResponseEntity.created(location).build();
    }

}
