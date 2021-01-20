package com.passwordwallet.services.impl;

import com.passwordwallet.entities.ActionEntity;
import com.passwordwallet.entities.UserEntity;
import com.passwordwallet.repositories.ActionRepository;
import com.passwordwallet.services.ActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActionServiceImpl implements ActionService {

    @Autowired
    ActionRepository actionRepository;

    @Override
    public ActionEntity save(ActionEntity actionEntity) {
        return actionRepository.save(actionEntity);
    }

    @Override
    public List<ActionEntity> findAllByUser(UserEntity user){
        return actionRepository.findByUserOrderByIdDesc(user);
    }

}
