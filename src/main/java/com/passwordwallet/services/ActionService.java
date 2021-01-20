package com.passwordwallet.services;

import com.passwordwallet.entities.ActionEntity;
import com.passwordwallet.entities.UserEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ActionService {
    ActionEntity save(ActionEntity actionEntity);

    List<ActionEntity> findAllByUser(UserEntity user);
}
