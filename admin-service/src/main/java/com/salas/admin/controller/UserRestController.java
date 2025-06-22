package com.salas.admin.controller;

import com.salas.admin.clients.UserFeignClient;
import com.salas.admin.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserRestController {
    private final UserFeignClient client;

    @Autowired
    public UserRestController(UserFeignClient client) {
        this.client = client;
    }

    @GetMapping
    public List<UserDto> getUsers() {
        return client.findAll();
    }

    @PostMapping
    public UserDto createUser(@RequestBody UserDto userDto) {
        return client.create(userDto);
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable Long id) {
        return client.findById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable Long id) {
        client.delete(id);
    }

}
