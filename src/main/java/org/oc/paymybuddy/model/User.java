package org.oc.paymybuddy.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
public class User {
    @Getter
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "userID")
    private Integer userID;
    @Getter
    @Setter
    @Column(name = "first_name")
    private String firstName;
    @Getter
    @Setter
    @Column(name = "email")
    private String email;
    @Getter
    @Setter
    @Column(name = "password")
    private String password;

    public User() {
    }

    public User(String firstName, String email, String password) {
        this.firstName = firstName;
        this.email = email;
        this.password = password;
    }

}
