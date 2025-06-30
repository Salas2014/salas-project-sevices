package com.salas.admin.service;

import com.salas.admin.clients.ConfigurationTagsFeignClient;
import com.salas.admin.dto.ConfigurationTagsDto;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service
public class ConfigurationTagsService {

    private final ConfigurationTagsFeignClient configurationTagsFeignClient;
    private final MeterRegistry meterRegistry;

    public ConfigurationTagsService(ConfigurationTagsFeignClient configurationTagsFeignClient,
                                    MeterRegistry meterRegistry) {
        this.configurationTagsFeignClient = configurationTagsFeignClient;
        this.meterRegistry = meterRegistry;
    }

    public List<ConfigurationTagsDto> getAll() {
        return configurationTagsFeignClient.getAll();
    }

    public ConfigurationTagsDto getById(@PathVariable("id") Integer id) {
        return configurationTagsFeignClient.getById(id);
    }
}
