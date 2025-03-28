package com.desafioipn.taskmanageripn.controller;

import com.desafioipn.taskmanageripn.entity.Project;
import com.desafioipn.taskmanageripn.entity.Role;
import com.desafioipn.taskmanageripn.entity.Task;
import com.desafioipn.taskmanageripn.entity.User;
import com.desafioipn.taskmanageripn.service.ProjectService;
import com.desafioipn.taskmanageripn.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*",allowedHeaders = "*")
@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    private final ProjectService projectService;
    private final UserService userService;

    public ProjectController(ProjectService projectService, UserService userService) {
        this.projectService = projectService;
        this.userService = userService;
    }

    @GetMapping
    public List<Project> getAllProjects() {
        return projectService.getAllProjects();
    }
    @GetMapping("/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable Long id) {
        Optional<Project> project = projectService.getProjectById(id);
        if (project.isPresent()) {
            return ResponseEntity.ok(project.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/{id}/tasks")
    public ResponseEntity<List<Task>> getTasksByProjectId(@PathVariable Long id) {
        Optional<Project> project = projectService.getProjectById(id);
        if (project.isPresent()) {
            return ResponseEntity.ok(project.get().getTasks());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/{email}/projects-with-tasks")
    public ResponseEntity<List<Project>> getProjectsWithTasksByUserEmail(@PathVariable String email) {
        Optional<User> user = userService.getUserByUsername(email);
        if (user.isPresent()) {
            List<Project> projects = projectService.getAllProjects();
            projects.forEach(project -> project.setTasks(
            project.getTasks().stream()
                                .filter(task -> task.getUser() != null && task.getUser().getEmail().equals(email))
                                .collect(Collectors.toList())
            ));
            return ResponseEntity.ok(projects);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping
public Project createProject(@RequestBody Project project) {
    Optional<User> userOptional = Optional.empty();
    if (project.getUser() != null) {
        userOptional = userService.getUserById(project.getUser().getId());
    }
    if (userOptional.isPresent() && !userOptional.get().getRole().equals(Role.MANAGER) && !userOptional.get().getRole().equals(Role.ADMIN)) {
        throw new IllegalArgumentException("The user assigned to the project must be a manager or admin.");
    }
    userOptional.ifPresent(project::setUser);
    return projectService.saveProject(project);
}


@PutMapping("/{id}")
public ResponseEntity<Project> updateProject(@PathVariable Long id, @RequestBody Project projectDetails) {
    Optional<Project> project = projectService.getProjectById(id);
    if (project.isPresent()) {
        Project updatedProject = project.get();
        if (projectDetails.getName() != null) {
            updatedProject.setName(projectDetails.getName());
        }
        if (projectDetails.getDescription() != null) {
            updatedProject.setDescription(projectDetails.getDescription());
        }
        if (projectDetails.getBudget() != 0){
            updatedProject.setBudget(projectDetails.getBudget());
        }
        if (projectDetails.getTasks() != null) {
            updatedProject.getTasks().clear();
            for (Task task : projectDetails.getTasks()) {
                updatedProject.addTask(task);
            }
        }

        return ResponseEntity.ok(projectService.saveProject(updatedProject));
    } else {
        return ResponseEntity.notFound().build();
    }
}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        Optional<Project> project = projectService.getProjectById(id);
        if (project.isPresent()) {
            projectService.deleteProject(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
