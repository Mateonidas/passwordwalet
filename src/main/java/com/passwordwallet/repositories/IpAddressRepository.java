package com.passwordwallet.repositories;

import com.passwordwallet.entities.IpAddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IpAddressRepository extends JpaRepository<IpAddressEntity, Integer> {
}
