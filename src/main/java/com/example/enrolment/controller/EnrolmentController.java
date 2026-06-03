package com.example.enrolment.controller;

import com.example.enrolment.model.Enrolment;
import com.example.enrolment.service.EnrolmentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class EnrolmentController {

    private final EnrolmentService enrolmentService;

    public EnrolmentController(EnrolmentService enrolmentService) {
        this.enrolmentService = enrolmentService;
    }

    @GetMapping("/enrolments")
    public List<Enrolment> getAllEnrolments() {
        return enrolmentService.getAllEnrolments();
    }
}
