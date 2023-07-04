package br.com.alura.school.course;

import br.com.alura.school.lesson.Lesson;
import br.com.alura.school.lesson.LessonRepository;
import br.com.alura.school.lesson.LessonResponse;
import br.com.alura.school.lesson.NewLessonRequest;
import br.com.alura.school.user.User;
import br.com.alura.school.user.UserRepository;
import br.com.alura.school.user.UserRole;
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

    private final LessonRepository lessonRepository;

    private final UserRepository userRepository;

    public CourseController(CourseRepository courseRepository, LessonRepository lessonRepository, UserRepository userRepository) {
        this.courseRepository = courseRepository;
        this.lessonRepository = lessonRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/courses")
    ResponseEntity<List<CourseResponse>> allCourses() {
        List<Course> courses = courseRepository.findAll();
        List<CourseResponse> courseResponses = courses.stream()
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

    @PostMapping("/courses/{code}/sections")
    ResponseEntity<?> newLesson(@PathVariable("code") String code,
                                @RequestBody @Valid NewLessonRequest newLessonRequest,
                                BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(getErrorMessageValidation(bindingResult));
        }

        Optional<Course> optionalCourse = courseRepository.findByCode(code);

        if (optionalCourse.isEmpty()) {
            return ResponseEntity.badRequest().body("Curso não encontrado");
        }

        Optional<User> optionalAuthor = userRepository.findByUsername(newLessonRequest.getAuthorUsername());

        if (optionalAuthor.isEmpty()) {
            return ResponseEntity.badRequest().body("Autor não encontrado");
        }

        if (optionalAuthor.get().isntInstructor()) {
            return ResponseEntity.badRequest().body("Autor não é instrutor");
        }

        Lesson lesson = newLessonRequest.toEntity();
        lesson.setCourse(optionalCourse.get());
        lesson.setAuthor(optionalAuthor.get());
        lessonRepository.save(lesson);

        return ResponseEntity.ok(new LessonResponse(lesson));
    }

    private List<String> getErrorMessageValidation(BindingResult bindingResult) {
        return bindingResult.getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());
    }
}
