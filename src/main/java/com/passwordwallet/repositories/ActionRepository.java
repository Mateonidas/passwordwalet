package com.passwordwallet.repositories;

import com.passwordwallet.entities.ActionEntity;
import com.passwordwallet.entities.IpAddressEntity;
import com.passwordwallet.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ActionRepository extends JpaRepository<ActionEntity, Integer> {

    List<ActionEntity> findByUser(UserEntity user);
}
