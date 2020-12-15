package com.passwordwallet.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "password", schema = "password_wallet")
public class PasswordEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Basic
    @Column(name = "password")
    private String password;

    @Basic
    @Column(name = "id_user", insertable = false, updatable = false)
    private Integer idUser;

    @Basic
    @Column(name = "web_address")
    private String webAddress;

    @Basic
    @Column(name = "description")
    private String description;

    @Basic
    @Column(name = "login")
    private String login;

    @ManyToOne
    @JoinColumn(name = "id_user", referencedColumnName = "id", table = "password")
    private UserEntity userByIdUser;

    @ManyToMany(mappedBy = "sharedPasswords", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<UserEntity> usersList;

    public PasswordEntity() {
    }

    public PasswordEntity(PasswordEntity passwordEntity) {
        this.id = passwordEntity.getId();
        this.password = passwordEntity.getPassword();
        this.idUser = passwordEntity.getIdUser();
        this.webAddress = passwordEntity.getWebAddress();
        this.description = passwordEntity.getDescription();
        this.login = passwordEntity.getLogin();
        this.userByIdUser = passwordEntity.getUserByIdUser();
        this.usersList = passwordEntity.getUsersList();
    }
}
