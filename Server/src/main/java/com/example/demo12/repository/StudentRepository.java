package com.example.demo12.repository;

import com.example.demo12.entities.Student;
import com.example.demo12.services.StudentService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer>{
    Student findStudentById(Integer id);
    List<Student> findAllByIdBetweenOrderByMatrikelnummer(Integer start, Integer finish);

}
