package com.salas.configuration.dto;

import java.io.Serializable;

public record TagDto(Long id, String name, Integer priority) implements Serializable {
}
