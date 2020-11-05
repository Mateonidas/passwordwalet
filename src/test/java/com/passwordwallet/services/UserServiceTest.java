package com.passwordwallet.services;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class UserServiceTest {

    private UserService userService;

//    @BeforeAll
//    void setUp(){
//        Authentication authentication = Mockito.mock(Authentication.class);
//        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
//        SecurityContextHolder.setContext(securityContext);
//
//        when(securityContext.getAuthentication()).thenReturn(authentication);
//        when(securityContext.getAuthentication().getCredentials()).thenReturn("password");
//        when(SecurityUtils.getCurrentUserLoginCredential()).thenReturn("password");
//    }

    @Test
    void findByLogin() {
    }

    @Test
    void save() {
    }
}