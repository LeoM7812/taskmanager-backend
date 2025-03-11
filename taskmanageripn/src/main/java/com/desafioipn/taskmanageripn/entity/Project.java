package com.desafioipn.taskmanageripn.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
@OneToMany(mappedBy = "project" , cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasks;

public void addTask(Task task) {
    tasks.add(task);
    task.setProject(this);
}

public void removeTask(Task task) {
    tasks.remove(task);
    task.setProject(null);
}
}
