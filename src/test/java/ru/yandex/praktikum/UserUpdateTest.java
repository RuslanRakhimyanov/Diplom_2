package ru.yandex.praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.users.*;

import static java.net.HttpURLConnection.*;
import static org.hamcrest.Matchers.equalTo;
import static ru.yandex.praktikum.Endpoints.BASE_URL;

public class UserUpdateTest {
    private final UserRequests userRequests = new UserRequests();
    private final UserGeneration userGeneration = new UserGeneration();
    private final String userLogin = userGeneration.getLogin();
    private final String userPassword = userGeneration.getPassword();
    private final String userName = userGeneration.getName();
    private String json;
    private String token;

    @Before
    @Description("Создание тестовых данных перед тестированием")
    public void createTestUser() {
        RestAssured.baseURI = BASE_URL;
        token = null;
        json = "{" + userLogin + "\"," + userName + "\"," + userPassword + "\"}";
        token = userRequests.userRegistration(json).then().extract().path("accessToken");
    }

    @After
    @Description("Удаление тестовых данных после тестирования")
    public void deleteTestUser() {
        if (token != null) userRequests.deleteUser(token.substring(7));
    }

    @Test
    @DisplayName("Проверка изменения поля email у авторизованного пользователя")
    @Description("Проверка изменения поля email у авторизованного пользователя")
    public void editEmailUserTest() {
        json = "{" + userLogin.replaceFirst("@mail", "@yandex") + "\"," + userName + "\"," + userPassword + "\"}";
        String email = userGeneration.getEmail().replaceFirst("@mail", "@yandex");
        String name = userGeneration.getRndName();
        userRequests.editAuthorUser(json, token.substring(7))
                .then()
                .assertThat()
                .statusCode(HTTP_OK)
                .and()
                .body("success", equalTo(true))
                .body("user.email", equalTo(email))
                .body("user.name", equalTo(name));
    }

    @Test
    @DisplayName("Проверка изменения поля name у авторизованного пользователя")
    @Description("Проверка изменения поля name у авторизованного пользователя")
    public void editNameUserTest() {
        String updateName = RandomStringUtils.randomAlphabetic(3);
        json = "{" + userLogin + "\"," + userName + updateName + "\"," + userPassword + "\"}";
        String email = userGeneration.getEmail();
        String name = userGeneration.getRndName() + updateName;
        userRequests.editAuthorUser(json, token.substring(7))
                .then()
                .assertThat()
                .statusCode(HTTP_OK)
                .and()
                .body("success", equalTo(true))
                .body("user.email", equalTo(email))
                .body("user.name", equalTo(name));
    }

    @Test
    @DisplayName("Проверка изменения поля password у авторизованного пользователя")
    @Description("Проверка изменения поля password у авторизованного пользователя")
    public void editPasswordUserTest() {
        String updatePassword = RandomStringUtils.randomAlphabetic(3);
        json = "{" + userLogin + "\"," + userName + "\"," + userPassword + updatePassword + "\"}";
        String email = userGeneration.getEmail();
        String name = userGeneration.getRndName();
        userRequests.editAuthorUser(json, token.substring(7))
                .then()
                .assertThat()
                .statusCode(HTTP_OK)
                .and()
                .body("success", equalTo(true))
                .body("user.email", equalTo(email))
                .body("user.name", equalTo(name));
    }

    @Test
    @DisplayName("Проверка изменения поля email на уже существующий у авторизованного пользователя")
    @Description("Проверка изменения поля email на уже существующий у авторизованного пользователя")
    public void editDuplicateEmailUserTest() {
        String updatePassword = RandomStringUtils.randomAlphabetic(3);
        String updateName = RandomStringUtils.randomAlphabetic(3);
        String tokenSecond;
        String jsonSecond = "{" + userLogin.replaceFirst("@gmail", "@yandex") + "\"," +
                userName + updateName + "\"," + userPassword + updatePassword + "\"}";
        tokenSecond = userRequests.userRegistration(jsonSecond).then().extract().path("accessToken");
        json = "{" + userLogin.replaceFirst("@gmail", "@yandex") + "\"," +
                userName + "\"," + userPassword + "\"}";
        userRequests.editAuthorUser(json, token.substring(7))
                .then()
                .assertThat()
                .statusCode(HTTP_FORBIDDEN)
                .and()
                .body("success", equalTo(false))
                .body("message", equalTo("User with such email already exists"));
        if (tokenSecond != null) userRequests.deleteUser(tokenSecond.substring(7));
    }

    @Test
    @DisplayName("Проверка изменения полей у неавторизованного пользователя")
    @Description("Проверка изменения полей у неавторизованного пользователя")
    public void editFieldsNotAuthUserTest() {
        String updatePassword = RandomStringUtils.randomAlphabetic(3);
        String updateName = RandomStringUtils.randomAlphabetic(3);
        json = "{" + userLogin.replaceFirst("@mail", "@yandex") + "\"," +
                userName + updateName + "\"," + userPassword + updatePassword + "\"}";
        userRequests.editNoAuthorUser(json)
                .then()
                .assertThat()
                .statusCode(HTTP_UNAUTHORIZED)
                .and()
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }
}
