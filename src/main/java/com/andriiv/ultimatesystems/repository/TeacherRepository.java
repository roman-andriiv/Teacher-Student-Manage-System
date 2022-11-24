package com.andriiv.ultimatesystems.repository;

import com.andriiv.ultimatesystems.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Roman_Andriiv
 */
@Repository
public interface TeacherRepository extends JpaRepository<Teacher,Long> {
    List<Teacher> findByFirstName(String firstName);

    List<Teacher> findByLastName(String lastName);

}
