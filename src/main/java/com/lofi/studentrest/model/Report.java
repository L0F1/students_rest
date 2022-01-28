package com.lofi.studentrest.model;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Report entity
 */
public class Report {
    Map<String, List<Float>> avgStudentMarks = new TreeMap<>();

    public Report() {
    }

    public Report(Map<String, List<Float>> avgStudentMarks) {
        this.avgStudentMarks = avgStudentMarks;
    }

    public Map<String, List<Float>> getAvgStudentMarks() {
        return avgStudentMarks;
    }

    public void setAvgStudentMarks(Map<String, List<Float>> avgStudentMarks) {
        this.avgStudentMarks = avgStudentMarks;
    }
}
