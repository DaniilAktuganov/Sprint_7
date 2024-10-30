package ru.yandex.praktikum.sprint7;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.praktikum.sprint7.client.OrderClient;
import ru.yandex.praktikum.sprint7.models.Order;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static ru.yandex.praktikum.sprint7.generators.OrderGenerator.createDefaultOrder;

@RunWith(Parameterized.class)
public class CreateOrderTest {

    private final String[] color;

    public CreateOrderTest(String[] color) {
        this.color = color;
    }

    @Parameterized.Parameters
    public static Object[][] data () {
        return new Object[][] {
                {new String[]{"BLACK"}},
                {new String[]{"GREY"}},
                {new String[]{"BLACK", "GREY"}},
                {new String[]{}}
        };
    }

    @Test
    @DisplayName("Создание заказа")
    @Description("Успешное создание заказа")
    public void createOrderTest() {
        Order order = createDefaultOrder().withColor(color);
        OrderClient orderClient = new OrderClient();
        Response response = orderClient.sendPostRequestV1Orders(order);
        assertEquals("Неверный статус код", SC_CREATED, response.statusCode());
        response.then().assertThat().body("track", notNullValue());
    }
}