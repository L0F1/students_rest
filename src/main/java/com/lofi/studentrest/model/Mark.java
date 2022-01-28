package com.lofi.studentrest.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Mark entity
 */
public class Mark {
    @Min(value = 2)
    @Max(value = 5)
    @NotNull(message = "Value is required")
    private Integer value;

    public Mark() {
    }

    public Mark(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
