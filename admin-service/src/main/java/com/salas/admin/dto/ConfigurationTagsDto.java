package com.salas.admin.dto;


import java.util.List;

public record ConfigurationTagsDto(Integer id, String platform, String message, List<TagDto> tags) {
}
