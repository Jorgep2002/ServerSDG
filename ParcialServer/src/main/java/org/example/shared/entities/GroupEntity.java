package org.example.shared.entities;

import java.io.Serializable;

public class GroupEntity implements Serializable {
    private int id; // Cambiado a Integer para permitir valores nulos
    private String name;
    private String description;


    // Constructor sin el ID
    public GroupEntity(String name, String description) {
        this.name = name;
        this.description = description;
    }

    // Constructor vacío
    public GroupEntity() {
    }

    public GroupEntity(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Método toString para mostrar los detalles del grupo
    @Override
    public String toString() {
        return "GroupEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
