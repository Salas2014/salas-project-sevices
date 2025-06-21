package com.salas.configuration.repository;

import com.salas.configuration.entities.ConfigurationTags;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfigurationTagsRepo extends JpaRepository<ConfigurationTags, Integer> {
}
