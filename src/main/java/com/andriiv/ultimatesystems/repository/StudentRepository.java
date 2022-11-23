package com.andriiv.ultimatesystems.repository;

import com.andriiv.ultimatesystems.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Roman_Andriiv
 */
@Repository
public interface StudentRepository extends JpaRepository<Student,Long> {
}
