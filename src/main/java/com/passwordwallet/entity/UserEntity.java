package com.passwordwallet.entity;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "user", schema = "password_wallet")
public class UserEntity {
    private int id;
    private String login;
    private String passwordHash;
    private String salt;
    private boolean isPasswordKeptAsHash;
    private Collection<PasswordEntity> passwordsById;

    @Id
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
    @Column(name = "isPasswordKeptAsHash")
    public boolean getIsPasswordKeptAsHash() {
        return isPasswordKeptAsHash;
    }

    public void setIsPasswordKeptAsHash(boolean isPasswordKeptAsHash) {
        this.isPasswordKeptAsHash = isPasswordKeptAsHash;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return id == that.id &&
                isPasswordKeptAsHash == that.isPasswordKeptAsHash &&
                Objects.equals(login, that.login) &&
                Objects.equals(passwordHash, that.passwordHash) &&
                Objects.equals(salt, that.salt) &&
                Objects.equals(passwordsById, that.passwordsById);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login, passwordHash, salt, isPasswordKeptAsHash, passwordsById);
    }

    @OneToMany(mappedBy = "userByIdUser")
    public Collection<PasswordEntity> getPasswordsById() {
        return passwordsById;
    }

    public void setPasswordsById(Collection<PasswordEntity> passwordsById) {
        this.passwordsById = passwordsById;
    }
}
