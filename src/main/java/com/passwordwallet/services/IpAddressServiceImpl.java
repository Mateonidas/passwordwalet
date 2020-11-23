package com.passwordwallet.services;

import com.passwordwallet.entities.IpAddressEntity;
import com.passwordwallet.repositories.IpAddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IpAddressServiceImpl implements IpAddressService {

    IpAddressRepository ipAddressRepository;

    @Autowired
    public IpAddressServiceImpl(IpAddressRepository ipAddressRepository) {
        this.ipAddressRepository = ipAddressRepository;
    }

    @Override
    public IpAddressEntity findByIpAddress(String ipAddress){
        return ipAddressRepository.findByIpAddress(ipAddress);
    }

    @Override
    public IpAddressEntity save(IpAddressEntity ipAddressEntity) {
        return ipAddressRepository.save(ipAddressEntity);
    }

    @Override
    public List<IpAddressEntity> findBlockedIpAddresses() {
        return ipAddressRepository.findBlockedIpAddresses();
    }

    @Override
    public List<IpAddressEntity> saveAll(List<IpAddressEntity> ipAddressEntities) {
        return ipAddressRepository.saveAll(ipAddressEntities);
    }
}