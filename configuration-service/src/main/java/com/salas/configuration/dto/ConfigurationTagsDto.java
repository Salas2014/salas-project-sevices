package com.salas.configuration.dto;

import java.io.Serializable;
import java.util.List;

public record ConfigurationTagsDto(Integer id, String platform, String message,
                                   List<TagDto> tags) implements Serializable {
}
