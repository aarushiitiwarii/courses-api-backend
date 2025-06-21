package com.example.courses_api.service;

import com.example.courses_api.dto.CourseInstanceRequest;
import com.example.courses_api.entity.Course;
import com.example.courses_api.entity.CourseInstance;
import com.example.courses_api.repository.CourseInstanceRepository;
import com.example.courses_api.repository.CourseRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CourseInstanceService {

    private final CourseInstanceRepository instanceRepo;
    private final CourseRepository courseRepo;

    public CourseInstanceService(CourseInstanceRepository instanceRepo, CourseRepository courseRepo) {
        this.instanceRepo = instanceRepo;
        this.courseRepo = courseRepo;
    }

    public CourseInstance create(CourseInstanceRequest req) {
        if (instanceRepo.existsByAcademicYearAndSemesterAndCourse_CourseId(
                req.academicYear(), req.semester(), req.courseId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Instance already exists");
        }

        Course course = courseRepo.findByCourseId(req.courseId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Course ID not found"));

        return instanceRepo.save(new CourseInstance(
            req.academicYear(), req.semester(), course
        ));
    }

    public List<CourseInstance> list(int year, int sem) {
        return instanceRepo.findByAcademicYearAndSemester(year, sem);
    }

    public CourseInstance one(int year, int sem, String courseId) {
        return instanceRepo.findByAcademicYearAndSemesterAndCourse_CourseId(year, sem, courseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public void delete(int year, int sem, String courseId) {
        CourseInstance instance = one(year, sem, courseId);
        instanceRepo.delete(instance);
    }
}
