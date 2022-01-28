package com.lofi.studentrest.model;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

/**
 * Subject entity
 */
public class Subject {
    @NotBlank(message = "Name is required")
    String title;

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
}
