package br.com.alura.school.enrollment;

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
import br.com.alura.school.support.validation.ErrorMessageValidation;

import javax.validation.Valid;
import java.util.Optional;

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
                                 @RequestBody @Valid NewEnrollmentRequest newEnrollmentRequest,
                                 BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(ErrorMessageValidation.getErrorMessageValidation(bindingResult));
        }

        Optional<Course> optionalCourse = courseRepository.findByCode(courseCode);

        if (optionalCourse.isEmpty()) {
            return ResponseEntity.badRequest().body("Curso inválido.");

        }

        Optional<User> optionalUser = userRepository.findByUsername(newEnrollmentRequest.getUsername());
        if(optionalUser.isEmpty()) {
            return ResponseEntity.badRequest().body("Usuário inválido.");

        }

        Optional<Enrollment> optionalEnrollment = enrollmentRepository.findByUser_UsernameAndCourse_Code(newEnrollmentRequest.getUsername(), courseCode);
        if (optionalEnrollment.isPresent()) {
            return ResponseEntity.badRequest().body("Aluno já matriculado no curso.");
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setUser(optionalUser.get());
        enrollment.setEnrollmentDate(newEnrollmentRequest.getDateEnrollment());
        enrollment.setCourse(optionalCourse.get());

        enrollmentRepository.save(enrollment);

        return ResponseEntity.created(null).build();

    }
}
