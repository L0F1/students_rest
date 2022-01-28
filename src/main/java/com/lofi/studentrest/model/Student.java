package com.lofi.studentrest.model;

import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Student entity
 */
public class Student {
    @NotBlank(message = "Name is required")
    String lastName;
    Map<String, List<Integer>> subjectAndMarks = new HashMap<>();

    public Student() {
    }

    public Student(String lastName, Map<String, List<Integer>> subjectAndMarks) {
        this.lastName = lastName;
        this.subjectAndMarks = subjectAndMarks;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Map<String, List<Integer>> getSubjectAndMarks() {
        return subjectAndMarks;
    }

    public void setSubjectAndMarks(Map<String, List<Integer>> subjectAndMarks) {
        this.subjectAndMarks = subjectAndMarks;
    }
}
