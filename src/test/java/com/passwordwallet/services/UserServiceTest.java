package com.passwordwallet.services;

import com.passwordwallet.entities.UserEntity;
import com.passwordwallet.repositories.UserRepository;
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
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
@Sql("users.sql")
class UserServiceTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @InjectMocks
    private UserServiceImpl userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldReturnTrueIfUserWasSuccessfullySaved(){
        userService = new UserServiceImpl(userRepository);
        int listSize = userRepository.findAll().size();

        UserEntity user = new UserEntity();
        user.setLogin("Login");
        user.setPasswordHash(EncryptionService.calculateHMAC("Password", "key"));
        user.setSalt("");
        user.setUsedAlgorithm("HMAC");
        userService.save(user);

        int listSizeAfterSave = userRepository.findAll().size();
        MatcherAssert.assertThat(listSize+1, equalTo(listSizeAfterSave));
    }

    @Test
    void shouldReturnTrueIfUserWasFoundByLogin() {
        userService = new UserServiceImpl(userRepository);
        UserEntity user = new UserEntity();
        user.setLogin("Login");
        user.setPasswordHash(EncryptionService.calculateHMAC("Password", "key"));
        user.setSalt("");
        user.setUsedAlgorithm("HMAC");

        testEntityManager.persist(user);

        UserEntity find = userService.findByLogin("Login");
        MatcherAssert.assertThat(user, equalTo(find));
    }

    @Test
    void shouldReturnFalseIfNoUserWasFoundByLogin() {
        userService = new UserServiceImpl(userRepository);

        Assertions.assertThrows(RuntimeException.class, () -> {userService.findByLogin("Test");});
    }
}