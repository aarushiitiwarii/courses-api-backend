package com.example.courses_api.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String courseId;

    @Column(length = 2000)
    private String description;

    @ManyToMany
    private List<Course> prerequisites;

    @Column(name = "academic_year") // renamed to avoid SQL keyword conflict
    private int academicYear;

    private int semester;

    // Constructors
    public Course() {}

    public Course(String title, String courseId, String description, List<Course> prerequisites, int academicYear, int semester) {
        this.title = title;
        this.courseId = courseId;
        this.description = description;
        this.prerequisites = prerequisites;
        this.academicYear = academicYear;
        this.semester = semester;
    }

    // Getters and setters
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getCourseId() { return courseId; }
    public String getDescription() { return description; }
    public List<Course> getPrerequisites() { return prerequisites; }

    public void setId(Long id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setCourseId(String courseId) { this.courseId = courseId; }
    public void setDescription(String description) { this.description = description; }
    public void setPrerequisites(List<Course> prerequisites) { this.prerequisites = prerequisites; }

    public int getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(int academicYear) {
        this.academicYear = academicYear;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }
}
