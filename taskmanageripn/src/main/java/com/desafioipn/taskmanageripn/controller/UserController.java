package com.desafioipn.taskmanageripn.controller;

import com.desafioipn.taskmanageripn.entity.Role;
import com.desafioipn.taskmanageripn.entity.Task;
import com.desafioipn.taskmanageripn.entity.User;
import com.desafioipn.taskmanageripn.service.TaskService;
import com.desafioipn.taskmanageripn.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.desafioipn.taskmanageripn.service.JwtService;

import java.util.Optional;



import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*",allowedHeaders = "*")
public class UserController {
    private final JwtService jwtService;
    private final UserService userService;
    private final TaskService taskService;




    public UserController(UserService userService, JwtService jwtService, TaskService taskService) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.taskService = taskService;
    }


@GetMapping("/role")
public ResponseEntity<Role> getUserRoleFromToken(@RequestHeader("Authorization") String token) {
    String jwtToken = token.substring(7);
    String username = jwtService.decodeTokenAndGetUserId(jwtToken);

    Optional<User> user = userService.getUserByUsername(username);
    if (user.isPresent()) {
        return ResponseEntity.ok(user.get().getRole());
    } else {
        return ResponseEntity.notFound().build();
    }
}
@GetMapping("/{email}")
public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
    Optional<User> user = userService.getUserByUsername(email);
    System.out.println("Received email: " + email);
    if (user.isPresent()) {
        return ResponseEntity.ok(user.get());
    } else {
        return ResponseEntity.notFound().build();
    }
}

}
