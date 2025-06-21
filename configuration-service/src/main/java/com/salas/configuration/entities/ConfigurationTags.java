package com.salas.configuration.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "tags_configuration")
@NoArgsConstructor
@Getter
@Setter
@ToString
@AllArgsConstructor
public class ConfigurationTags {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "platform_type")
    private String platform;
    private String message;
    @ManyToMany(cascade = CascadeType.ALL, fetch =  FetchType.EAGER)
    @JoinTable(
            name = "tags_configuration_mapping",
            joinColumns = @JoinColumn(name = "config_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags;

}
