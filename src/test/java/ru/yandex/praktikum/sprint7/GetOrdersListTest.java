package ru.yandex.praktikum.sprint7;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;
import ru.yandex.praktikum.sprint7.client.OrderClient;
import ru.yandex.praktikum.sprint7.models.Order;

import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.Assert.assertEquals;
import static ru.yandex.praktikum.sprint7.generators.OrderGenerator.createDefaultOrder;
import static org.hamcrest.Matchers.*;

public class GetOrdersListTest {

    @Test
    @DisplayName("Получение списка заказов")
    @Description("Проверка, что в тело ответа возвращается список заказов")
    public void ordersListTest() {
        OrderClient orderClient = new OrderClient();
        Order order = createDefaultOrder();
        orderClient.sendPostRequestV1Orders(order);
        Response response = orderClient.sendGetRequestV1Orders();
        assertEquals("Неверный статус код", SC_OK, response.statusCode());
        response.then().assertThat().body("orders", is(not(empty())));
    }
}