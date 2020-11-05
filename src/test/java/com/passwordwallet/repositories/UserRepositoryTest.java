package com.passwordwallet.repositories;

import com.passwordwallet.entities.UserEntity;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
@Sql("users.sql")
class UserRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    void injectedComponentsAreNotNull(){
        MatcherAssert.assertThat(testEntityManager, notNullValue());
        MatcherAssert.assertThat(userRepository, notNullValue());
    }

    @Test
    void whenFindByLogin_thenReturnUser() {
        UserEntity user = new UserEntity();
        user.setLogin("Login");
        user.setPasswordHash("Password");
        user.setSalt("");
        user.setUsedAlgorithm("HMAC");

        testEntityManager.persist(user);

        UserEntity found = userRepository.findByLogin("Login").get();
        MatcherAssert.assertThat(found.getLogin(), equalTo("Login"));
    }

}