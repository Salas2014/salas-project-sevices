package com.salas.admin.clients;

import com.salas.admin.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.salas.admin.clients.UserFeignClient.BASE_PATH;
import static com.salas.admin.clients.UserFeignClient.FEIGN_CLIENT_NAME;

@FeignClient(
        name = "configuration-service",
        path = BASE_PATH,
        contextId = FEIGN_CLIENT_NAME
)
public interface UserFeignClient {
    String FEIGN_CLIENT_NAME = "users-feign-client";
    String BASE_PATH = "/v1/users";

    @GetMapping
    List<UserDto> findAll();

    @PostMapping
    UserDto create(@RequestBody UserDto userDto);

    @GetMapping("/{id}")
    UserDto findById(@PathVariable Long id);

    @DeleteMapping("/{id}")
    void delete(@PathVariable Long id);
}
