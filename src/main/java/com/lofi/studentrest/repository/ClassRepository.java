package com.lofi.studentrest.repository;

import com.lofi.studentrest.model.Mark;
import com.lofi.studentrest.model.Report;
import com.lofi.studentrest.model.Student;
import com.lofi.studentrest.model.Subject;

import java.util.List;
import java.util.Set;

/**
 * Repository for retrieve and map data from json file to POJOs
 */
public interface ClassRepository {
    Student getStudent(String studentName);

    void setStudent(Student student);

    void updateStudent(Student student);

    void deleteStudent(String studentName);

    List<Subject> getSubjectsByStudent(String studentName);

    void setSubjectByStudent(String studentName, Subject subject);

    void updateSubjectByStudent(String studentName, Subject subject);

    void deleteSubjectByStudent(String studentName, String subjectName);

    List<Mark> getMarksBySubjectAndStudent(String studentName, String subjectName);

    void setMarkBySubjectAndStudent(String studentName, String subjectName, Mark mark);

    void deleteMarkBySubjectAndStudent(String studentName, String subjectName, Integer mark);

    Report getReport(Integer minAvg);

    Set<String> getAllSubjects();
}
