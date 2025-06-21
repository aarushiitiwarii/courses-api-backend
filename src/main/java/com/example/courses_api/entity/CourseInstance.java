package com.example.courses_api.entity;

import jakarta.persistence.*;

@Entity
@Table(
    name = "course_instance",
    uniqueConstraints = @UniqueConstraint(
        columnNames = {"academicYear", "semester", "course_id"}
    )
)
public class CourseInstance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int academicYear;
    private int semester;

    @ManyToOne(optional = false)
    @JoinColumn(name = "course_id")
    private Course course;

    public CourseInstance() {}

    public CourseInstance(int academicYear, int semester, Course course) {
        this.academicYear = academicYear;
        this.semester = semester;
        this.course = course;
    }

    public Long getId() { return id; }
    public int getAcademicYear() { return academicYear; }
    public void setAcademicYear(int academicYear) { this.academicYear = academicYear; }

    public int getSemester() { return semester; }
    public void setSemester(int semester) { this.semester = semester; }

    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }
}
