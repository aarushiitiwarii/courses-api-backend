package com.example.courses_api.controller;

import com.example.courses_api.dto.CourseRequest;
import com.example.courses_api.entity.Course;
import com.example.courses_api.entity.CourseInstance;
import com.example.courses_api.repository.CourseRepository;
import com.example.courses_api.repository.CourseInstanceRepository;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseRepository courseRepo;
    private final CourseInstanceRepository instanceRepo;

    public CourseController(CourseRepository courseRepo,
                            CourseInstanceRepository instanceRepo) {
        this.courseRepo = courseRepo;
        this.instanceRepo = instanceRepo;
    }

    /* ---------- POST /api/courses ---------- */
    @PostMapping
    public ResponseEntity<?> create(@RequestBody CourseRequest req) {

        if (courseRepo.existsByCourseId(req.courseId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Course ID already exists");
        }

        List<Course> prereqs = new ArrayList<>();
        for (String pid : Optional.ofNullable(req.prerequisiteIds()).orElse(List.of())) {
            Course p = courseRepo.findByCourseId(pid).orElse(null);
            if (p == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Prerequisite not found: " + pid);
            }
            prereqs.add(p);
        }

        Course saved = courseRepo.save(
            new Course(
                req.title(),
                req.courseId(),
                req.description(),
                prereqs,
                req.academicYear(),
                req.semester()
            )
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    /* ---------- GET /api/courses ---------- */
    @GetMapping
    public List<Course> all() {
        return courseRepo.findAll();
    }

    /* ---------- GET /api/courses/{courseId} ---------- */
    @GetMapping("/{courseId}")
    public ResponseEntity<?> one(@PathVariable String courseId) {
        return courseRepo.findByCourseId(courseId)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Course not found"));
    }

    /* ---------- DELETE /api/courses/{courseId} ---------- */
    @DeleteMapping("/{courseId}")
    public ResponseEntity<?> deleteCourse(
            @PathVariable String courseId,
            @RequestParam(name = "force", defaultValue = "false") boolean force
    ) {

        Course course = courseRepo.findByCourseId(courseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        // ✅ Debug: Show which courses use this course as a prerequisite
        List<Course> using = courseRepo.findCoursesUsingCourseIdAsPrerequisite(courseId);
        System.out.println("▶️ Checking usage of " + courseId + " as prerequisite… found: " + using.size());
        for (Course c : using) {
            System.out.println(" - " + c.getCourseId());
        }

        boolean usedAsPrereq = !using.isEmpty();
        if (usedAsPrereq && !force) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Cannot delete: course is a prerequisite for other courses.");
        }

        // ✅ Check for existing instances of this course
        List<CourseInstance> instances = instanceRepo.findByCourse(course);
        if (!instances.isEmpty() && !force) {
            List<String> offered = instances.stream()
                    .map(ci -> ci.getAcademicYear() + "-S" + ci.getSemester())
                    .toList();
            return ResponseEntity.status(HttpStatus.CONFLICT).body(offered);
        }

        // ✅ Cascade delete instances, then course
        instanceRepo.deleteAll(instances);
        courseRepo.delete(course);

        return ResponseEntity.ok("Course deleted");
    }

    /* ---------- PUT /api/courses/{courseId} ---------- */
    @PutMapping("/{courseId}")
    public ResponseEntity<?> update(@PathVariable String courseId, @RequestBody CourseRequest req) {

        Optional<Course> existingOpt = courseRepo.findByCourseId(courseId);
        if (existingOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found");
        }

        Course existing = existingOpt.get();
        existing.setTitle(req.title());
        existing.setDescription(req.description());
        existing.setAcademicYear(req.academicYear());
        existing.setSemester(req.semester());

        List<Course> prereqs = new ArrayList<>();
        for (String pid : Optional.ofNullable(req.prerequisiteIds()).orElse(List.of())) {
            Course p = courseRepo.findByCourseId(pid).orElse(null);
            if (p == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Prerequisite not found: " + pid);
            }
            prereqs.add(p);
        }
        existing.setPrerequisites(prereqs);

        courseRepo.save(existing);
        return ResponseEntity.ok(existing);
    }
}
