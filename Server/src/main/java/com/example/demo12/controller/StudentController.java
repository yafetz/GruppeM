package com.example.demo12.controller;


import com.example.demo12.entities.Student;
import com.example.demo12.repository.StudentRepository;
import com.example.demo12.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RestController
@RequestMapping(path = "/student")
public class StudentController {
    @Autowired
    StudentService studentService;



    @GetMapping("/all")
    public ResponseEntity<List<Student>> getAll(){

        return new ResponseEntity<>(studentService.getAll(), HttpStatus.OK);
    }
    @PostMapping(path = "/add",
    produces = "application/json",
            consumes = "application/json"
    )
    public ResponseEntity<Student> createNewStudent(@RequestBody Student student){
        return new ResponseEntity<>(studentService.createStudent(student), HttpStatus.CREATED);

    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Student> getById(@PathVariable Integer id){
        return new ResponseEntity<>(studentService.getStudentById(id), HttpStatus.OK);

    }




}
