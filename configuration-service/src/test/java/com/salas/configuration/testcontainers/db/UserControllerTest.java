package com.salas.configuration.testcontainers.db;

import com.salas.configuration.entities.User;
import com.salas.configuration.repository.UserRepo;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasSize;


public class UserControllerTest extends AbstractIntegrationTest {
    @LocalServerPort
    private Integer port;

    @Autowired
    public UserRepo userRepo;

    @BeforeEach
    public void setup() {
        RestAssured.baseURI = "http://localhost:" + port;
    }

    @BeforeEach
    void setUp() {
        userRepo.deleteAll();
    }

    @Test
    public void shutGetAllUsers() {
        User vlad = new User("Vlad", "salas@vk.kh.ua", 380632836733L);
        User sergey = new User("Sergey", "sergey@vk.kh.ua", 380632836733L);

        userRepo.saveAll(List.of(vlad, sergey));

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/v1/users")
                .then()
                .statusCode(200)
                .body(".", hasSize(2));
    }
}
