package com.passwordwallet.services;

import com.passwordwallet.entities.PasswordEntity;
import com.passwordwallet.repositories.PasswordRepository;
import com.passwordwallet.security.EncryptionService;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.security.Key;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
@Sql("password.sql")
class PasswordServiceTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @InjectMocks
    private PasswordServiceImpl passwordService;

    @Autowired
    private PasswordRepository passwordRepository;

    @Test
    void shouldReturnTrueIfPasswordWasFoundById() {
        passwordService = new PasswordServiceImpl(passwordRepository);
        Key key = EncryptionService.generateKey("testKey");
        PasswordEntity password = new PasswordEntity();
        password.setLogin("Login");
        password.setDescription("Description");
        password.setWebAddress("address.pl");
        password.setIdUser(1);
        password.setPassword(EncryptionService.encryptAES("Password", key));
        testEntityManager.persist(password);
        int id = (int) testEntityManager.getId(password);

        PasswordEntity find = passwordService.findById(id);

        MatcherAssert.assertThat(password, equalTo(find));
    }

    @Test
    void shouldReturnFalseIfNoPasswordWasFoundById() {
        passwordService = new PasswordServiceImpl(passwordRepository);

        Assertions.assertThrows(RuntimeException.class, () -> {
            passwordService.findById(1);
        });
    }

    @Test
    void shouldReturnTrueIfPasswordWasSuccessfullySaved() {
        passwordService = new PasswordServiceImpl(passwordRepository);
        int listSize = passwordRepository.findAll().size();
        Key key = EncryptionService.generateKey("testKey");
        PasswordEntity password = new PasswordEntity();
        password.setLogin("Login");
        password.setDescription("Description");
        password.setWebAddress("address.pl");
        password.setIdUser(1);
        password.setPassword(EncryptionService.encryptAES("Password", key));

        passwordService.save(password);

        int listSizeAfterSave = passwordRepository.findAll().size();
        MatcherAssert.assertThat(listSize + 1, equalTo(listSizeAfterSave));
    }

    @Test
    void shouldReturnTrueIfPasswordWasDeleted() {
        passwordService = new PasswordServiceImpl(passwordRepository);
        Key key = EncryptionService.generateKey("testKey");
        PasswordEntity password = new PasswordEntity();
        password.setLogin("Login");
        password.setDescription("Description");
        password.setWebAddress("address.pl");
        password.setIdUser(1);
        password.setPassword(EncryptionService.encryptAES("Password", key));
        testEntityManager.persist(password);
        int id = (int) testEntityManager.getId(password);

        passwordService.deleteById(id);

        int listSize = passwordRepository.findAll().size();
        MatcherAssert.assertThat(0, equalTo(listSize));
    }

    @Test
    void shouldReturnTrueIfAllPasswordsWasSaved() {
        passwordService = new PasswordServiceImpl(passwordRepository);
        Key key = EncryptionService.generateKey("testKey");
        PasswordEntity password1 = new PasswordEntity();
        password1.setLogin("Login1");
        password1.setDescription("Description1");
        password1.setWebAddress("address.pl");
        password1.setIdUser(1);
        password1.setPassword(EncryptionService.encryptAES("Password", key));
        PasswordEntity password2 = new PasswordEntity();
        password2.setLogin("Login2");
        password2.setDescription("Description");
        password2.setWebAddress("address.pl");
        password2.setIdUser(2);
        password2.setPassword(EncryptionService.encryptAES("Password", key));
        List<PasswordEntity> list = new ArrayList<>();
        list.add(password1);
        list.add(password2);

        passwordService.saveAll(list);

        int listSize = passwordRepository.findAll().size();
        MatcherAssert.assertThat(list.size(), equalTo(listSize));
    }
}