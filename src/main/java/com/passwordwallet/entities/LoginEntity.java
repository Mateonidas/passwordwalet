package com.passwordwallet.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "login", schema = "password_wallet")
public class LoginEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Basic
    @Column(name = "time")
    private Timestamp time;

    @Basic
    @Column(name = "result")
    private Boolean result;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity userByUserId;

    @ManyToOne
    @JoinColumn(name = "ip_address_id", referencedColumnName = "id")
    private IpAddressEntity ipAddressByIpAddressId;
}
