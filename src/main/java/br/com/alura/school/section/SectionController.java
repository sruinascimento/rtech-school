package br.com.alura.school.section;

import br.com.alura.school.course.Course;
import br.com.alura.school.course.CourseRepository;
import br.com.alura.school.user.User;
import br.com.alura.school.user.UserRepository;
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
    ResponseEntity<?> newSection(@PathVariable("code") String code,
                                @RequestBody @Valid NewSectionRequest newSectionRequest) {

        Course course = courseRepository.findByCode(code)
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, format("Course with code %s not found", code)));

        User user = userRepository.findByUsername(newSectionRequest.getAuthorUsername())
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, format("User with username %s not found", newSectionRequest.getAuthorUsername())));

        if (user.isntInstructor()) {
            return ResponseEntity.badRequest().body(format("Author %s isnt Instructor", user.getUsername()));
        }

        Section section = newSectionRequest.toEntity(course, user);

        sectionRepository.save(section);

        return ResponseEntity.status(CREATED).body(new NewSectionResponse(section));
    }
}
