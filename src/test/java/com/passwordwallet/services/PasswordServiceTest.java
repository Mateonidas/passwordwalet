package com.passwordwallet.services;

import com.passwordwallet.entities.PasswordEntity;
import com.passwordwallet.repositories.PasswordRepository;
import com.passwordwallet.security.EncryptionService;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.Key;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;

@SpringBootTest
class PasswordServiceTest {

    @InjectMocks
    private PasswordServiceImpl passwordService;

    @Mock
    private PasswordRepository passwordRepository;

    @ParameterizedTest
    @MethodSource("providePassword")
    void shouldReturnTrueIfPasswordWasFoundById(PasswordEntity password) {
        when(passwordRepository.findById(password.getId()))
                .thenReturn(java.util.Optional.of(password));

        PasswordEntity find = passwordService.findById(password.getId());
        MatcherAssert.assertThat(password, equalTo(find));
    }

    @Test
    void shouldReturnFalseIfNoPasswordWasFoundById() {
        Assertions.assertThrows(RuntimeException.class, () -> {
            passwordService.findById(1);
        });
    }

    @ParameterizedTest
    @MethodSource("providePassword")
    void shouldReturnTrueIfPasswordWasSuccessfullySaved(PasswordEntity password) {
        passwordService.save(password);

        verify(passwordRepository, times(1)).save(password);
    }

    @ParameterizedTest
    @MethodSource("providePassword")
    void shouldReturnTrueIfPasswordWasDeleted(PasswordEntity password) {
        passwordService.deleteById(password.getId());

        verify(passwordRepository, times(1)).deleteById(password.getId());
    }

    @ParameterizedTest
    @MethodSource("providePasswords")
    void shouldReturnTrueIfAllPasswordsWasSaved(List<PasswordEntity> passwordsList) {
        passwordService.saveAll(passwordsList);

        verify(passwordRepository, times(1)).saveAll(passwordsList);
    }

    private static Stream<PasswordEntity> providePassword() {
        Key key = EncryptionService.generateKey("testKey");
        PasswordEntity password = new PasswordEntity();
        password.setId(1);
        password.setLogin("Login");
        password.setDescription("Description");
        password.setWebAddress("address.pl");
        password.setIdUser(1);
        password.setPassword(EncryptionService.encryptAES("Password", key));

        return Stream.of(password);
    }

    private static Stream<List<PasswordEntity>> providePasswords() {
        Key key = EncryptionService.generateKey("testKey");
        PasswordEntity password = new PasswordEntity();
        password.setId(1);
        password.setLogin("Login");
        password.setDescription("Description");
        password.setWebAddress("address.pl");
        password.setIdUser(1);
        password.setPassword(EncryptionService.encryptAES("Password", key));

        PasswordEntity password2 = new PasswordEntity();
        password2.setLogin("Login2");
        password2.setDescription("Description");
        password2.setWebAddress("address.pl");
        password2.setIdUser(2);
        password2.setPassword(EncryptionService.encryptAES("Password", key));

        List<PasswordEntity> passwordsList = new ArrayList<>();
        passwordsList.add(password);
        passwordsList.add(password2);

        return Stream.of(passwordsList);
    }
}