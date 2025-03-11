package com.desafioipn.taskmanageripn.controller;

import com.desafioipn.taskmanageripn.entity.Task;
import com.desafioipn.taskmanageripn.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "*",allowedHeaders = "*")
public class TaskController
{
    private final TaskService taskService;

    public TaskController(TaskService taskService)
    {
        this.taskService = taskService;
    }

    @GetMapping
    public List<Task> getAllTasks()
    {
        return taskService.getAllTasks();
    }
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id)
    {
        Optional<Task> task = taskService.getTaskById(id);
        if (task.isPresent())
        {
            return ResponseEntity.ok(task.get());
        }
        else
        {
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task)
    {
        return ResponseEntity.ok(taskService.saveTask(task));
    }
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task taskDetails)
    {
        Optional<Task> task = taskService.getTaskById(id);
        if(task.isPresent() )
        {
            Task updatedTask = task.get();
            updatedTask.setName(taskDetails.getName());
            updatedTask.setDescription(taskDetails.getDescription());
            updatedTask.setDueDate(taskDetails.getDueDate());
            updatedTask.setCompleted(taskDetails.getCompleted());
            return ResponseEntity.ok(taskService.saveTask(updatedTask));
        }
        else
        {
            return ResponseEntity.notFound().build();
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id)
    {
        Optional<Task> task = taskService.getTaskById(id);
        if (task.isPresent())
        {
            taskService.deleteTask(id);
            return ResponseEntity.ok().build();
        }
        else
        {
            return ResponseEntity.notFound().build();
        }
    }
}
