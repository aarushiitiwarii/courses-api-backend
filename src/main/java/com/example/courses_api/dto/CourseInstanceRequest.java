package com.example.courses_api.dto;

public record CourseInstanceRequest(
    String courseId,
    int academicYear,
    int semester
) { }
