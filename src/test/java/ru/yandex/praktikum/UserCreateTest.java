package ru.yandex.praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.users.*;

import static java.net.HttpURLConnection.HTTP_FORBIDDEN;
import static java.net.HttpURLConnection.HTTP_OK;
import static org.hamcrest.Matchers.equalTo;
import static ru.yandex.praktikum.Endpoints.BASE_URL;

public class UserCreateTest {
    private final UserRequests userRequests = new UserRequests();
    private final UserGeneration userGeneration = new UserGeneration();
    private final String userLogin = userGeneration.getLogin();
    private final String userPassword = userGeneration.getPassword();
    private final String userName = userGeneration.getName();
    private String json;
    private String token;

    @Before
    @Description("Подготовка тестовых данных перед тестированием")
    public void setURL() {
        RestAssured.baseURI = BASE_URL;
        token = null;
    }

    @After
    @Description("Удаление тестовых данных после тестирования")
    public void deleteTestUser() {
        if (token != null) userRequests.deleteUser(token.substring(7));
    }

    @Test
    @DisplayName("Проверка создания нового пользователя")
    @Description("Проверка создания нового пользователя")
    public void userRegistrationTest() {
        json = "{" + userLogin + "\"," + userName + "\"," + userPassword + "\"}";
        token = userRequests.userRegistration(json)
                .then()
                .assertThat()
                .statusCode(HTTP_OK)
                .and()
                .body("success", equalTo(true))
                .extract().path("accessToken");
    }

    @Test
    @DisplayName("Проверка создания дубликата пользователя")
    @Description("Проверка создания дубликата пользователя")
    public void userRegistrationDuplicateTest() {
        json = "{" + userLogin + "\"," + userName + "\"," + userPassword + "\"}";
        token = userRequests.userRegistration(json)
                .then()
                .assertThat()
                .statusCode(HTTP_OK)
                .and()
                .body("success", equalTo(true))
                .extract().path("accessToken");
        userRequests.userRegistration(json)
                .then()
                .assertThat()
                .statusCode(HTTP_FORBIDDEN)
                .and()
                .body("success", equalTo(false))
                .body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Проверка создания пользователя без поля email")
    @Description("Проверка создания пользователя без поля email")
    public void userRegistrationNoEmailTest() {
        json = "{" + userName + "\"," + userPassword + "\"}";
        userRequests.userRegistration(json)
                .then()
                .assertThat()
                .statusCode(HTTP_FORBIDDEN)
                .and()
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Проверка создания пользователя без поля name")
    @Description("Проверка создания пользователя без поля name")
    public void userRegistrationNoNameTest() {
        json = "{" + userLogin + "\"," + userPassword + "\"}";
        userRequests.userRegistration(json)
                .then()
                .assertThat()
                .statusCode(HTTP_FORBIDDEN)
                .and()
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Проверка создания пользователя без поля password")
    @Description("Проверка создания пользователя без поля password")
    public void userRegistrationNoPasswordTest() {
        json = "{" + userLogin + "\"," + userName + "\"}";
        userRequests.userRegistration(json)
                .then()
                .assertThat()
                .statusCode(HTTP_FORBIDDEN)
                .and()
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

}
