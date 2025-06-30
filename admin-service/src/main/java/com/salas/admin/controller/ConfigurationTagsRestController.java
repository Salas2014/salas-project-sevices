package com.salas.admin.controller;

import com.salas.admin.dto.ConfigurationTagsDto;
import com.salas.admin.service.ConfigurationTagsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/configuration-tags")
public class ConfigurationTagsRestController {

    private final ConfigurationTagsService service;

    public ConfigurationTagsRestController(ConfigurationTagsService service) {
        this.service = service;
    }

    @GetMapping
    public List<ConfigurationTagsDto> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ConfigurationTagsDto getById(@PathVariable("id") Integer id) {
        return service.getById(id);
    }
}
