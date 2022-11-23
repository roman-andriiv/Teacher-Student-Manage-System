package com.andriiv.ultimatesystems.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Set;

/**
 * @author Roman_Andriiv
 */
@Entity
@Data
@NoArgsConstructor
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @NotNull
    @NotEmpty(message = "The first name should not be empty")
    @Size(min = 2, message = "The first name should  have at least 2 characters")
    @Column(name = "first_name")
    private String firstName;

    @NotNull
    @NotEmpty(message = "The first name should not be empty")
    @Size(min = 2, message = "The first name should  have at least 2 characters")
    @Column(name = "last_name")
    private String lastName;

    @NotNull
    @NotEmpty(message = "The age should not be empty")
    @Min(value = 18, message = "The age should be 18 or greater")
    @Column(name = "age")
    private int age;

    @Email(message = "Email is not valid", regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$")
    @NotEmpty(message = "Email cannot be empty")
    @Column(name = "email")
    private String email;

    @Column(name = "specialization")
    private String specialization;

    //todo @ManyToMany
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "student_table_teacher",
            joinColumns ={@JoinColumn(name = "id")},
            inverseJoinColumns = {@JoinColumn(name = "id")})
    private Set<Teacher> teachers;

}
