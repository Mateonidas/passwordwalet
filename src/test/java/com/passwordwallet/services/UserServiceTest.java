package com.passwordwallet.services;

import com.passwordwallet.entities.UserEntity;
import com.passwordwallet.repositories.UserRepository;
import com.passwordwallet.security.EncryptionService;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.Stream;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;


@SpringBootTest
class UserServiceTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @ParameterizedTest
    @MethodSource("provideUser")
    void shouldReturnTrueIfUserWasSuccessfullySaved(UserEntity user){
        userService.save(user);

        verify(userRepository, times(1)).save(user);
    }

    @ParameterizedTest
    @MethodSource("provideUser")
    void shouldReturnTrueIfUserWasFoundByLogin(UserEntity user) {

        when(userRepository.findByLogin(user.getLogin()))
                .thenReturn(java.util.Optional.of(user));

        UserEntity find = userService.findByLogin(user.getLogin());
        MatcherAssert.assertThat(user, equalTo(find));
    }

    @Test
    void shouldReturnFalseIfNoUserWasFoundByLogin() {
        Assertions.assertThrows(RuntimeException.class, () -> {
            userService.findByLogin("Test");
        });
    }

    private static Stream<UserEntity> provideUser(){
        UserEntity user = new UserEntity();
        user.setLogin("Login");
        user.setPasswordHash(EncryptionService.calculateHMAC("Password", "key"));
        user.setSalt("");
        user.setUsedAlgorithm("HMAC");

        return Stream.of(user);
    }
}