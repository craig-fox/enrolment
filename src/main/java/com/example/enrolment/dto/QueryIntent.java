package com.example.enrolment.dto;

public class QueryIntent {
    private String faculty;

    private String programme;

    private Integer enrolmentYear;

    private String groupBy;

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public String getProgramme() {
        return programme;
    }

    public void setProgramme(String programme) {
        this.programme = programme;
    }

    public Integer getEnrolmentYear() {
        return enrolmentYear;
    }

    public void setEnrolmentYear(Integer enrolmentYear) {
        this.enrolmentYear = enrolmentYear;
    }

    public String getGroupBy() {
        return groupBy;
    }

    public void setGroupBy(String groupBy) {
        this.groupBy = groupBy;
    }

}
