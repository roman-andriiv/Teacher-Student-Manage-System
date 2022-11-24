package com.andriiv.ultimatesystems.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.List;

/**
 * @author Roman_Andriiv
 */
@Entity
@Data
@NoArgsConstructor
@Table(name = "teachers")
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @NotEmpty(message = "The first name should not be empty")
    @Size(min = 2, message = "The first name should  have at least 2 characters")
    @Column(name = "first_name")
    private String firstName;

    @NotNull
    @NotEmpty(message = "The last name should not be empty")
    @Size(min = 2, message = "The last name should  have at least 2 characters")
    @Column(name = "last_name")
    private String lastName;

    @NotNull
    @Min(value = 18, message = "The age should be 18 or greater")
    @Column(name = "age")
    private int age;

    @Email(message = "Email is not valid", regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$")
    @NotEmpty(message = "Email cannot be empty")
    @Column(name = "email")
    private String email;

    @Column(name = "subject")
    private String subject;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "teachers")
    @JsonIgnoreProperties("teachers")
    private List<Student> students;
}