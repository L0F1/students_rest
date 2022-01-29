package com.lofi.studentrest.model;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Subject entity
 */
public class Subject {
    @NotBlank(message = "Name is required")
    String title;
    List<Mark> marks = new ArrayList<>();

    public Subject() {
    }

    public Subject(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Mark> getMarks() {
        return marks;
    }

    public void setMarks(List<Mark> marks) {
        this.marks = marks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Subject subject = (Subject) o;
        return title.equals(subject.title) &&
            marks.equals(subject.marks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, marks);
    }
}
