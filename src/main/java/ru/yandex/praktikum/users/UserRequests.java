package ru.yandex.praktikum.users;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static ru.yandex.praktikum.Endpoints.*;

public class UserRequests {

    @Step("Создание нового пользователя")
    public Response userRegistration(String json) {
        return given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post(REGISTER_URL);
    }

    @Step("Авторизация пользователя")
    public Response loginUser(String json) {
        return given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post(LOGIN_URL);
    }

    @Step("Удаление пользователя")
    public void deleteUser(String token) {
        given()
                .contentType(ContentType.JSON)
                .auth().oauth2(token)
                .when()
                .delete(USER_URL);
    }

    @Step("Изменение данных пользователя с авторизацией")
    public Response editAuthorUser(String json, String token) {
        return given()
                .contentType(ContentType.JSON)
                .auth().oauth2(token)
                .body(json)
                .when()
                .patch(USER_URL);
    }

    @Step("Изменение данных пользователя без авторизации")
    public Response editNoAuthorUser(String json) {
        return given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .patch(USER_URL);
    }

}
