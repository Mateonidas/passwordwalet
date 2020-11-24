package com.passwordwallet.security;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;

import static org.hamcrest.Matchers.equalTo;

@SpringBootTest
class UserBlockingServiceTest {

    @InjectMocks
    private UserBlockingService userBlockingService;

    @Test
    public void shouldReturnTrueIfUserIsBlocked(){
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        Boolean result = userBlockingService.checkIfBlockedByAttempts(timestamp, 4);
        MatcherAssert.assertThat(true, equalTo(result));
    }

    @Test
    public void shouldReturnTrueIfUserIsBlockedByIp(){
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        Boolean result = userBlockingService.checkIfBlockedByIP(timestamp, 4);
        MatcherAssert.assertThat(true, equalTo(result));
    }

    @Test
    public void shouldReturnFalseIfUserIsNotBlocked(){
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        Boolean result = userBlockingService.checkIfBlockedByAttempts(timestamp, 1);
        MatcherAssert.assertThat(false, equalTo(result));
    }

    @Test
    public void shouldReturnFalseIfUserIsNotBlockedByIp(){
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        Boolean result = userBlockingService.checkIfBlockedByIP(timestamp, 1);
        MatcherAssert.assertThat(false, equalTo(result));
    }
}