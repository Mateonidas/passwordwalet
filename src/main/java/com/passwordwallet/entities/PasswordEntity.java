package com.passwordwallet.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

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
}
