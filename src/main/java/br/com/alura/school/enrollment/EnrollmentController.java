package br.com.alura.school.enrollment;

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
public class EnrollmentController {

    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;

    private final UserRepository userRepository;

    public EnrollmentController(EnrollmentRepository enrollmentRepository, CourseRepository courseRepository, UserRepository userRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/courses/{courseCode}/enroll")
    ResponseEntity<?> addEnrollment(@PathVariable("courseCode") String courseCode,
                                 @RequestBody @Valid NewEnrollmentRequest newEnrollmentRequest) {

        Course course = courseRepository.findByCode(courseCode)
                .orElseThrow(
                        () -> new ResponseStatusException(BAD_REQUEST, format("Course with code %s not found", courseCode)));

        User user = userRepository.findByUsername(newEnrollmentRequest.getUsername())
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, format("User with username %s not found", newEnrollmentRequest.getUsername())));

        enrollmentRepository.findByUser_UsernameAndCourse_Code(newEnrollmentRequest.getUsername(), courseCode)
                .ifPresent(existingEnrollment -> {
                    throw new ResponseStatusException(CONFLICT, format("Student with username %s already enrolled in course %s", newEnrollmentRequest.getUsername(), courseCode));
                });

        enrollmentRepository.save(newEnrollmentRequest.toEntity(user, course));

        return ResponseEntity.status(CREATED).build();

    }
}
