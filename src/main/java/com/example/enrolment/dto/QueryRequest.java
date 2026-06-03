package com.example.enrolment.dto;

public class QueryRequest {

    private final String queryText;

    public QueryRequest(String queryText) {
        this.queryText = queryText;
    }

    public String getQueryText() {
        return queryText;
    }
}
