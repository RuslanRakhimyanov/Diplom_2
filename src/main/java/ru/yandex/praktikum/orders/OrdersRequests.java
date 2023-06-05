package ru.yandex.praktikum.orders;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.util.List;

import static io.restassured.RestAssured.given;
import static ru.yandex.praktikum.Endpoints.ORDER_URL;

public class OrdersRequests {
    private final List<String> ingredientsListTest = List.of("61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa6f", "61c0c5a71d1f82001bdaaa70", "61c0c5a71d1f82001bdaaa72");
    private final List<String> noValidIngredientsListTest = List.of("60dqwert7034a000269f45e7", "60dqwert7034a000269f45e9", "60dqwert7034a000269f45e8", "60dqwert7034a000269f45ea");

    public List<String> getIngredientsListTest() {
        return ingredientsListTest;
    }

    public List<String> getNoValidIngredientsListTest() {
        return noValidIngredientsListTest;
    }

    @Step("Создание заказа c авторизацией и c существующими ингредиентами")
    public Response createOrder(Orders orders, String token) {
        return given()
                .contentType(ContentType.JSON)
                .auth().oauth2(token)
                .body(orders)
                .when()
                .post(ORDER_URL);
    }

    @Step("Создание заказа без авторизации c существующими ингредиентами")
    public Response createOrderUnauthorizedUser(Orders orders) {
        return given()
                .contentType(ContentType.JSON)
                .body(orders)
                .when()
                .post(ORDER_URL);
    }

    @Step("Создание заказа c авторизацией без ингредиентов")
    public Response createOrderWithoutIngredients(String token) {
        return given()
                .contentType(ContentType.JSON)
                .auth().oauth2(token)
                .when()
                .post(ORDER_URL);
    }

    @Step("Создание заказа c авторизацией и несуществующими ингредиентами")
    public Response createOrderWithNoValidIngredients(Orders orders, String token) {
        return given()
                .contentType(ContentType.JSON)
                .auth().oauth2(token)
                .body(orders)
                .when()
                .post(ORDER_URL);
    }

    @Step("Получение заказов от авторизованного пользователя")
    public Response getOrdersWithAuthorization(String token) {
        return given()
                .contentType(ContentType.JSON)
                .auth().oauth2(token)
                .when()
                .get(ORDER_URL);
    }

    @Step("Получение заказов от неавторизованного пользователя")
    public Response getOrdersWithoutAuthorization() {
        return given()
                .contentType(ContentType.JSON)
                .when()
                .get(ORDER_URL);
    }
}
