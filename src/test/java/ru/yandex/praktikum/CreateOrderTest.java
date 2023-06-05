package ru.yandex.praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ru.yandex.praktikum.orders.*;
import ru.yandex.praktikum.users.*;

import static java.net.HttpURLConnection.*;
import static org.hamcrest.Matchers.equalTo;
import static ru.yandex.praktikum.Endpoints.BASE_URL;

public class CreateOrderTest {
    private final OrdersRequests ordersRequests = new OrdersRequests();
    private final UserRequests userRequests = new UserRequests();
    private final UserGeneration userGeneration = new UserGeneration();
    private final String userLogin = userGeneration.getLogin();
    private final String userPassword = userGeneration.getPassword();
    private final String userName = userGeneration.getName();
    private String token;
    private Orders ingredients;

    @Before
    @Description("Создание тестовых данных перед тестированием")
    public void createTestUser() {
        RestAssured.baseURI = BASE_URL;
        token = null;
        String json = "{" + userLogin + "\"," + userName + "\"," + userPassword + "\"}";
        token = userRequests.userRegistration(json).then().extract().path("accessToken");
    }

    @After
    @Description("Удаление тестовых данных после тестирования")
    public void deleteTestUser() {
        if (token != null) userRequests.deleteUser(token.substring(7));
    }

    @Test
    @DisplayName("Создание заказа авторизованным пользователем")
    @Description("Создание заказа авторизованным пользователем")
    public void createOrderAuthorizedUserTest() {
        Orders ingredients = new Orders(ordersRequests.getIngredientsListTest());
        ordersRequests.createOrder(ingredients, token.substring(7))
                .then()
                .assertThat()
                .statusCode(HTTP_OK)
                .and()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание заказа не авторизованным пользователем")
    @Description("Создание заказа не авторизованным пользователем")
    public void createOrderUnauthorizedUserTest() {
        ingredients = new Orders(ordersRequests.getIngredientsListTest());
        ordersRequests.createOrderUnauthorizedUser(ingredients)
                .then()
                .assertThat()
                .statusCode(HTTP_OK)
                .and()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание заказа авторизованным пользователем без ингредиентов")
    @Description("Создание заказа авторизованным пользователем без ингредиентов")
    public void createOrderWithoutIngredientsTest() {
        ordersRequests.createOrderWithoutIngredients(token.substring(7))
                .then()
                .assertThat()
                .statusCode(HTTP_BAD_REQUEST)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа авторизованным пользователем с неправильными ингредиентами")
    @Description("Создание заказа авторизованным пользователем с неправильными ингредиентами")
    public void createOrderWithNoValidIngredientsTest() {
        Orders ingredients = new Orders(ordersRequests.getNoValidIngredientsListTest());
        ordersRequests.createOrderWithNoValidIngredients(ingredients, token.substring(7))
                .then()
                .assertThat()
                .statusCode(HTTP_INTERNAL_ERROR);
    }

}
