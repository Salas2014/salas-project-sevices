package com.salas.admin.controller;

import com.salas.admin.clients.ConfigurationTagsFeignClient;
import com.salas.admin.dto.ConfigurationTagsDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/configuration-tags")
public class ConfigurationTagsRestController {

    private final ConfigurationTagsFeignClient configurationTagsFeignClient;

    public ConfigurationTagsRestController(ConfigurationTagsFeignClient configurationTagsFeignClient) {
        this.configurationTagsFeignClient = configurationTagsFeignClient;
    }

    @GetMapping
    public List<ConfigurationTagsDto> getAll() {
        return configurationTagsFeignClient.getAll();
    }
}
