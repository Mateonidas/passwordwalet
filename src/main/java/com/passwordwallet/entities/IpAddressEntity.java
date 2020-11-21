package com.passwordwallet.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "ip_address", schema = "password_wallet")
public class IpAddressEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Basic
    @Column(name = "ip_address")
    private String ipAddress;

    @Basic
    @Column(name = "incorrect_login_trial")
    private Integer incorrectLoginTrial;
}
