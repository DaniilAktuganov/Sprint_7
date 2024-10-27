package ru.yandex.praktikum.sprint7;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.sprint7.client.CourierClient;
import ru.yandex.praktikum.sprint7.client.OrderClient;
import ru.yandex.praktikum.sprint7.models.Courier;
import ru.yandex.praktikum.sprint7.models.Order;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static ru.yandex.praktikum.sprint7.generators.CourierGenerator.randomCourier;
import static ru.yandex.praktikum.sprint7.generators.OrderGenerator.randomOrder;

public class AcceptOrderTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
    }

    @Test
    @DisplayName("Успешное принятие заказа")
    @Description("Проверка, что заказ успешно принят")
    public void acceptOrderTest() {
        CourierClient courierClient = new CourierClient();
        OrderClient orderClient = new OrderClient();
        Courier courier = randomCourier();
        Order order = randomOrder();
        courierClient.sendPostRequestV1Courier(courier);
        orderClient.sendPostRequestV1Orders(order);
        Response response = orderClient.sendPutRequestV1OrdersAcceptId(courier, order);
        assertEquals("Неверный статус код", SC_OK, response.statusCode());
        response.then().body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Принятие заказа без id курьера")
    @Description("Невозможно принять заказ без id курьера")
    public void acceptOrderWithoutCourierIdTest() {
        CourierClient courierClient = new CourierClient();
        OrderClient orderClient = new OrderClient();
        Courier courier = randomCourier();
        Order order = randomOrder();
        courierClient.sendPostRequestV1Courier(courier);
        orderClient.sendPostRequestV1Orders(order);
        Response response = orderClient.sendPutRequestV1OrdersAcceptIdWithoutCourierId(order);
        assertEquals("Неверный статус код", SC_BAD_REQUEST, response.statusCode());
        response.then().body("message", equalTo("Недостаточно данных для поиска"));
    }

    @Test
    @DisplayName("Принятие заказа с неверным id курьера")
    @Description("Невозможно принять заказ с неверным id курьера")
    public void acceptOrderWithInvalidCourierIdTest() {
        CourierClient courierClient = new CourierClient();
        OrderClient orderClient = new OrderClient();
        Courier courier = randomCourier();
        Order order = randomOrder();
        courierClient.sendPostRequestV1Courier(courier);
        orderClient.sendPostRequestV1Orders(order);
        Response response = orderClient.sendPutRequestV1OrdersAcceptIdWithInvalidCourierId(order);
        assertEquals("Неверный статус код", SC_NOT_FOUND, response.statusCode());
        response.then().body("message", equalTo("Курьера с таким id не существует"));
    }

    @Test
    @DisplayName("Принятие заказа без id заказа")
    @Description("Невозможно принять заказ без id заказа")
    public void acceptOrderWithoutOrderIdTest() {
        CourierClient courierClient = new CourierClient();
        OrderClient orderClient = new OrderClient();
        Courier courier = randomCourier();
        Order order = randomOrder();
        courierClient.sendPostRequestV1Courier(courier);
        orderClient.sendPostRequestV1Orders(order);
        Response response = orderClient.sendPutRequestV1OrdersAcceptIdWithoutOrderId(courier);
        assertEquals("Неверный статус код", SC_BAD_REQUEST, response.statusCode());
        response.then().body("message", equalTo("Недостаточно данных для поиска"));
    }

    @Test
    @DisplayName("Принятие заказа с неверным id заказа")
    @Description("Невозможно принять заказ с неверным id заказа")
    public void acceptOrderWithInvalidOrderIdTest() {
        CourierClient courierClient = new CourierClient();
        OrderClient orderClient = new OrderClient();
        Courier courier = randomCourier();
        Order order = randomOrder();
        courierClient.sendPostRequestV1Courier(courier);
        orderClient.sendPostRequestV1Orders(order);
        Response response = orderClient.sendPutRequestV1OrdersAcceptIdWithInvalidOrderId(courier);
        assertEquals("Неверный статус код", SC_NOT_FOUND, response.statusCode());
        response.then().body("message", equalTo("Заказа с таким id не существует"));
    }
}