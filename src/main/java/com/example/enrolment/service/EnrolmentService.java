package com.example.enrolment.service;

import com.example.enrolment.model.Enrolment;
import com.example.enrolment.repository.EnrolmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnrolmentService {

    private final EnrolmentRepository enrolmentRepository;

    public EnrolmentService(EnrolmentRepository enrolmentRepository) {
        this.enrolmentRepository = enrolmentRepository;
    }

    public List<Enrolment> getAllEnrolments() {
        return enrolmentRepository.findAll();
    }
}