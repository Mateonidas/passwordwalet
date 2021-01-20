package com.passwordwallet.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "password_backup")
public class PasswordBackupEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Basic
    @Column(name = "password")
    private String password;

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
    @JoinColumn(name = "password_id", referencedColumnName = "id")
    private PasswordEntity passwordEntity;

}
