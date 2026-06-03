package com.example.enrolment.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "enrolment")
public class Enrolment {

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


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

    @Id
    @GeneratedValue
    private Long id;

    private int enrolmentYaar;

    private String faculty;

    private String programme;

    public int getStudentcount() {
        return studentcount;
    }

    public void setStudentcount(int studentcount) {
        this.studentcount = studentcount;
    }

    public int getEnrolmentYaar() {
        return enrolmentYaar;
    }

    public void setEnrolmentYaar(int enrolmentYaar) {
        this.enrolmentYaar = enrolmentYaar;
    }

    private int studentcount;
}