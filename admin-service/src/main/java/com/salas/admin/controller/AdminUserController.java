package com.salas.admin.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/api/users-session")
public class AdminUserController {
    record User(Integer id, String username) {}
    record Session(Integer id, String status, User user) {}
    private RestTemplate restTemplate;

    public AdminUserController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping
    public List<Session> getUsers() {
        User[] users = restTemplate.getForObject("http://configuration-service/v1/users", User[].class);
        return List.of(
                new Session(1, "open", users[0]),
                new Session(2, "open", users[1])
        );
    }

}
