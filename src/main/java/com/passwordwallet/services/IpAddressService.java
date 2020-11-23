package com.passwordwallet.services;

import com.passwordwallet.entities.IpAddressEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IpAddressService {
    IpAddressEntity findByIpAddress(String ipAddress);

    IpAddressEntity save(IpAddressEntity ipAddressEntity);

    List<IpAddressEntity> findBlockedIpAddresses();

    List<IpAddressEntity> saveAll(List<IpAddressEntity> ipAddressEntities);
}
