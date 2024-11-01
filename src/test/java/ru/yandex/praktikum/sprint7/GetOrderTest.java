package ru.yandex.praktikum.sprint7;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.sprint7.client.OrderClient;
import ru.yandex.praktikum.sprint7.models.Order;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static ru.yandex.praktikum.sprint7.generators.OrderGenerator.createDefaultOrder;

public class GetOrderTest {

    private OrderClient orderClient;

    @Before
    public void setUp() {
        orderClient = new OrderClient();
    }

    @Test
    @DisplayName("Успешное получение заказа по его номеру")
    @Description("Проверка, что заказ можно получить по его номеру")
    public void getOrderTest() {
        Order order = createDefaultOrder();
        orderClient.sendPostRequestV1Orders(order);
        int track = orderClient.getOrderTrack(order);
        Response response = orderClient.sendGetRequestV1OrdersTrack(track);
        assertEquals("Неверный статус код", SC_OK, response.statusCode());
        response.then().assertThat().body("order", notNullValue());
    }

    @Test
    @DisplayName("Получение заказа без номера")
    @Description("Невозможно получить заказ без номера")
    public void getOrderWithoutTrackTest() {
        Response response = orderClient.sendGetRequestV1OrdersTrackWithoutTrack();
        assertEquals("Неверный статус код", SC_BAD_REQUEST, response.statusCode());
        response.then().body("message", equalTo("Недостаточно данных для поиска"));
    }

    @Test
    @DisplayName("Получение заказа с несуществующим номером")
    @Description("Невозможно получить заказ с несуществующим номером")
    public void getOrderWithInvalidTrackTest() {
        int track = 999999999;
        Response response =orderClient.sendGetRequestV1OrdersTrack(track);
        assertEquals("Неверный статус код", SC_NOT_FOUND, response.statusCode());
        response.then().body("message", equalTo("Заказ не найден"));
    }
}