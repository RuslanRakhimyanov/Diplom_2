package ru.yandex.praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.users.*;

import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;
import static org.hamcrest.Matchers.equalTo;
import static ru.yandex.praktikum.Endpoints.BASE_URL;

public class UserLoginTest {
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
    @DisplayName("Проверка авторизации под существующим пользователем")
    @Description("Проверка авторизации под существующим пользователем")
    public void loginUserTest() {
        json = "{" + userLogin + "\"," + userName + "\"," + userPassword + "\"}";
        userRequests.loginUser(json)
                .then()
                .assertThat()
                .statusCode(HTTP_OK)
                .and()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Проверка авторизации c неверными логином и паролем")
    @Description("Проверка авторизации c неверными логином и паролем")
    public void loginUserNoValidEmailAndPasswordTest() {
        String addPassword = RandomStringUtils.randomAlphabetic(5);
        json = "{" + userLogin.replaceFirst("@gmail", "@yandex") + "\"," + userName + "\"," + userPassword + addPassword + "\"}";
        userRequests.loginUser(json)
                .then()
                .assertThat()
                .statusCode(HTTP_UNAUTHORIZED)
                .and()
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Проверка авторизации c несуществующим логином")
    @Description("Проверка авторизации c несуществующим логином")
    public void loginUserNoValidEmailTest() {
        json = "{" + userLogin.replaceFirst("@gmail", "@yandex") + "\"," + userName + "\"," + userPassword + "\"}";
        userRequests.loginUser(json)
                .then()
                .assertThat()
                .statusCode(HTTP_UNAUTHORIZED)
                .and()
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Проверка авторизации c неверным паролем")
    @Description("Проверка авторизации c неверным паролем")
    public void loginUserNoValidPasswordTest() {
        String addPassword = RandomStringUtils.randomAlphabetic(5);
        json = "{" + userLogin + "\"," + userName + "\"," + userPassword + addPassword + "\"}";
        userRequests.loginUser(json)
                .then()
                .assertThat()
                .statusCode(HTTP_UNAUTHORIZED)
                .and()
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }

}
