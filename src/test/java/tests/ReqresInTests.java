package tests;

import models.LombokUserData;
import models.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static spec.Specs.request;
import static spec.Specs.responseSpec200;

public class ReqresInTests extends TestBase {

    @Tag("Api")
    @Test
    @DisplayName("Проверка email, при помощи Groovy")
    void checkEmailWithGroovy() {
        given()
                .spec(request)
                .when()
                .get("/users")
                .then()
                .spec(responseSpec200)
                .log().body()
                .body("data.findAll{it.email =~/.*?@reqres.in/}.email.flatten()",
                        hasItems("eve.holt@reqres.in"));
    }

    @Tag("Api")
    @Test
    @DisplayName("Успешная регистрация")
    void successfulRegister() {

        User user = new User();
        user.setEmail("eve.holt@reqres.in");
        user.setPassword("pistol");

        User response = given()
                .spec(request)
                .body(user)
                .when()
                .post("/register")
                .then()
                .spec(responseSpec200)
                .log().body()
                .extract().as(User.class);

        assertEquals("4", response.getId());
        assertEquals("QpwL5tke4Pnpja7X4", response.getToken());
    }

    @Tag("Api")
    @Test
    @DisplayName("Успешная авторизация")
    void successfulLogin() {
        User user = new User();
        user.setEmail("eve.holt@reqres.in");
        user.setPassword("cityslicka");

        User response = given()
                .spec(request)
                .body(user)
                .when()
                .post("/login")
                .then()
                .spec(responseSpec200)
                .log().body()
                .extract().as(User.class);

        assertEquals(response.getToken(), "QpwL5tke4Pnpja7X4");
    }

    @Tag("Api")
    @Test
    @DisplayName("Неуспешная авторизация")
    void unsuccessfulLogin() {
        User user = new User();
        user.setEmail("peter@klaven");

        User response = given()
                .spec(request)
                .body(user)
                .when()
                .post("/login")
                .then()
                .statusCode(400)
                .log().body()
                .extract().as(User.class);

        assertEquals(response.getError(), "Missing password");
    }

    @Tag("Api")
    @Test
    @DisplayName("Создание нового пользователя")
    void createUser() {
        User user = new User();
        user.setName("morpheus");
        user.setJob("leader");

        User response = given()
                .spec(request)
                .body(user)
                .when()
                .post("/users")
                .then()
                .statusCode(201)
                .log().body()
                .extract().as(User.class);
        assertEquals(response.getName(), user.getName());
        assertEquals(response.getJob(), user.getJob());
    }

    @Tag("Api")
    @Test
    @DisplayName("Обновление данных пользователя")
    void updateUser() {
        User user = new User();
        user.setName("morpheus");
        user.setJob("zion resident");

        User response = given()
                .spec(request)
                .body(user)
                .when()
                .put("/users/2")
                .then()
                .spec(responseSpec200)
                .log().body()
                .extract().as(User.class);
        assertEquals(response.getName(), user.getName());
        assertEquals(response.getJob(), user.getJob());
    }

    @Tag("Api")
    @Test
    @DisplayName("Поиск пользователя")
    void singleUser() {

        LombokUserData response = given()
                .spec(request)
                .when()
                .get("/users/2")
                .then()
                .spec(responseSpec200)
                .log().body()
                .extract().as(LombokUserData.class);

        assertEquals("2", response.getUser().getId());
        assertEquals("janet.weaver@reqres.in", response.getUser().getEmail());
    }

}
