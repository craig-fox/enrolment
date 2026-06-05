package com.example.enrolment.service;

import com.example.enrolment.dto.QueryIntent;
import com.example.enrolment.dto.QueryResponse;
import com.example.enrolment.model.Enrolment;
import com.example.enrolment.repository.EnrolmentRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class EnrolmentService {

    private final EnrolmentRepository enrolmentRepository;
    private final RestClient openAiRestClient;
    private final String openAiModel;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public EnrolmentService(
            EnrolmentRepository enrolmentRepository,
            RestClient openAiRestClient,
            @Value("${openai.model}") String openAiModel) {
        this.enrolmentRepository = enrolmentRepository;
        this.openAiRestClient = openAiRestClient;
        this.openAiModel = openAiModel;
    }

    public List<Enrolment> getAll() {
        return enrolmentRepository.findAll();
    }

    public QueryResponse processQuestion(String question) {

        validateQuestion(question);
    
        log.info("Sending question to OpenAI: {}", question);
    
        String prompt = buildPrompt(question);
    
        JsonNode response = callOpenAi(prompt);
    
        String content = extractContent(response);
    
        log.info("OpenAI returned: {}", content);
    
        QueryIntent intent = parseIntent(content);
    
        List<Enrolment> results = executeIntent(intent);
    
        return new QueryResponse(intent, results);
    }
    
    private void validateQuestion(String question) {
        if (question == null || question.isBlank()) {
            throw new IllegalArgumentException("Question must not be blank");
        }
    }
    
    private String buildPrompt(String question) {
        return """
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
    }
    
    private JsonNode callOpenAi(String prompt) {
    
        var requestBody = Map.of(
                "model", openAiModel,
                "messages", List.of(
                        Map.of("role", "system", "content", "Return only JSON."),
                        Map.of("role", "user", "content", prompt)
                )
        );
    
        JsonNode response = openAiRestClient.post()
                .uri("/v1/chat/completions")
                .body(requestBody)
                .retrieve()
                .body(JsonNode.class);
    
        if (response == null) {
            throw new IllegalStateException("Empty response from OpenAI");
        }
    
        return response;
    }
    
    private String extractContent(JsonNode response) {
        return response.path("choices")
                .path(0)
                .path("message")
                .path("content")
                .asText()
                .replace("```json", "")
                .replace("```", "")
                .trim();
    }
    
    private QueryIntent parseIntent(String content) {
        try {
            return objectMapper.readValue(content, QueryIntent.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse OpenAI response", e);
        }
    }
    
    private List<Enrolment> executeIntent(QueryIntent intent) {
    
        if (intent.getFaculty() != null) {
            return enrolmentRepository.findByFaculty(intent.getFaculty());
        }
    
        if (intent.getProgramme() != null) {
            return enrolmentRepository.findByProgramme(intent.getProgramme());
        }
    
        if (intent.getEnrolmentYear() != null) {
            return enrolmentRepository.findByEnrolmentYear(intent.getEnrolmentYear());
        }
    
        return enrolmentRepository.findAll();
    }
    

 
}
