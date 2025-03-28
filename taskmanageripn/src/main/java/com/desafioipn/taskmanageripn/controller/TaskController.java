package com.desafioipn.taskmanageripn.controller;

import com.desafioipn.taskmanageripn.entity.Project;
import com.desafioipn.taskmanageripn.entity.Task;
import com.desafioipn.taskmanageripn.entity.User;
import com.desafioipn.taskmanageripn.service.ProjectService;
import com.desafioipn.taskmanageripn.service.TaskService;
import com.desafioipn.taskmanageripn.service.UserService;
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
    private final ProjectService projectService;
    private final UserService userService;

    public TaskController(TaskService taskService, ProjectService projectService, UserService userService)
    {
        this.taskService = taskService;
        this.projectService = projectService;
        this.userService = userService;
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
 public ResponseEntity<Task> createTask(@RequestBody Task task) {
     if (task.getProject() != null && task.getProject().getId() != null) {
         Optional<Project> projectOptional = projectService.getProjectById(task.getProject().getId());
         if (projectOptional.isPresent()) {
             task.setProject(projectOptional.get());
         } else {
             return ResponseEntity.badRequest().body(null);
         }
     }
     return ResponseEntity.ok(taskService.saveTask(task));
 }
    @PostMapping("/{taskId}/assignUser/{userId}")
    public ResponseEntity<Task> assignUserToTask(@PathVariable Long taskId, @PathVariable Integer userId) {
        Optional<Task> task = taskService.getTaskById(taskId);
        Optional<User> user = userService.getUserById(userId);

        if (task.isPresent() && user.isPresent()) {
            Task existingTask = task.get();
            existingTask.setUser(user.get());
            return ResponseEntity.ok(taskService.saveTask(existingTask));
        } else {
            return ResponseEntity.notFound().build();
        }
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
            updatedTask.setUser(taskDetails.getUser());
            return ResponseEntity.ok(taskService.saveTask(updatedTask));
        }
        else
        {
            return ResponseEntity.notFound().build();
        }
    }
    @PutMapping("/{taskId}/assignProject/{projectId}")
        public ResponseEntity<Task> assignTaskToProject(@PathVariable Long taskId, @PathVariable Long projectId) {
            Optional<Task> taskOptional = taskService.getTaskById(taskId);
            if (taskOptional.isPresent()) {
                Task task = taskOptional.get();
                Optional<Project> projectOptional = projectService.getProjectById(projectId);
                if (projectOptional.isPresent()) {
                    task.setProject(projectOptional.get());
                    return ResponseEntity.ok(taskService.saveTask(task));
                } else {
                    return ResponseEntity.badRequest().body(null);
                }
            } else {
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
