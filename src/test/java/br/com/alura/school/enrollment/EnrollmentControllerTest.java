package br.com.alura.school.enrollment;

import br.com.alura.school.course.Course;
import br.com.alura.school.course.CourseRepository;
import br.com.alura.school.user.User;
import br.com.alura.school.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class EnrollmentControllerTest {

    private final ObjectMapper jsonMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    // ...

    @AfterAll
    public static void limparBaseDeDados(@Autowired WebApplicationContext webApplicationContext) {
        CourseRepository courseRepository = webApplicationContext.getBean(CourseRepository.class);
        UserRepository userRepository = webApplicationContext.getBean(UserRepository.class);
        EnrollmentRepository enrollmentRepository = webApplicationContext.getBean(EnrollmentRepository.class);

        enrollmentRepository.deleteAll();
        courseRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void should_add_new_enrollment() throws Exception {

        courseRepository.save(
                new Course("data-s",
                        "Data Structure",
                        "Array, Queue, Stack"));

        userRepository.save(
                new User("ploop", "ploop@email.com")
        );

        NewEnrollmentRequest newEnrollmentRequest = new NewEnrollmentRequest("ploop");

        mockMvc.perform(post("/courses/data-s/enroll")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(newEnrollmentRequest)))
                .andExpect(status().isCreated());

    }


    @Test
    void should_not_allow_duplication_of_enrollment() throws Exception {
        NewEnrollmentRequest newEnrollmentRequest = new NewEnrollmentRequest("ploop");

        mockMvc.perform(post("/courses/data-s/enroll")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(newEnrollmentRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_not_allow_username_blank_of_enrollment() throws Exception {
        NewEnrollmentRequest newEnrollmentRequest = new NewEnrollmentRequest("");

        mockMvc.perform(post("/courses/data-s/enroll")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(newEnrollmentRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_validate_invalid_course_bad_enroll_requests() throws Exception {
        NewEnrollmentRequest newEnrollmentRequest = new NewEnrollmentRequest("ploop");

        mockMvc.perform(post("/courses/invalid/enroll")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(newEnrollmentRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

}
