package com.andriiv.ultimatesystems.controller;

import com.andriiv.ultimatesystems.entity.Student;
import com.andriiv.ultimatesystems.entity.Teacher;
import com.andriiv.ultimatesystems.exception.ResourceNotFoundException;
import com.andriiv.ultimatesystems.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Roman_Andriiv
 */
@RestController
@RequestMapping("/teachers")
public class TeacherController {

    private final TeacherRepository teacherRepository;

    @Autowired
    public TeacherController(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }


    /**
     * Gets all teachers.
     *
     * @return the all teachers
     */
    @GetMapping("/all")
    public ResponseEntity<?> getAll() {

        List<Teacher> teachers = teacherRepository.findAll();

        if (teachers.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(teachers, HttpStatus.OK);
    }

    /**
     * Gets one teacher.
     *
     * @param id the id
     * @return the one teacher
     * @throws ResourceNotFoundException the resource not found exception
     */
    @GetMapping("/{id}")
    public ResponseEntity<Teacher> getOne(@PathVariable(value = "id") Long id) throws ResourceNotFoundException {

        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Teacher not found for id :: " + id));

        return new ResponseEntity<>(teacher, HttpStatus.OK);
    }

    /**
     * Save teacher.
     *
     * @param teacher the teacher
     * @return the response entity
     */
    @PostMapping("/save")
    public ResponseEntity<?> save(@Valid @RequestBody Teacher teacher){

        teacherRepository.save(teacher);
        return new ResponseEntity<>("Teacher was saved successfully",HttpStatus.OK);
    }

    /**
     * Update teacher.
     *
     * @param id             the id
     * @param teacherDetails the teacher details
     * @return the response entity
     * @throws ResourceNotFoundException the resource not found exception
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable(value = "id") Long id,
                                           @Valid @RequestBody Teacher teacherDetails) throws ResourceNotFoundException {

        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found for this ID :: " + id));

        teacher.setFirstName(teacherDetails.getFirstName());
        teacher.setLastName(teacherDetails.getLastName());
        teacher.setAge(teacherDetails.getAge());
        teacher.setEmail(teacherDetails.getEmail());
        teacher.setSubject(teacherDetails.getSubject());
        teacher.setStudents(teacherDetails.getStudents());

        teacherRepository.save(teacher);

        return new ResponseEntity<>("Teacher was updated successfully", HttpStatus.OK);
    }

    /**
     * Delete teacher.
     *
     * @param id the id
     * @return the response entity
     * @throws ResourceNotFoundException the resource not found exception
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable(value = "id") Long id) throws ResourceNotFoundException {

        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found for this ID :: " + id));

        teacherRepository.delete(teacher);
        return new ResponseEntity<>("Teacher was deleted successfully", HttpStatus.OK);

    }
}
