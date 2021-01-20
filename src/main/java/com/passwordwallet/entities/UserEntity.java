package com.passwordwallet.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "user", schema = "password_wallet")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Basic
    @Column(name = "login")
    private String login;

    @Basic
    @Column(name = "email")
    private String email;

    @Basic
    @Column(name = "password_hash")
    private String passwordHash;

    @Basic
    @Column(name = "salt")
    private String salt;

    @Basic
    @Column(name = "used_algorithm")
    private String usedAlgorithm;

    @OneToMany(mappedBy = "userByIdUser")
    private Collection<PasswordEntity> passwordsById;

    @Basic
    @Column(name = "incorrect_logins")
    private int incorrectLogins;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<PasswordEntity> sharedPasswords;

    @OneToMany(mappedBy = "user")
    private Collection<ActionEntity> actions;
}
