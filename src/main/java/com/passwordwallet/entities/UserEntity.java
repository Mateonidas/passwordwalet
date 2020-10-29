package com.passwordwallet.entities;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "user", schema = "password_wallet")
public class UserEntity {
    private int id;
    private String login;
    private String passwordHash;
    private String salt;
    private String usedAlgorithm;
    private Collection<PasswordEntity> passwordsById;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "login")
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Basic
    @Column(name = "password_hash")
    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    @Basic
    @Column(name = "salt")
    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    @Basic
    @Column(name = "used_algorithm")
    public String getUsedAlgorithm() {
        return usedAlgorithm;
    }

    public void setUsedAlgorithm(String isPasswordKeptAsHash) {
        this.usedAlgorithm = isPasswordKeptAsHash;
    }

    @OneToMany(mappedBy = "userByIdUser")
    public Collection<PasswordEntity> getPasswordsById() {
        return passwordsById;
    }

    public void setPasswordsById(Collection<PasswordEntity> passwordsById) {
        this.passwordsById = passwordsById;
    }
}
