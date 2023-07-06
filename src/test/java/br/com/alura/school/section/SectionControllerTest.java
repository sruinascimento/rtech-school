package br.com.alura.school.section;

import br.com.alura.school.course.Course;
import br.com.alura.school.course.CourseRepository;
import br.com.alura.school.user.User;
import br.com.alura.school.user.UserRepository;
import br.com.alura.school.user.UserRole;
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
public class SectionControllerTest {
    private final ObjectMapper jsonMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    CourseRepository courseRepository;

    @AfterAll
    public static void limparBaseDeDados(@Autowired WebApplicationContext webApplicationContext) {
        CourseRepository courseRepository = webApplicationContext.getBean(CourseRepository.class);
        UserRepository userRepository = webApplicationContext.getBean(UserRepository.class);
        SectionRepository sectionRepository = webApplicationContext.getBean(SectionRepository.class);

        sectionRepository.deleteAll();
        courseRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void should_add_new_section() throws Exception {

        courseRepository.save(
                new Course("jpa",
                        "Hibernate",
                        "Mapeamento, Relações"));

        userRepository.save(
                new User("javinha", "javinha@email.com", UserRole.INSTRUCTOR)
        );

        NewSectionRequest newSectionRequest = new NewSectionRequest("map-join",
                "Relacionamentos", "javinha");


        mockMvc.perform(post("/courses/jpa/sections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(newSectionRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    void should_not_allow_duplication_of_section() throws Exception {
        NewSectionRequest newSectionRequest = new NewSectionRequest("map-join",
                "Relacionamentos", "javinha");


        mockMvc.perform(post("/courses/jpa/sections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(newSectionRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_not_allow_section_title_less_than_five_letters() throws Exception {
        NewSectionRequest newSectionRequest = new NewSectionRequest("teste-java",
                "Java", "javinha");

        mockMvc.perform(post("/courses/jpa/sections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(newSectionRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_allow_section_title_more_than_four_letters() throws Exception {
        NewSectionRequest newSectionRequest = new NewSectionRequest("teste-java",
                "Java 20", "javinha");

        mockMvc.perform(post("/courses/jpa/sections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(newSectionRequest)))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void should_validate_invalid_section_course_not_found_request() throws Exception {
        NewSectionRequest newSectionRequest = new NewSectionRequest("teste-java",
                "Java 20", "javinha");

        mockMvc.perform(post("/courses/invalid/sections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(newSectionRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_validate_invalid_section_author_not_found_request() throws Exception {
        NewSectionRequest newSectionRequest = new NewSectionRequest("nova-aula",
                "Java 20", "invalid");

        mockMvc.perform(post("/courses/jpa/sections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(newSectionRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_validate_invalid_section_author_not_instructor_request() throws Exception {   userRepository.save(
            new User("piton", "piton@email.com", UserRole.DEV)
    );

        NewSectionRequest newSectionRequest = new NewSectionRequest("nova-aula",
                "Java 20", "piton");

        mockMvc.perform(post("/courses/jpa/sections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(newSectionRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

}
