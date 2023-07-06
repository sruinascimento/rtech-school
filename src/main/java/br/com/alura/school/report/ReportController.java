package br.com.alura.school.report;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ReportController {
    private final ReportRepository reportRepository;

    public ReportController(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    @GetMapping("/sectionByVideosReport")
    ResponseEntity<?> getSectionByVideosReport() {
        List<ReportProjection> reportData = reportRepository.findSectionByVideosReport();

        if(reportData.isEmpty()) {
            System.out.println("Sem cursos com matrículas");
            return ResponseEntity.noContent().build();
        }

        List<ReportResponse> sectionByVideos = reportData.stream()
                .map(reportProjection ->
                        new ReportResponse(reportProjection.getCourseName(),
                                reportProjection.getSectionTitle(),
                                reportProjection.getAuthorName(),
                                reportProjection.getTotalVideos()))
                .collect(Collectors.toList());
        System.out.println("Report");
        sectionByVideos.forEach(System.out::println);

        return ResponseEntity.ok().body(sectionByVideos);
    }
}
