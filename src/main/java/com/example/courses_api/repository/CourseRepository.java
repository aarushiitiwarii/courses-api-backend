package com.example.courses_api.repository;

import com.example.courses_api.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;          // ← add this import
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {

    Optional<Course> findByCourseId(String courseId);

    boolean existsByCourseId(String courseId);

    List<Course> findByPrerequisitesContaining(Course course);
    
    boolean existsByPrerequisitesContaining(Course course);

    @Query("SELECT c FROM Course c JOIN c.prerequisites p WHERE p.courseId = :courseId")
    List<Course> findCoursesUsingCourseIdAsPrerequisite(@Param("courseId") String courseId);


}
