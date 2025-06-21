package com.example.courses_api.dto;

import java.util.List;

public record CourseRequest(
    String title,
    String courseId,
    String description,
    List<String> prerequisiteIds,
    int academicYear,  
    int semester
) {}