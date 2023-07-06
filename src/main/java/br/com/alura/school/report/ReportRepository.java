package br.com.alura.school.report;

import br.com.alura.school.section.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ReportRepository extends JpaRepository<Section, Long> {

    @Query(value = "SELECT  c.name AS courseName, s.title AS sectionTitle, " +
            "u.username AS authorName, " +
            "COUNT(v.id) AS totalVideos " +
            "FROM Course c " +
            "INNER JOIN Section s ON c.id = s.course_id " +
            "INNER JOIN User u ON s.author_id = u.id " +
            "LEFT JOIN Video v ON s.id = v.section_id " +
            "INNER JOIN Enrollment e ON c.id = e.course_id " +
            "GROUP BY c.name, s.title, u.username;", nativeQuery = true)
    List<ReportProjection> findSectionByVideosReport();
}
