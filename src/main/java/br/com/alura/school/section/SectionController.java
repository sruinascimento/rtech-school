package br.com.alura.school.section;

import br.com.alura.school.course.Course;
import br.com.alura.school.course.CourseRepository;
import br.com.alura.school.user.User;
import br.com.alura.school.user.UserRepository;
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
public class SectionController {
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final SectionRepository sectionRepository;

    public SectionController(CourseRepository courseRepository, UserRepository userRepository, SectionRepository sectionRepository) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
        this.sectionRepository = sectionRepository;
    }

    @PostMapping("/courses/{code}/sections")
    ResponseEntity<?> newLesson(@PathVariable("code") String code,
                                @RequestBody @Valid NewSectionRequest newSectionRequest,
                                BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(getErrorMessageValidation(bindingResult));
        }

        Optional<Course> optionalCourse = courseRepository.findByCode(code);

        if (optionalCourse.isEmpty()) {
            return ResponseEntity.badRequest().body("Curso não encontrado");
        }

        Optional<User> optionalAuthor = userRepository.findByUsername(newSectionRequest.getAuthorUsername());

        if (optionalAuthor.isEmpty()) {
            return ResponseEntity.badRequest().body("Autor não encontrado");
        }

        if (optionalAuthor.get().isntInstructor()) {
            return ResponseEntity.badRequest().body("Autor não é instrutor");
        }

        Section section = newSectionRequest.toEntity();
        section.setCourse(optionalCourse.get());
        section.setAuthor(optionalAuthor.get());
        sectionRepository.save(section);

        return ResponseEntity.created(null).body(new NewSectionResponse(section));
    }
}
