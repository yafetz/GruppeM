package com.example.demo12.controller;

import com.example.demo12.entities.Lehrender;
import com.example.demo12.entities.Student;
import com.example.demo12.repository.LehrenderRepository;
import com.example.demo12.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
@RestController
@RequestMapping(path = "/lehrende")
public class LehrenderController {
    @Autowired
    LehrenderRepository lehrenderRepository;


    @GetMapping("/all")
    public List<Lehrender> getStudents(){

        List<Lehrender> lehrendelist = lehrenderRepository.findAll();
        return lehrendelist;
    }
}
