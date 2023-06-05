package ru.yandex.praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.orders.*;
import ru.yandex.praktikum.users.*;

import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;
import static org.hamcrest.Matchers.equalTo;
import static ru.yandex.praktikum.Endpoints.BASE_URL;

public class GetOrderTest {
    private final OrdersRequests ordersRequests = new OrdersRequests();
    private final UserRequests userRequests = new UserRequests();
    private final UserGeneration userGeneration = new UserGeneration();
    private final String userLogin = userGeneration.getLogin();
    private final String userPassword = userGeneration.getPassword();
    private final String userName = userGeneration.getName();
    private String token;

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
    @DisplayName("Получение заказа от авторизованного пользователя")
    @Description("Получение заказа от авторизованного пользователя")
    public void getOrdersWithAuthorizationTest() {
        Orders ingredients = new Orders(ordersRequests.getIngredientsListTest());
        ordersRequests.createOrder(ingredients, token.substring(7));
        ordersRequests.getOrdersWithAuthorization(token.substring(7))
                .then()
                .assertThat()
                .statusCode(HTTP_OK)
                .and()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Получение заказа не авторизованным пользователем")
    @Description("Получение заказа не авторизованным пользователем")
    public void getOrdersWithoutAuthorizationTest() {
        ordersRequests.getOrdersWithoutAuthorization()
                .then()
                .assertThat()
                .statusCode(HTTP_UNAUTHORIZED)
                .and()
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }
}


