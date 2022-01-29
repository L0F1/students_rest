package com.lofi.studentrest.service;

import com.lofi.studentrest.model.Mark;
import com.lofi.studentrest.model.Report;
import com.lofi.studentrest.model.Student;
import com.lofi.studentrest.model.Subject;
import com.lofi.studentrest.repository.ClassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class ClassServiceImpl implements ClassService {
    private final ClassRepository classRepository;

    @Autowired
    public ClassServiceImpl(ClassRepository classRepository) {
        this.classRepository = classRepository;
    }

    @Override
    public Student getStudent(String name) {
        return classRepository.getStudent(name);
    }

    @Override
    public void setStudent(Student student) {
        classRepository.setStudent(student);
    }

    @Override
    public void updateStudent(Student student) {
        classRepository.updateStudent(student);
    }

    @Override
    public void deleteStudent(String name) {
        classRepository.deleteStudent(name);
    }

    @Override
    public List<Subject> getSubjectsByStudent(String studentName) {
        return classRepository.getSubjectsByStudent(studentName);
    }

    @Override
    public void setSubjectByStudent(String studentName, Subject subject) {
        classRepository.setSubjectByStudent(studentName, subject);
    }

    @Override
    public void updateSubjectByStudent(String studentName, Subject subject) {
        classRepository.updateSubjectByStudent(studentName, subject);
    }

    @Override
    public void deleteSubjectByStudent(String studentName, String subjectName) {
        classRepository.deleteSubjectByStudent(studentName, subjectName);
    }

    @Override
    public List<Mark> getMarksBySubjectAndStudent(String studentName, String subjectName) {
        return classRepository.getMarksBySubjectAndStudent(studentName, subjectName);
    }

    @Override
    public void setMarkBySubjectAndStudent(String studentName, String subjectName, Mark mark) {
        classRepository.setMarkBySubjectAndStudent(studentName, subjectName, mark);
    }

    @Override
    public void deleteMarkBySubjectAndStudent(String studentName, String subjectName, Integer mark) {
        classRepository.deleteMarkBySubjectAndStudent(studentName, subjectName, mark);
    }

    @Override
    public Report getReport(Integer minAvg) {
        return classRepository.getReport(minAvg);
    }

    @Override
    public Set<String> getAllSubjects() {
        return classRepository.getAllSubjects();
    }
}
