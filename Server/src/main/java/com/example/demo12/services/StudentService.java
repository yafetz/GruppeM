package com.example.demo12.services;

import com.example.demo12.entities.Person;
import com.example.demo12.entities.Student;
import com.example.demo12.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {
    @Autowired
    StudentRepository studentRepository;

    public Student getStudentById(Integer id){
        return studentRepository.findStudentById(id);
    }

    public List<Student> getAll(){
        return studentRepository.findAll();
    }


    public Student createStudent(Student student){
        return studentRepository.save(student);
    }

    public Student editStudentAdresse(Integer studentId, String newAdresse){
        Student student = studentRepository.findStudentById(studentId);
        student.setAddresse(newAdresse);
        return student;
    }
    public Student editStudentEmail(Integer studentId, String newEmail){
        Student student = studentRepository.findStudentById(studentId);
        student.setEmail(newEmail);
        return student;
    }

}
