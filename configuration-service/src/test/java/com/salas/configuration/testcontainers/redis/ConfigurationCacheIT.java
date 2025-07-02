package com.salas.configuration.testcontainers.redis;

import com.salas.configuration.entities.ConfigurationTags;
import com.salas.configuration.repository.ConfigurationTagsRepo;
import com.salas.configuration.service.ConfigurationTagsService;
import com.salas.configuration.testcontainers.db.AbstractIntegrationTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;

import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ConfigurationCacheIT extends AbstractIntegrationTest {

    @Container
    static GenericContainer<?> redis = new GenericContainer(DockerImageName.parse("redis:7.2"))
            .withExposedPorts(6379);

    @Autowired
    ConfigurationTagsService configurationTagsService;
    @Autowired
    private ConfigurationTagsRepo repository;
    @SpyBean
    private ConfigurationTagsRepo spyRepository;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private CacheManager cacheManager;

    Integer id;

    @BeforeEach
    void init() {
        clearCache();
        ConfigurationTags entity = new ConfigurationTags();
        entity.setPlatform("Mobile");
        entity.setMessage("Test Tag");

        id = repository.save(entity).getId();
    }

    private void clearCache() {
        if (cacheManager != null) {
            cacheManager.getCacheNames().forEach(cacheName -> {
                Cache cache = cacheManager.getCache(cacheName);
                if (cache != null) {
                    cache.clear();
                }
            });
        }
    }

    @DynamicPropertySource
    static void overrideRedisProps(DynamicPropertyRegistry registry) {
        registry.add("spring.data.redis.host", redis::getHost);
        registry.add("spring.data.redis.port", () -> redis.getMappedPort(6379));
    }

    @Test
    void shouldEvictCacheAfterTTL() {
        configurationTagsService.findById(id);
        await().atMost(Duration.ofSeconds(7))
                .untilAsserted(() -> {
                    configurationTagsService.findById(id);
                    verify(spyRepository, times(2)).findById(id);
                });
    }

    @Test
    void shouldHitCache() {
        configurationTagsService.findById(id);
        await().atMost(Duration.ofSeconds(3))
                .untilAsserted(() -> {
                    configurationTagsService.findById(id);
                    verify(spyRepository, times(1)).findById(id);
                });
    }

    @Test
    void redisMustContainKey() {
        configurationTagsService.findById(id);
        String key = "configTags::" + id;
        Boolean has = redisTemplate.hasKey(key);
        Assertions.assertEquals(Boolean.TRUE, has);
    }
}
