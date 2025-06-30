package com.salas.admin.clients;

import com.salas.admin.dto.ConfigurationTagsDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

import static com.salas.admin.clients.ConfigurationTagsFeignClient.BASE_PATH;
import static com.salas.admin.clients.ConfigurationTagsFeignClient.FEIGN_CLIENT_NAME;


@FeignClient(
        name = "configuration-service",
        contextId = FEIGN_CLIENT_NAME,
        path = BASE_PATH
)
public interface ConfigurationTagsFeignClient {
    String FEIGN_CLIENT_NAME = "configuration-tags-feign-client";
    String BASE_PATH = "/v1/configuration-tags";

    @GetMapping
    List<ConfigurationTagsDto> getAll();

    @GetMapping("/{id}")
    ConfigurationTagsDto getById(@PathVariable Integer id);
}
