package com.salas.admin.clients;

import com.salas.admin.configuration.FeignConfig;
import com.salas.admin.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.salas.admin.clients.UserFeignClient.BASE_PATH;
import static com.salas.admin.clients.UserFeignClient.FEIGN_CLIENT_NAME;

@FeignClient(
        name = "configuration-service",
        path = BASE_PATH,
        contextId = FEIGN_CLIENT_NAME,
        fallbackFactory = UserFeignClient.UserFallback.class,
        configuration = FeignConfig.class
)
public interface UserFeignClient {
    String FEIGN_CLIENT_NAME = "users-feign-client";
    String BASE_PATH = "/v1/users";

    @GetMapping
    List<UserDto> findAll();

    @PostMapping
    UserDto create(@RequestBody UserDto userDto);

    @GetMapping("/{id}")
    UserDto findById(@PathVariable Long id);

    @DeleteMapping("/{id}")
    void delete(@PathVariable Long id);

    @Slf4j
    @Component
    class UserFallback implements FallbackFactory<UserFeignClient> {
        @Override
        public UserFeignClient create(Throwable cause) {
            log.warn("Feign fallback triggered: {}", cause.toString());

            return new UserFeignClient() {
                private final UserDto defaultUser = new UserDto(null, "fallback-user", "", 0L);

                @Override
                public List<UserDto> findAll() {
                    log.warn("Fallback: findAll()");
                    return List.of();
                }

                @Override
                public UserDto create(UserDto userDto) {
                    log.warn("Fallback: create()");
                    return defaultUser;
                }

                @Override
                public UserDto findById(Long id) {
                    log.warn("Fallback: findById({}) — cause: {}", id, cause.toString());
                    return defaultUser;
                }

                @Override
                public void delete(Long id) {
                    log.warn("Fallback: delete({})", id);
                }
            };
        }
    }


}
