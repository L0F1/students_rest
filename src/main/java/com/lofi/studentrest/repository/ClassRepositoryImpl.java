package com.lofi.studentrest.repository;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lofi.studentrest.model.Mark;
import com.lofi.studentrest.model.Report;
import com.lofi.studentrest.model.Student;
import com.lofi.studentrest.model.Subject;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

@Repository
public class ClassRepositoryImpl implements ClassRepository {
    private static final File JSON_FILE = new File("class.json");
    private static final String STUDENT_NOT_EXIST_BAD_REQUEST = "There is no student with the name '%s'";
    private static final String STUDENT_EXIST_BAD_REQUEST = "Student '%s' already exist";
    private static final String SUBJECT_NOT_EXIST_BAD_REQUEST = "There is no subject '%s'";
    private static final String SUBJECT_EXIST_BAD_REQUEST = "Subject '%s' already exist";
    private static final String MARK_NOT_EXIST_BAD_REQUEST = "There is no mark '%d'";
    private static final Set<String> ALL_SUBJECTS = new TreeSet<>();

    public ClassRepositoryImpl() {
        updateAllSubjects(readAllStudents());
    }

    @Override
    public synchronized Student getStudent(String studentName) {
        return readAllStudents()
            .stream()
            .filter(s -> s.getLastName().equals(studentName))
            .findFirst()
            .orElseThrow(() -> new NoSuchElementException(format(STUDENT_NOT_EXIST_BAD_REQUEST, studentName)));
    }

    @Override
    public synchronized void setStudent(Student student) {
        List<Student> students = readAllStudents();
        checkStudentNotExist(students, student.getLastName());
        students.add(student);
        writeAllStudents(students);
    }

    @Override
    public synchronized void updateStudent(Student student) {
        List<Student> students = readAllStudents();
        checkStudentExist(students, student.getLastName());
        writeAllStudents(students
            .stream()
            .map(s -> {
                if (s.getLastName().equals(student.getLastName())) {
                    s.setSubjectAndMarks(student.getSubjectAndMarks());
                }
                return s;
            })
            .collect(toList())
        );
    }

    @Override
    public synchronized void deleteStudent(String studentName) {
        List<Student> students = readAllStudents();
        checkStudentExist(students, studentName);
        writeAllStudents(students
            .stream()
            .filter(s -> !s.getLastName().equals(studentName))
            .collect(toList())
        );
    }

    @Override
    public synchronized List<Subject> getSubjectsByStudent(String studentName) {
        return getStudent(studentName)
            .getSubjectAndMarks()
            .keySet()
            .stream()
            .map(Subject::new)
            .collect(toList());
    }

    @Override
    public synchronized void setSubjectByStudent(String studentName, Subject subject) {
        List<Student> students = readAllStudents();
        checkStudentExist(students, studentName);
        writeAllStudents(students
            .stream()
            .map(s -> {
                if (s.getLastName().equals(studentName)) {
                    var subjectsAndMarks = s.getSubjectAndMarks();
                    String title = subject.getTitle();
                    checkSubjectNotExist(subjectsAndMarks, title);
                    subjectsAndMarks.put(title, new ArrayList<>());
                    s.setSubjectAndMarks(subjectsAndMarks);
                }
                return s;
            })
            .collect(toList())
        );
        updateAllSubjects(students);
    }

    @Override
    public synchronized void deleteSubjectByStudent(String studentName, String subjectName) {
        List<Student> students = readAllStudents();
        checkStudentExist(students, studentName);
        writeAllStudents(students
            .stream()
            .map(s -> {
                if (s.getLastName().equals(studentName)) {
                    var subjectsAndMarks = s.getSubjectAndMarks();
                    checkSubjectExist(subjectsAndMarks, subjectName);
                    subjectsAndMarks.remove(subjectName);
                    s.setSubjectAndMarks(subjectsAndMarks);
                }
                return s;
            })
            .collect(toList())
        );
        updateAllSubjects(students);
    }

    @Override
    public synchronized List<Mark> getMarksBySubjectAndStudent(String studentName, String subjectName) {
        return Optional.of(getStudent(studentName).getSubjectAndMarks().get(subjectName))
            .orElseThrow(() -> new NoSuchElementException(format(SUBJECT_NOT_EXIST_BAD_REQUEST, subjectName)))
            .stream()
            .map(Mark::new)
            .collect(toList());
    }

