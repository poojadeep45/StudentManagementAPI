package com.example.StudentManagementAPI.controller;

import com.example.StudentManagementAPI.Entity.Student;
import com.example.StudentManagementAPI.service.StudentService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/all")
public class StudentController {

    @Autowired
    private StudentService studentService;

    private final Logger logger = LoggerFactory.getLogger(StudentController.class);

    @PostMapping("/students/save")
    public ResponseEntity<Student> saveStudent(@Valid @RequestBody Student student){
        Student savedStudent = studentService.saveStudent(student);
        logger.info("Student saved successfully: {} " + savedStudent.getStudentEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedStudent);
    }

    @GetMapping("/students/fetch")
    public ResponseEntity<Page<Student>> fetchStudentList(Pageable pageable) {
        return ResponseEntity.ok(studentService.fetchStudentList(pageable));
    }

//    @GetMapping("/students/fetch")
//    public ResponseEntity<List<Student>> fetchStudentList(){
//        logger.info("Fetch Student list successfully ");
//        return ResponseEntity.ok(studentService.fetchStudentList());
//    }

    @GetMapping("/students/fetch/{id}")
    public ResponseEntity<Student> fetchStudentById(@PathVariable("id") Long studentId){
        return ResponseEntity.ok(studentService.fetchStudentById(studentId));
    }

    @GetMapping("/students/fetch/email/{email}")
    public ResponseEntity<Optional<Student>> fetchStudentByEmail(@PathVariable("email") String studentEmail){
        return ResponseEntity.ok(studentService.fetchStudentByEmail(studentEmail));
    }

    @GetMapping("/students/fetch/name/{name}")
    public ResponseEntity<List<Student>> fetchStudentByName(@PathVariable("name") String studentName){
        return ResponseEntity.ok(studentService.fetchStudentByName(studentName));
    }

    @GetMapping("/students/fetch/count")
    public ResponseEntity<Long> countStudents(){
        logger.info("Count Student successful");
        return ResponseEntity.ok(studentService.countStudents());
    }

    @GetMapping("/students/fetch/nic/{nic}")
    public ResponseEntity<Student> getByNIC(@PathVariable("nic") String studentNIC) {
        return ResponseEntity.ok(studentService.getByNIC(studentNIC));
    }

    @DeleteMapping("/students/delete/{id}")
    public ResponseEntity<String> deleteStudentById(@PathVariable("id") Long studentId){
        studentService.deleteStudentById(studentId);
        logger.info("Student deleted successfully: {}" + studentId);
        return ResponseEntity.ok("Deleted student with id " + studentId);
    }

    @PutMapping("/students/update/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable("id") Long studentId, @Valid @RequestBody Student student){
        logger.info("Student updated successfully: {}" + studentId);
        return ResponseEntity.ok(studentService.updateStudent(studentId , student));
    }


}
