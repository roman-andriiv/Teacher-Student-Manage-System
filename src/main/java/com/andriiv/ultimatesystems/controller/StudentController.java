package com.andriiv.ultimatesystems.controller;

import com.andriiv.ultimatesystems.entity.Student;
import com.andriiv.ultimatesystems.exception.ResourceNotFoundException;
import com.andriiv.ultimatesystems.repository.StudentRepository;
import com.andriiv.ultimatesystems.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * The type Student controller.
 *
 * @author Roman_Andriiv
 */
@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }


    /**
     * Gets all students.
     *
     * @return the all students
     */
    @GetMapping("/all")
    public ResponseEntity<?> getAllStudents() {

        List<Student> students = studentRepository.findAll();

        if (students.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(students, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getOneStudent(@PathVariable(value = "id") Long id) throws ResourceNotFoundException {

        Student student = studentRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Student not found for id :: " + id));

        return new ResponseEntity<>(student, HttpStatus.OK);
    }


    @PostMapping("/save")
    public ResponseEntity<?> saveStudent(@Valid @RequestBody Student student){

        studentRepository.save(student);
        return new ResponseEntity<>("Student was saved to DB",HttpStatus.OK);

    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateStudent(@PathVariable(value = "id") Long id,
                                                 @Valid @RequestBody Student studentDetails) throws ResourceNotFoundException {

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found for this ID :: " + id));

        student.setFirstName(studentDetails.getFirstName());
        student.setLastName(studentDetails.getLastName());
        student.setAge(studentDetails.getAge());
        student.setEmail(studentDetails.getEmail());
        student.setSpecialization(studentDetails.getSpecialization());
        student.setTeachers(studentDetails.getTeachers());

        studentRepository.save(student);

        return new ResponseEntity<>("Student was updated", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable(value = "id") Long id) throws ResourceNotFoundException {

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found for this ID :: " + id));

        studentRepository.delete(student);
        return new ResponseEntity<>("Student was deleted from DB", HttpStatus.OK);

    }

}
