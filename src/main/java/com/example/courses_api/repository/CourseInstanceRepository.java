package com.example.courses_api.repository;

import com.example.courses_api.entity.Course;
import com.example.courses_api.entity.CourseInstance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CourseInstanceRepository extends JpaRepository<CourseInstance, Long> {

    List<CourseInstance> findByAcademicYearAndSemester(int year, int semester);

    Optional<CourseInstance> findByAcademicYearAndSemesterAndCourse_CourseId(
            int year, int semester, String courseId
    );

    boolean existsByAcademicYearAndSemesterAndCourse_CourseId(
            int year, int semester, String courseId
    );

    /* ─── NEW helpers used for cascade‑delete checks ─── */

    // Is THIS course used in ANY instance?
    boolean existsByCourse(Course course);

    // Fetch all instances that reference THIS course
    List<CourseInstance> findByCourse(Course course);
}
