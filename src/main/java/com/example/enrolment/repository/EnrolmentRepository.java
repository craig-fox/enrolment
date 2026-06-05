package com.example.enrolment.repository;

import com.example.enrolment.model.Enrolment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EnrolmentRepository extends JpaRepository<Enrolment, Long> {

    @Query("""
            SELECT e FROM Enrolment e
            WHERE LOWER(e.faculty) LIKE LOWER(CONCAT('%', :queryText, '%'))
               OR LOWER(e.programme) LIKE LOWER(CONCAT('%', :queryText, '%'))
               OR CAST(e.enrolmentYear AS string) LIKE CONCAT('%', :queryText, '%')
            """)
    List<Enrolment> findByQueryText(@Param("queryText") String queryText);

    List<Enrolment> findByFaculty(String faculty);

    List<Enrolment> findByProgramme(String programme);

    List<Enrolment> findByEnrolmentYear(int enrolmentYear);
}
