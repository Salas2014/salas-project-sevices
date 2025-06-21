package com.salas.configuration.controller;

import com.salas.configuration.dto.ConfigurationTagsDto;
import com.salas.configuration.entities.ConfigurationTags;
import com.salas.configuration.service.ConfigurationTagsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/configuration-tags")
public class ConfigurationTagsRestController {

    private final ConfigurationTagsService configurationTagsService;

    public ConfigurationTagsRestController(ConfigurationTagsService configurationTagsService) {
        this.configurationTagsService = configurationTagsService;
    }

    @GetMapping
    public List<ConfigurationTagsDto> findAll() {
        return configurationTagsService.findAll();
    }

    @GetMapping("/{id}")
    public ConfigurationTagsDto findById(@PathVariable Integer id) {
        return configurationTagsService.findById(id);
    }

    @PostMapping
    public ConfigurationTags save(@RequestBody ConfigurationTagsDto configurationTags) {
        return configurationTagsService.save(configurationTags);
    }

    @DeleteMapping
    public void deleteById(@RequestParam Integer id) {
        configurationTagsService.deleteById(id);
    }
}
