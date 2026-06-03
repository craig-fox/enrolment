package com.example.enrolment.service;

import com.example.enrolment.dto.QueryRequest;
import com.example.enrolment.model.Enrolment;
import com.example.enrolment.repository.EnrolmentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class EnrolmentService {

    private final EnrolmentRepository enrolmentRepository;

    public EnrolmentService(EnrolmentRepository enrolmentRepository) {
        this.enrolmentRepository = enrolmentRepository;
    }

    public List<Enrolment> findEnrolments(QueryRequest request) {
        String queryText = request.getQueryText();
        log.info("Query text for LLM: {}", queryText);

        if (queryText == null || queryText.isBlank()) {
            return enrolmentRepository.findAll();
        }

        return enrolmentRepository.findByQueryText(queryText.trim());
    }
}
