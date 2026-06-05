package com.example.enrolment.dto;

import java.util.List;

import com.example.enrolment.model.Enrolment;

public class QueryResponse {
    private QueryIntent intent;

    private List<Enrolment> results;

    public QueryResponse(QueryIntent intent, List<Enrolment> results) {

        this.intent = intent;

        this.results = results;

    }

    public QueryIntent getIntent() {

        return intent;

    }

    public void setIntent(QueryIntent intent) {

        this.intent = intent;

    }

    public List<Enrolment> getResults() {

        return results;

    }

    public void setResults(List<Enrolment> results) {

        this.results = results;

    }
}
