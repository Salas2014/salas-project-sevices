package com.salas.configuration.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "tags")
@NoArgsConstructor
@Getter
@Setter
@ToString
@AllArgsConstructor
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "tag_name")
    private String name;
    private Integer priority;
    @ManyToMany(mappedBy = "tags")
    private List<ConfigurationTags> configurations;

    public Tag(String name, Integer priority) {
    }
}
