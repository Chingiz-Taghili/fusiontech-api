package com.fusiontech.api.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String name;

    //Yeni rol yaradanda @PrePersist, mövcud rolu dəyişəndə @PreUpdate avtomatik bu metodu çağırır.
    //Ona görə də hər zaman name avtomatik upperCase olur
    @PrePersist
    @PreUpdate
    private void formatName() {
        this.name = this.name.toUpperCase();
    }
}
