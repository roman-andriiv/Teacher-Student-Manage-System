package com.andriiv.ultimatesystems.controller;

import com.andriiv.ultimatesystems.entity.Student;
import com.andriiv.ultimatesystems.entity.Teacher;
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
    private final TeacherRepository teacherRepository;

    @Autowired
    public StudentController(StudentRepository studentRepository, TeacherRepository teacherRepository) {
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
    }


    /**
     * Gets all students.
     *
     * @return the all students
     */
    @GetMapping("/all")
    public ResponseEntity<?> getAllStudents() {

        List<Student> students = studentRepository.findAll();

        if (students.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(students, HttpStatus.OK);
    }

    /**
     * Gets one student.
     *
     * @param id the id
     * @return the one student
     * @throws ResourceNotFoundException the resource not found exception
     */
    @GetMapping("/{id}")
    public ResponseEntity<Student> getOneStudent(@PathVariable(value = "id") Long id) throws ResourceNotFoundException {

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found for id :: " + id));

        return new ResponseEntity<>(student, HttpStatus.OK);
    }


    /**
     * Save student.
     *
     * @param student the student
     * @return the response entity
     */
    @PostMapping("/save")
    public ResponseEntity<?> saveStudent(@Valid @RequestBody Student student) {

        studentRepository.save(student);
        return new ResponseEntity<>("Student was saved to DB", HttpStatus.OK);
    }


    /**
     * Update student response entity.
     *
     * @param id             the id
     * @param studentDetails the student details
     * @return the response entity
     * @throws ResourceNotFoundException the resource not found exception
     */
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

    /**
     * Add teacher to the list of teachers of a specific student.
     *
     * @param studentId the student id
     * @param teacherId the teacher id
     * @return the response entity
     * @throws ResourceNotFoundException the resource not found exception
     */
    @PutMapping("/{student_id}/addTeacher/{teacher_id}")
    public ResponseEntity<?> addTeacher(@PathVariable(value = "student_id") Long studentId,
                                        @PathVariable(value = "teacher_id") Long teacherId) throws ResourceNotFoundException {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found for this ID :: " + studentId));

        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found for this ID :: " + studentId));

        student.getTeachers().add(teacher);
        studentRepository.save(student);

        teacher.getStudents().add(student);
        teacherRepository.save(teacher);

        return new ResponseEntity<>("The teachers has been added to teacher list", HttpStatus.OK);
    }

    /**
     * Remove teacher from teachers list of specific student.
     *
     * @param studentId the student id
     * @param teacherId the teacher id
     * @return the response entity
     * @throws ResourceNotFoundException the resource not found exception
     */
    @PutMapping("/{student_id}/removeTeacher/{teacher_id}")
    public ResponseEntity<?> removeTeacher(@PathVariable(value = "student_id") Long studentId,
                                           @PathVariable(value = "teacher_id") Long teacherId) throws ResourceNotFoundException {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found for this ID :: " + studentId));

        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found for this ID :: " + teacherId));

        if (student.getTeachers().contains(teacher)){

            student.getTeachers().remove(teacher);
            studentRepository.save(student);

            return new ResponseEntity<>("The teacher has been removed from the teacher list", HttpStatus.OK);

        } else
            return new ResponseEntity<>("Something wrong", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
