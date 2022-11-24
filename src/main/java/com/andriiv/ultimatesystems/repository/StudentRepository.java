package com.andriiv.ultimatesystems.repository;

import com.andriiv.ultimatesystems.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Roman_Andriiv
 */
@Repository
public interface StudentRepository extends JpaRepository<Student,Long> {
    List<Student> findByFirstName(String firstName);
    List<Student> findByLastName(String LastName);
}
