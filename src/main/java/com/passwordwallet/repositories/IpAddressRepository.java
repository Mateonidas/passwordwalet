package com.passwordwallet.repositories;

import com.passwordwallet.entities.IpAddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IpAddressRepository extends JpaRepository<IpAddressEntity, Integer> {
    IpAddressEntity findByIpAddress(String ipAddress);

    @Query("SELECT i FROM IpAddressEntity i WHERE i.incorrectLoginTrial >= 4")
    List<IpAddressEntity> findBlockedIpAddresses();
}
