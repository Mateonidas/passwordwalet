package com.passwordwallet.services.impl;

import com.passwordwallet.entities.IpAddressEntity;
import com.passwordwallet.repositories.IpAddressRepository;
import com.passwordwallet.services.IpAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IpAddressServiceImpl implements IpAddressService {

    IpAddressRepository ipAddressRepository;

    @Autowired
    public IpAddressServiceImpl(IpAddressRepository ipAddressRepository) {
        this.ipAddressRepository = ipAddressRepository;
    }

    @Override
    public Optional<IpAddressEntity> findByIpAddress(String ipAddress){
        return ipAddressRepository.findByIpAddress(ipAddress);
    }

    @Override
    public IpAddressEntity findById(Integer id) {
        return ipAddressRepository.findById(id).get();
    }

    @Override
    public IpAddressEntity save(IpAddressEntity ipAddressEntity) {
        return ipAddressRepository.save(ipAddressEntity);
    }

    @Override
    public List<IpAddressEntity> findBlockedIpAddresses() {
        return ipAddressRepository.findBlockedIpAddresses();
    }
}
