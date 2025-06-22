package com.salas.admin.clients;

import com.salas.admin.dto.ConfigurationTagsDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(
        name = "configuration-service",
        path = ConfigurationTagsFeignClient.BASE_PATH
)
public interface ConfigurationTagsFeignClient {
    String BASE_PATH = "/v1/configuration-tags";

    @GetMapping
    List<ConfigurationTagsDto> getAll();
}
