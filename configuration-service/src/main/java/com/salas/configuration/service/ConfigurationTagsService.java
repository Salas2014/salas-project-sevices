package com.salas.configuration.service;

import com.salas.configuration.dto.ConfigurationTagsDto;
import com.salas.configuration.dto.TagDto;
import com.salas.configuration.entities.ConfigurationTags;
import com.salas.configuration.entities.Tag;
import com.salas.configuration.repository.ConfigurationTagsRepo;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ConfigurationTagsService {

    private final ConfigurationTagsRepo configurationTagsRepo;
    private static final String CASH_NAME = "configTags";

    public ConfigurationTagsService(ConfigurationTagsRepo configurationTagsRepo) {
        this.configurationTagsRepo = configurationTagsRepo;
    }

    @Cacheable( cacheNames = "configTagsAll" )
    public List<ConfigurationTagsDto> findAll() {
        return configurationTagsRepo.findAll()
                .stream()
                .map(ConfigurationTagsService::convert)
                .toList();
    }

    @Cacheable(cacheNames = CASH_NAME, key = "#id")
    public ConfigurationTagsDto findById(Integer id) {
        return convert(configurationTagsRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("No configuration tags found with id: " + id)));
    }

    @Transactional
    public ConfigurationTags save(ConfigurationTagsDto dto) {
        return configurationTagsRepo.save(convert(dto));
    }

    @Transactional
    @CacheEvict(cacheNames = CASH_NAME, key = "#id")
    public void deleteById(Integer id) {
        configurationTagsRepo.deleteById(id);
    }

    private static ConfigurationTagsDto convert(ConfigurationTags config) {
        var tags = config.getTags().stream()
                .map(tag -> new TagDto(tag.getId(), tag.getName(), tag.getPriority()))
                .toList();
        return new ConfigurationTagsDto(config.getId(), config.getPlatform(), config.getMessage(), tags);
    }

    private static ConfigurationTags convert(ConfigurationTagsDto dto) {
        var tags = dto.tags().stream()
                .map(tagDto -> new Tag(tagDto.name(), tagDto.priority()))
                .toList();

        ConfigurationTags config = new ConfigurationTags();
        config.setPlatform(dto.platform());
        config.setMessage(dto.message());
        config.setTags(tags);

        return config;
    }

}
