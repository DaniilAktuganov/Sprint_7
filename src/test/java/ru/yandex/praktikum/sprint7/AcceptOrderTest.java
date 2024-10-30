package ru.yandex.praktikum.sprint7;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.sprint7.client.CourierClient;
import ru.yandex.praktikum.sprint7.client.OrderClient;
import ru.yandex.praktikum.sprint7.models.Courier;
import ru.yandex.praktikum.sprint7.models.Order;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static ru.yandex.praktikum.sprint7.generators.CourierGenerator.createDefaultCourier;
import static ru.yandex.praktikum.sprint7.generators.OrderGenerator.createDefaultOrder;

public class AcceptOrderTest {

    private CourierClient courierClient;
    private OrderClient orderClient;
    private Courier courier;
    private Order order;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
        orderClient = new OrderClient();
        courier = createDefaultCourier();
        order = createDefaultOrder();
        courierClient.sendPostRequestV1Courier(courier);
        orderClient.sendPostRequestV1Orders(order);
    }

    @Test
    @DisplayName("Успешное принятие заказа")
    @Description("Проверка, что заказ успешно принят")
    public void acceptOrderTest() {
        Response response = orderClient.sendPutRequestV1OrdersAcceptId(courier, order);
        assertEquals("Неверный статус код", SC_OK, response.statusCode());
        response.then().body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Принятие заказа без id курьера")
    @Description("Невозможно принять заказ без id курьера")
    public void acceptOrderWithoutCourierIdTest() {
        Response response = orderClient.sendPutRequestV1OrdersAcceptIdWithoutCourierId(order);
        assertEquals("Неверный статус код", SC_BAD_REQUEST, response.statusCode());
        response.then().body("message", equalTo("Недостаточно данных для поиска"));
    }

    @Test
    @DisplayName("Принятие заказа с неверным id курьера")
    @Description("Невозможно принять заказ с неверным id курьера")
    public void acceptOrderWithInvalidCourierIdTest() {
        Response response = orderClient.sendPutRequestV1OrdersAcceptIdWithInvalidCourierId(order);
        assertEquals("Неверный статус код", SC_NOT_FOUND, response.statusCode());
        response.then().body("message", equalTo("Курьера с таким id не существует"));
    }

    @Test
    @DisplayName("Принятие заказа без id заказа")
    @Description("Невозможно принять заказ без id заказа")
    public void acceptOrderWithoutOrderIdTest() {
        Response response = orderClient.sendPutRequestV1OrdersAcceptIdWithoutOrderId(courier);
        assertEquals("Неверный статус код", SC_BAD_REQUEST, response.statusCode());
        response.then().body("message", equalTo("Недостаточно данных для поиска"));
    }

    @Test
    @DisplayName("Принятие заказа с неверным id заказа")
    @Description("Невозможно принять заказ с неверным id заказа")
    public void acceptOrderWithInvalidOrderIdTest() {
        Response response = orderClient.sendPutRequestV1OrdersAcceptIdWithInvalidOrderId(courier);
        assertEquals("Неверный статус код", SC_NOT_FOUND, response.statusCode());
        response.then().body("message", equalTo("Заказа с таким id не существует"));
    }

    @After
    public void tearDown() {
        courierClient.sendDeleteRequestV1Courier(courier);
    }
}