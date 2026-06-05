package com.example.enrolment.controller;

import com.example.enrolment.dto.QueryRequest;
import com.example.enrolment.service.EnrolmentService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EnrolmentController {

    private final EnrolmentService enrolmentService;

    public EnrolmentController(EnrolmentService enrolmentService) {
        this.enrolmentService = enrolmentService;
    }

    @PostMapping("/query")
    public Object query(@RequestBody QueryRequest request) {
        return enrolmentService.processQuestion(request.getQuestion());
    }
}
