package br.com.alura.school.course;

import br.com.alura.school.section.Section;
import br.com.alura.school.section.SectionRepository;
import br.com.alura.school.video.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestController {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private VideoRepository videoRepository;

    @GetMapping("/teste")
    public List<?> getAll () {
        List<Section> sections = sectionRepository.findAll();
        sections.forEach(System.out::println);
        return  null;

    }

}
