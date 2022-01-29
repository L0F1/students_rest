package com.lofi.studentrest.model;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

/**
 * Student entity
 */
public class Student {
    @NotBlank(message = "Name is required")
    String lastName;
    @Valid
    List<Subject> subjects = new ArrayList<>();

    public Student() {
    }

    public Student(String lastName, List<Subject> subjects) {
        this.lastName = lastName;
        this.subjects = subjects;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }
}
