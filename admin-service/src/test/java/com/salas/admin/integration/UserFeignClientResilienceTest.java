package com.salas.admin.integration;

import com.salas.admin.clients.UserFeignClient;
import com.salas.admin.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.consul.config.ConsulConfigAutoConfiguration;
import org.springframework.cloud.consul.serviceregistry.ConsulAutoServiceRegistrationAutoConfiguration;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureWireMock(port = 0)
@ImportAutoConfiguration(exclude = {
        ConsulAutoServiceRegistrationAutoConfiguration.class,
        ConsulConfigAutoConfiguration.class
})
@ActiveProfiles("test")
public class UserFeignClientResilienceTest {

    @Autowired
    private UserFeignClient userFeignClient;
    

    @Test
    void shouldTriggerFallback_whenServiceUnavailable() {
        UserDto result = userFeignClient.findById(123L);

        assertNotNull(result);
        assertEquals("fallback-user", result.name());
        assertEquals(0L, result.phone());
    }

    @Test
    void shouldOpenCircuitAfterFailureThresholdExceeded() {
        for (int i = 0; i < 5; i++) {
            stubFor(get(urlEqualTo("/v1/users/" + i))
                    .willReturn(aResponse().withStatus(503)));
            userFeignClient.findById((long) i);
        }

        // Circuit должен быть OPEN → fallback вызовется без попытки HTTP
        UserDto result = userFeignClient.findById(999L);
        assertEquals("fallback-user", result.name());
    }

}