    @Override
    public synchronized void setMarksBySubjectAndStudent(String studentName, String subjectName, Mark mark) {
        List<Student> students = readAllStudents();
        checkStudentExist(students, studentName);
        writeAllStudents(students
            .stream()
            .map(s -> {
                if (s.getLastName().equals(studentName)) {
                    var subjectsAndMarks = s.getSubjectAndMarks();
                    checkSubjectExist(subjectsAndMarks, subjectName);
                    subjectsAndMarks.get(subjectName).add(mark.getValue());
                    s.setSubjectAndMarks(subjectsAndMarks);
                }
                return s;
            })
            .collect(toList())
        );
    }

    @Override
    public synchronized void deleteMarkBySubjectAndStudent(String studentName, String subjectName, Integer mark) {
        List<Student> students = readAllStudents();
        checkStudentExist(students, studentName);
        writeAllStudents(students
            .stream()
            .map(s -> {
                if (s.getLastName().equals(studentName)) {
                    var subjectsAndMarks = s.getSubjectAndMarks();
                    checkSubjectExist(subjectsAndMarks, subjectName);
                    var marks = subjectsAndMarks.get(subjectName);
                    removeMarkOrElseThrow(marks, mark);
                    subjectsAndMarks.put(subjectName, marks);
                    s.setSubjectAndMarks(subjectsAndMarks);
                }
                return s;
            })
            .collect(toList())
        );
    }

    @Override
    public Report getReport(Integer minAvg) {
        Report report = new Report();
        List<Student> students = readAllStudents();
        students.forEach(student -> {
                var avgMarks = new ArrayList<Float>();
                var subjects = student.getSubjectAndMarks();
                var subjectTitles = subjects.keySet();
                ALL_SUBJECTS.forEach(subject -> {
                    if (subjectTitles.contains(subject)) {
                        var marks = subjects.get(subject);
                        if (!marks.isEmpty()) {
                            float avgMark = (marks.stream()
                                .mapToInt(m -> m)
                                .sum()) / (float) marks.size();

                            avgMarks.add(avgMark >= minAvg ? avgMark : 0f);
                        }
                        else {
                            avgMarks.add(0f);
                        }
                    }
                    else {
                        avgMarks.add(0f);
                    }
                });
                report.getAvgStudentMarks().put(student.getLastName(), avgMarks);
                });
        return report;
    }

    @Override
    public Set<String> getAllSubjects() {
        return new TreeSet<>(ALL_SUBJECTS);
    }

    // ===================================================================================================================
    // = Implementation
    // ===================================================================================================================

    private void updateAllSubjects(List<Student> students) {
        ALL_SUBJECTS.clear();
        students.forEach(s -> ALL_SUBJECTS.addAll(s.getSubjectAndMarks().keySet()));
    }

    private boolean checkStudent(List<Student> students, String studentName) {
        return students
            .stream()
            .anyMatch(s -> s.getLastName().equals(studentName));
    }

    private void checkStudentExist(List<Student> students, String studentName) {
        if (!checkStudent(students, studentName)) {
            throw new NoSuchElementException(format(STUDENT_NOT_EXIST_BAD_REQUEST, studentName));
        }
    }

    private void checkStudentNotExist(List<Student> students, String studentName) {
        if (checkStudent(students, studentName)) {
            throw new ElementAlreadyExistException(format(STUDENT_EXIST_BAD_REQUEST, studentName));
        }
    }

    private void checkSubjectExist(Map<String, List<Integer>> subjectsAndMarks, String subjectName) {
        if (subjectsAndMarks.get(subjectName) == null){
            throw new NoSuchElementException(format(SUBJECT_NOT_EXIST_BAD_REQUEST, subjectName));
        }
    }

    private void checkSubjectNotExist(Map<String, List<Integer>> subjectsAndMarks, String subjectName) {
        if (subjectsAndMarks.get(subjectName) != null){
            throw new ElementAlreadyExistException(format(SUBJECT_EXIST_BAD_REQUEST, subjectName));
        }
    }

    private void removeMarkOrElseThrow(List<Integer> marks, Integer mark) {
        if (!marks.remove(mark)) {
            throw new NoSuchElementException(format(MARK_NOT_EXIST_BAD_REQUEST, mark));
        }
    }

    private List<Student> readAllStudents() {
        try {
            return new ArrayList<>(asList(new ObjectMapper().readValue(JSON_FILE, Student[].class)));
        }
        catch (IOException e) {
            throw new NoSuchElementException("Fail to read file: " + e.getMessage());
        }
    }

    private void writeAllStudents(List<Student> students) {
        try {
            (new ObjectMapper().writer(new DefaultPrettyPrinter())).writeValue(JSON_FILE, students);
        }
        catch (IOException e) {
            throw new NoSuchElementException("Fail to write to file: " + e.getMessage());
        }
    }
}
