package com.lofi.studentrest.rest;

import com.lofi.studentrest.model.Mark;
import com.lofi.studentrest.model.Student;
import com.lofi.studentrest.model.Subject;
import com.lofi.studentrest.service.ClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import static java.lang.Math.abs;
import static java.lang.String.join;
import static java.util.stream.Collectors.joining;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestController
@RequestMapping("/class")
@Validated
public class StudentController {
    private final ClassService classService;

    @Autowired
    public StudentController(ClassService classService) {
        this.classService = classService;
    }

    // ------- Student -------

    @GetMapping("/student")
    public ResponseEntity<Student> getStudent(@RequestParam(name = "studentName") String studentName) {
        try {
            return ResponseEntity.ok(classService.getStudent(studentName));
        }
        catch (Exception e) {
            throw new ResponseStatusException(BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/student")
    public ResponseEntity<String> setStudent(@Valid @RequestBody Student student) {
        try {
            classService.setStudent(student);
            return ResponseEntity.ok("Student saved successfully");
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/student")
    public ResponseEntity<String> updateStudent(@Valid @RequestBody Student student) {
        try {
            classService.updateStudent(student);
            return ResponseEntity.ok("Student updated successfully");
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/student")
    public ResponseEntity<String> deleteStudent(@RequestParam(name = "studentName") String studentName) {
        try {
            classService.deleteStudent(studentName);
            return ResponseEntity.ok("Student deleted successfully");
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ------- Subject -------

    @GetMapping("/student/subjects")
    public ResponseEntity<List<Subject>> getAllSubjects(@RequestParam(name = "studentName") String studentName) {
        try {
            return ResponseEntity.ok(classService.getSubjectsByStudent(studentName));
        }
        catch (Exception e) {
            throw new ResponseStatusException(BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/student/subjects")
    public ResponseEntity<String> setSubject(@RequestParam(name = "studentName") String studentName, @Valid @RequestBody Subject subject) {
        try {
            classService.setSubjectByStudent(studentName, subject);
            return ResponseEntity.ok("Subject saved successfully");
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/student/subjects")
    public ResponseEntity<String> updateSubject(@RequestParam(name = "studentName") String studentName, @Valid @RequestBody Subject subject) {
        try {
            classService.updateSubjectByStudent(studentName, subject);
            return ResponseEntity.ok("Subject updated successfully");
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/student/subjects")
    public ResponseEntity<String> deleteSubject(@RequestParam(name = "studentName") String studentName,
                                         @RequestParam(name = "subjectName") String subjectName) {
        try {
            classService.deleteSubjectByStudent(studentName, subjectName);
            return ResponseEntity.ok("Subject deleted successfully");
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ------- Marks -------

    @GetMapping("/student/subjects/marks")
    public ResponseEntity<List<Mark>> getAllMarks(@RequestParam(name = "studentName") String studentName,
                                           @RequestParam(name = "subjectName") String subjectName) {
        try {
            return ResponseEntity.ok(classService.getMarksBySubjectAndStudent(studentName, subjectName));
        }
        catch (Exception e) {
            throw new ResponseStatusException(BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/student/subjects/marks")
    public ResponseEntity<String> setMarks(@RequestParam(name = "studentName") String studentName,
                                    @RequestParam(name = "subjectName") String subjectName,
                                    @Valid @RequestBody Mark mark) {
        try {
            classService.setMarkBySubjectAndStudent(studentName, subjectName, mark);
            return ResponseEntity.ok("Mark saved successfully");
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/student/subjects/marks")
    public ResponseEntity<String> deleteMark(@RequestParam(name = "studentName") String studentName,
                                      @RequestParam(name = "subjectName") String subjectName,
                                      @RequestParam(name = "mark") Integer mark) {
        try {
            classService.deleteMarkBySubjectAndStudent(studentName, subjectName, mark);
            return ResponseEntity.ok("Mark deleted successfully");
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ------- Report -------

    @GetMapping("/report")
    public void getReport(HttpServletResponse response, @RequestParam(name = "minAvg", required = false) @Min(2) @Max(5) Integer minAvg) {
        response.setContentType("text/csv; charset=utf-8");
        var report = classService.getReport(minAvg != null ? minAvg : 0);
        String separator = ",";
        float eps = 0.0000001f;

        try (PrintWriter writer = response.getWriter()) {
            // write header
            writer.print("name" + separator);
            writer.print(join(separator, classService.getAllSubjects()));
            writer.println();

            // write data
            report.getAvgStudentMarks().forEach((student, avgMarks) -> {
                // if minAvg is given don't show students who have no marks
                if (minAvg == null || avgMarks.stream().mapToDouble(m -> m).sum() > 0d) {
                    writer.print(student + separator);
                    writer.print(avgMarks
                        .stream()
                        .map(m -> abs(m) < eps ? "" : m.toString())
                        .collect(joining(separator)));
                    writer.println();
                }
            });
        }
        catch (IOException e) {
            throw new ResponseStatusException(BAD_REQUEST, "Fail to write csv report: " + e.getMessage());
        }
    }
}
