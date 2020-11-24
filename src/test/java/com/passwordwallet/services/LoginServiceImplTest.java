package com.passwordwallet.services;

import com.passwordwallet.entities.LoginEntity;
import com.passwordwallet.repositories.LoginRepository;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;

@SpringBootTest
class LoginServiceImplTest {

    @InjectMocks
    private LoginServiceImpl loginService;

    @Mock
    private LoginRepository loginRepository;

    @ParameterizedTest
    @MethodSource("provideLoginWithSuccess")
    void shouldReturnTrueIfFoundLastTimestampWithSuccess(LoginEntity loginEntity) {
        when(loginRepository.findDistinctFirstByResultOrderByTimeDesc(true))
                .thenReturn(loginEntity);

        LoginEntity find = loginService.findLastTimestampWithSuccess();
        MatcherAssert.assertThat(loginEntity, equalTo(find));
    }

    @ParameterizedTest
    @MethodSource("provideLoginWithFailure")
    void shouldReturnTrueIfFoundLastTimestampWithFailure(LoginEntity loginEntity) {
        when(loginRepository.findDistinctFirstByResultOrderByTimeDesc(false))
                .thenReturn(loginEntity);

        LoginEntity find = loginService.findLastTimestampWithFailure();
        MatcherAssert.assertThat(loginEntity, equalTo(find));
    }

    @ParameterizedTest
    @MethodSource("provideLoginWithSuccess")
    void shouldReturnTrueIfLoginWasSuccessfullySaved(LoginEntity loginEntity) {
        loginService.save(loginEntity);

        verify(loginRepository, times(1)).save(loginEntity);
    }

    private static Stream<LoginEntity> provideLoginWithSuccess() {
        LoginEntity loginEntity = new LoginEntity();
        loginEntity.setId(1);
        loginEntity.setResult(true);
        loginEntity.setTime(new Timestamp(System.currentTimeMillis()));

        return Stream.of(loginEntity);
    }

    private static Stream<LoginEntity> provideLoginWithFailure() {
        LoginEntity loginEntity = new LoginEntity();
        loginEntity.setId(1);
        loginEntity.setResult(false);
        loginEntity.setTime(new Timestamp(System.currentTimeMillis()));

        return Stream.of(loginEntity);
    }
}