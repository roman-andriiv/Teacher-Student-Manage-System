package com.andriiv.ultimatesystems.controller;

import com.andriiv.ultimatesystems.entity.Student;
import com.andriiv.ultimatesystems.entity.Teacher;
import com.andriiv.ultimatesystems.exception.ResourceNotFoundException;
import com.andriiv.ultimatesystems.repository.StudentRepository;
import com.andriiv.ultimatesystems.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    private final StudentRepository studentRepository;

    @Autowired
    public TeacherController(TeacherRepository teacherRepository, StudentRepository studentRepository) {
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
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
     * Gets all teachers page with specific size.
     *
     * @param pageNumber the page number
     * @param pageSize   the page size
     * @return the all teachers page with size
     */
    @GetMapping("/all/{pageNumber}/{pageSize}")
    public Page<Teacher> getAllTeachersPageWithSize(@PathVariable(required = false) Integer pageNumber,
                                                    @PathVariable(required = false) Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        return teacherRepository.findAll(pageable);
    }

    /**
     * Gets all teacher page with size and sorting.
     *
     * @param pageNumber   the page number
     * @param pageSize     the page size
     * @param sortProperty the sort property
     * @return the all teacher page with size and sorting
     */
    @GetMapping("/all/{pageNumber}/{pageSize}/{sortProperty}")
    public Page<Teacher> getAllTeacherPageWithSizeAndSorting(@PathVariable Integer pageNumber,
                                                             @PathVariable Integer pageSize,
                                                             @PathVariable String sortProperty) {
        Pageable pageable = null;
        if (sortProperty!=null) {
            pageable = PageRequest.of(pageNumber, pageSize, Sort.Direction.ASC, sortProperty);
        }else {
            pageable = PageRequest.of(pageNumber, pageSize, Sort.Direction.ASC, "id");
        }
        return teacherRepository.findAll(pageable);
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

    /**
     * Add a student to the list of students of a specific teacher .
     *
     * @param teacherId the teacher id
     * @param studentId the student id
     * @return the response entity
     * @throws ResourceNotFoundException the resource not found exception
     */
    @PutMapping("/{teacher_id}/addStudent/{student_id}")
    public ResponseEntity<?> addStudent(@PathVariable(value = "teacher_id") Long teacherId,
                                        @PathVariable(value = "student_id") Long studentId) throws ResourceNotFoundException {

        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found for this ID :: " + studentId));

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found for this ID :: " + studentId));


        teacher.getStudents().add(student);
        teacherRepository.save(teacher);

        student.getTeachers().add(teacher);
        studentRepository.save(student);

        return new ResponseEntity<>("The student has been added to the list of students", HttpStatus.OK);
    }

    /**
     * Remove student from the student list of  the specific teacher.
     *
     * @param teacherId the teacher id
     * @param studentId the student id
     * @return the response entity
     * @throws ResourceNotFoundException the resource not found exception
     */
    @PutMapping("/{teacher_id}/removeStudent/{student_id}")
    public ResponseEntity<?> removeStudent(@PathVariable(value = "teacher_id") Long teacherId,
                                           @PathVariable(value = "student_id") Long studentId) throws ResourceNotFoundException {

        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found for this ID :: " + teacherId));

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found for this ID :: " + studentId));

        //todo: check whats wrong
        if (teacher.getStudents().contains(student)){

            teacher.getStudents().remove(student);
            teacherRepository.save(teacher);

            return new ResponseEntity<>("The student has been removed from the student list", HttpStatus.OK);
        }else
            return new ResponseEntity<>("Something wrong", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Filter teachers by first name.
     *
     * @param firstName the first name
     * @return the response entity
     */
    @GetMapping("/filterByFirstName/{firstName}")
    public ResponseEntity<?> filterTeachersByFirstName(@PathVariable(value = "firstName") String firstName) {

        List<Teacher> teachers = teacherRepository.findByFirstName(firstName);

        if (teachers.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(teachers, HttpStatus.OK);
    }
    @GetMapping("/filterByLastName/{lastName}")
    public ResponseEntity<?> filterTeachersByLastName(@PathVariable(value = "lastName") String lastName) {

        List<Teacher> teachers = teacherRepository.findByLastName(lastName);

        if (teachers.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(teachers, HttpStatus.OK);
    }
}
