package com.example.enrolment.service;

import com.example.enrolment.model.Enrolment;
import com.example.enrolment.repository.EnrolmentRepository;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class EnrolmentService {

    private final EnrolmentRepository enrolmentRepository;
    private final WebClient openAiWebClient;
    private final String openAiModel;

    public EnrolmentService(
            EnrolmentRepository enrolmentRepository,
            WebClient openAiWebClient,
            @Value("${openai.model}") String openAiModel) {
        this.enrolmentRepository = enrolmentRepository;
        this.openAiWebClient = openAiWebClient;
        this.openAiModel = openAiModel;
    }

    public List<Enrolment> findEnrolments(String question) {
        log.info("Query text for LLM: {}", question);

        if (question == null || question.isBlank()) {
            return enrolmentRepository.findAll();
        }

        return enrolmentRepository.findByQueryText(question.trim());
    }

    public String processQuestion(String question) {
        if (question == null || question.isBlank()) {
            throw new IllegalArgumentException("Question must not be blank");
        }

        log.info("Sending question to OpenAI: {}", question);

        var requestBody = Map.of(
                "model", openAiModel,
                "messages", List.of(Map.of("role", "user", "content", question)));

        JsonNode response = openAiWebClient.post()
                .uri("/v1/chat/completions")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();

        if (response == null) {
            throw new IllegalStateException("Empty response from OpenAI");
        }

        return response.path("choices").path(0).path("message").path("content").asText();
    }
}
