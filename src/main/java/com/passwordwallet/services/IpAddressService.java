package com.passwordwallet.services;

import com.passwordwallet.entities.IpAddressEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface IpAddressService {
    Optional<IpAddressEntity> findByIpAddress(String ipAddress);

    IpAddressEntity findById(Integer id);

    IpAddressEntity save(IpAddressEntity ipAddressEntity);

    List<IpAddressEntity> findBlockedIpAddresses();
}
