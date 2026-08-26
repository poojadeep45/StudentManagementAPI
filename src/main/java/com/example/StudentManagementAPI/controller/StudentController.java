package com.example.StudentManagementAPI.controller;

import com.example.StudentManagementAPI.Entity.Student;
import com.example.StudentManagementAPI.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "Students" , description = "Manage student records — create, fetch, update, and delete")
@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    private final Logger logger = LoggerFactory.getLogger(StudentController.class);

    @Operation(summary = "Create new Student" , description = "Saves a new student record and returns the persisted entity with its generated ID")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Student created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation failed on the request body")
    })
    @PostMapping("/save")
    public ResponseEntity<Student> saveStudent(@Valid @RequestBody Student student){
        Student savedStudent = studentService.saveStudent(student);
        logger.info("Student saved successfully: {} " + savedStudent.getStudentEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedStudent);
    }

    @Operation(summary = "Fetch all students (paginated)", description = "Returns a page of students; supports standard pagination params (page, size, sort)")
    @GetMapping("/fetch")
    public ResponseEntity<Page<Student>> fetchStudentList(Pageable pageable) {
        return ResponseEntity.ok(studentService.fetchStudentList(pageable));
    }

//    @GetMapping("/students/fetch")
//    public ResponseEntity<List<Student>> fetchStudentList(){
//        logger.info("Fetch Student list successfully ");
//        return ResponseEntity.ok(studentService.fetchStudentList());
//    }

    @Operation(summary = "Fetch a student by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Student found"),
            @ApiResponse(responseCode = "404", description = "No student exists with the given ID")
    })
    @GetMapping("/fetch/{id}")
    public ResponseEntity<Student> fetchStudentById(@PathVariable("id") Long studentId){
        return ResponseEntity.ok(studentService.fetchStudentById(studentId));
    }

    @Operation(summary = "Fetch a student by email address")
    @GetMapping("/fetch/email/{email}")
    public ResponseEntity<Optional<Student>> fetchStudentByEmail(@PathVariable("email") String studentEmail){
        return ResponseEntity.ok(studentService.fetchStudentByEmail(studentEmail));
    }

    @Operation(summary = "Fetch students by name", description = "Returns all students matching the given name (may return multiple results)")
    @GetMapping("/fetch/name/{name}")
    public ResponseEntity<List<Student>> fetchStudentByName(@PathVariable("name") String studentName){
        return ResponseEntity.ok(studentService.fetchStudentByName(studentName));
    }

    @Operation(summary = "Count total students", description = "Returns the total number of student records in the system")
    @GetMapping("/fetch/count")
    public ResponseEntity<Long> countStudents(){
        logger.info("Count Student successful");
        return ResponseEntity.ok(studentService.countStudents());
    }

    @Operation(summary = "Fetch a student by NIC (national ID)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Student found"),
            @ApiResponse(responseCode = "404", description = "No student exists with the given NIC")
    })
    @GetMapping("/fetch/nic/{nic}")
    public ResponseEntity<Student> getByNIC(@PathVariable("nic") String studentNIC) {
        return ResponseEntity.ok(studentService.getByNIC(studentNIC));
    }

    @Operation(summary = "Delete a student by ID", description = "Permanently removes a student record. Requires ADMIN role.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Student deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Caller lacks ADMIN role"),
            @ApiResponse(responseCode = "404", description = "No student exists with the given ID")
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteStudentById(@PathVariable("id") Long studentId){
        studentService.deleteStudentById(studentId);
        logger.info("Student deleted successfully: {}" + studentId);
        return ResponseEntity.ok("Deleted student with id " + studentId);
    }

    @Operation(summary = "Update an existing student", description = "Replaces the student record identified by ID with the given payload")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Student updated successfully"),
            @ApiResponse(responseCode = "400", description = "Validation failed on the request body"),
            @ApiResponse(responseCode = "404", description = "No student exists with the given ID")
    })
    @PutMapping("/update/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable("id") Long studentId, @Valid @RequestBody Student student){
        logger.info("Student updated successfully: {}" + studentId);
        return ResponseEntity.ok(studentService.updateStudent(studentId , student));
    }
}
