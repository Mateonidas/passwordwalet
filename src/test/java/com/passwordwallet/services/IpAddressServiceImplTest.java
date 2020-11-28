package com.passwordwallet.services;

import com.passwordwallet.entities.IpAddressEntity;
import com.passwordwallet.entities.PasswordEntity;
import com.passwordwallet.repositories.IpAddressRepository;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;

@SpringBootTest
class IpAddressServiceImplTest {

    @InjectMocks
    private IpAddressServiceImpl ipAddressService;

    @Mock
    private IpAddressRepository ipAddressRepository;

    @ParameterizedTest
    @MethodSource("provideIpAddress")
    void shouldReturnTrueIfIpAddressWasFoundByIpAddress(IpAddressEntity ipAddressEntity) {
        when(ipAddressRepository.findByIpAddress(ipAddressEntity.getIpAddress()))
                .thenReturn(java.util.Optional.of(ipAddressEntity));

        IpAddressEntity find = ipAddressService.findByIpAddress(ipAddressEntity.getIpAddress()).get();
        MatcherAssert.assertThat(ipAddressEntity, equalTo(find));
    }

    @Test
    void shouldReturnFalseIfNoIpAddressWasFoundByIpAddress() {
        Assertions.assertThrows(RuntimeException.class, () -> {
            ipAddressService.findByIpAddress("0:0:0:0:0:0:0:1");
        });
    }

    @ParameterizedTest
    @MethodSource("provideIpAddress")
    void shouldReturnTrueIfIpAddressWasFoundById(IpAddressEntity ipAddressEntity) {
        when(ipAddressRepository.findById(ipAddressEntity.getId()))
                .thenReturn(java.util.Optional.of(ipAddressEntity));

        IpAddressEntity find = ipAddressService.findById(ipAddressEntity.getId());
        MatcherAssert.assertThat(ipAddressEntity, equalTo(find));
    }

    @Test
    void shouldReturnFalseIfNoIpAddressWasFoundById() {
        Assertions.assertThrows(RuntimeException.class, () -> {
            ipAddressService.findById(1);
        });
    }

    @ParameterizedTest
    @MethodSource("provideIpAddress")
    void shouldReturnTrueIfIpAddressWasSuccessfullySaved(IpAddressEntity ipAddress) {
        ipAddressService.save(ipAddress);

        verify(ipAddressRepository, times(1)).save(ipAddress);
    }

    @ParameterizedTest
    @MethodSource("provideIpAddresses")
    void shouldReturnTrueIfFoundBlockedIpAddresses(List<IpAddressEntity> ipAddressList) {
        when(ipAddressRepository.findBlockedIpAddresses())
                .thenReturn(ipAddressList);

        List<IpAddressEntity> find = ipAddressService.findBlockedIpAddresses();
        MatcherAssert.assertThat(ipAddressList, equalTo(find));
    }


    private static Stream<IpAddressEntity> provideIpAddress() {
        IpAddressEntity ipAddress = new IpAddressEntity();
        ipAddress.setId(1);
        ipAddress.setIpAddress("0:0:0:0:0:0:0:1");
        ipAddress.setIncorrectLoginTrial(4);

        return Stream.of(ipAddress);
    }

    private static Stream<List<IpAddressEntity>> provideIpAddresses() {
        IpAddressEntity unlockedIpAddress = new IpAddressEntity();
        unlockedIpAddress.setId(1);
        unlockedIpAddress.setIpAddress("0:0:0:0:0:0:0:1");
        unlockedIpAddress.setIncorrectLoginTrial(4);

        IpAddressEntity lockedIpAddress = new IpAddressEntity();
        lockedIpAddress.setId(2);
        lockedIpAddress.setIpAddress("0:0:0:0:0:0:0:2");
        lockedIpAddress.setIncorrectLoginTrial(4);

        List<IpAddressEntity> ipAddressList = new ArrayList<>();
        ipAddressList.add(unlockedIpAddress);
        ipAddressList.add(lockedIpAddress);

        return Stream.of(ipAddressList);
    }
}