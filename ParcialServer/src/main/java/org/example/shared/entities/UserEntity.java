package org.example.shared.entities;

import java.io.Serializable;

public class UserEntity implements Serializable {

    private String id;
    private String password;


    public UserEntity(String id, String password) {
        this.id = id;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
