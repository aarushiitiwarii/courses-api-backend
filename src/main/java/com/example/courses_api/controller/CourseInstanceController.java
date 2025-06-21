package com.example.courses_api.controller;

import com.example.courses_api.dto.CourseInstanceRequest;
import com.example.courses_api.entity.CourseInstance;
import com.example.courses_api.service.CourseInstanceService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/instances")
@CrossOrigin(origins = "http://localhost:5173")
public class CourseInstanceController {

    private final CourseInstanceService svc;

    public CourseInstanceController(CourseInstanceService svc) {
        this.svc = svc;
    }

    @PostMapping
    public CourseInstance create(@RequestBody CourseInstanceRequest req) {
        return svc.create(req);
    }

    @GetMapping("/{year}/{sem}")
    public List<CourseInstance> list(@PathVariable int year, @PathVariable int sem) {
        return svc.list(year, sem);
    }

    @GetMapping("/{year}/{sem}/{courseId}")
    public CourseInstance one(@PathVariable int year, @PathVariable int sem,
                              @PathVariable String courseId) {
        return svc.one(year, sem, courseId);
    }

    @DeleteMapping("/{year}/{sem}/{courseId}")
    public void delete(@PathVariable int year, @PathVariable int sem,
                       @PathVariable String courseId) {
        svc.delete(year, sem, courseId);
    }
}
