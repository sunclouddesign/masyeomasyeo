package org.launchcode.masyeomasyeo.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue
    private int id;

    @NotNull
    @Size(min=5, max=15)
    private String username;

    @Email(message = "Not a valid email")
    private String email;

    @NotNull
    @Size(min=6, message = "Password must be at least 6 characters")
    private String password;

    public User(int id, String username, String email, String password) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public User() {

    }

}
