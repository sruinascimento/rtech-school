package br.com.alura.school.enrollment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    Optional<Enrollment> findByUser_UsernameAndCourse_Code(String username, String courseCode);
}
