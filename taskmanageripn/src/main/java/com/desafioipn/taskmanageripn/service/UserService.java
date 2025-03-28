package com.desafioipn.taskmanageripn.service;

import com.desafioipn.taskmanageripn.entity.User;
import com.desafioipn.taskmanageripn.repository.ProjectRepository;
import com.desafioipn.taskmanageripn.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class UserService {
    private final UserRepository userRepository;

        @Autowired
        public UserService(UserRepository userRepository) {
            this.userRepository = userRepository;
        }

        public Optional<User> getUserByUsername(String username) {
            return userRepository.findByEmail(username);
        }
        public Optional<User> getUserById(Integer id) {
                return userRepository.findById(id);
            }
}
