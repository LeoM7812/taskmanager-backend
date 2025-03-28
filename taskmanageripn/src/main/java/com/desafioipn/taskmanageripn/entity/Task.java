package com.desafioipn.taskmanageripn.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Boolean completed;
    private LocalDate dueDate;

@ManyToOne
@JoinColumn(name = "user_id")
private User user;

    @ManyToOne()
    @JoinColumn(name = "project_id")
    @JsonIgnoreProperties({"tasks","user"})
    private Project project;
}
