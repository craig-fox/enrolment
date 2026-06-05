package com.example.enrolment.service;

import com.example.enrolment.dto.QueryIntent;
import com.example.enrolment.model.Enrolment;
import com.example.enrolment.repository.EnrolmentRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

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
    private final ObjectMapper objectMapper = new ObjectMapper();

    public EnrolmentService(
            EnrolmentRepository enrolmentRepository,
            WebClient openAiWebClient,
            @Value("${openai.model}") String openAiModel) {
        this.enrolmentRepository = enrolmentRepository;
        this.openAiWebClient = openAiWebClient;
        this.openAiModel = openAiModel;
    }

    public List<Enrolment> getAll() {
        return enrolmentRepository.findAll();
    }

    public QueryIntent processQuestion(String question) {
        if (question == null || question.isBlank()) {

            throw new IllegalArgumentException("Question must not be blank");
    
        }
    
        log.info("Sending question to OpenAI: {}", question);
    
        String prompt = """
                You are translating university enrolment questions into JSON.
                Available fields:
                - faculty
                - programme
                - enrolmentYear
                - studentcount
    
                Return ONLY valid JSON.
    
                Example response:
    
                {
                  "faculty": "Engineering",
                  "groupBy": "enrolmentYear"
                }
    
                User question:
    
                """ + question;
    
        var requestBody = Map.of(
                "model", openAiModel,
                "messages", List.of(
                        Map.of("role", "system", "content", "Return only JSON."),
                        Map.of("role", "user", "content", prompt)
                )
    
        );
    
        JsonNode response = openAiWebClient.post()
                .uri("/v1/chat/completions")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();
    
        if (response == null) {
            throw new IllegalStateException("Empty response from OpenAI");
        }
    
        String content = response.path("choices")
                .path(0)
                .path("message")
                .path("content")
                .asText();
    
        log.info("OpenAI returned: {}", content);
    
        try {
            return objectMapper.readValue(content, QueryIntent.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse OpenAI response", e);
        }
    }
}
