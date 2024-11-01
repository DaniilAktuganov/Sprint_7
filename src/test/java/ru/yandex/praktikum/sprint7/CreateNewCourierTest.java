package ru.yandex.praktikum.sprint7;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.sprint7.client.CourierClient;
import ru.yandex.praktikum.sprint7.models.Courier;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static ru.yandex.praktikum.sprint7.generators.CourierGenerator.*;

public class CreateNewCourierTest {

    private CourierClient courierClient;
    private Courier courier;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
    }

    @Test
    @DisplayName("Успешное создание курьера")
    @Description("Проверка, что курьера можно создать с корректными данными")
    public void createNewCourierTest() {
        courier = createDefaultCourier();
        Response response = courierClient.sendPostRequestV1Courier(courier);
        assertEquals("Неверный статус код", SC_CREATED, response.statusCode());
        response.then().body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Проверка создания двух одинаковых курьеров")
    @Description("Невозможно создать двух одинаковых курьеров")
    public void createTwoIdenticalCouriersTest() {
        courier = createDefaultCourier();
        courierClient.sendPostRequestV1Courier(courier);
        Response response = courierClient.sendPostRequestV1Courier(courier);
        assertEquals("Неверный статус код", SC_CONFLICT, response.statusCode());
        response.then().body("message", equalTo("Этот логин уже используется"));
    }

    @Test
    @DisplayName("Проверка создания курьера без логина")
    @Description("Невозможно создать курьера без логина")
    public void createNewCourierWithoutLoginTest() {
        courier = createDefaultCourierWithoutLogin();
        Response response = courierClient.sendPostRequestV1Courier(courier);
        assertEquals("Неверный статус код", SC_BAD_REQUEST, response.statusCode());
        response.then().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Проверка создания курьера без пароля")
    @Description("Невозможно создать курьера без пароля")
    public void createNewCourierWithoutPasswordTest() {
        courier = createDefaultCourierWithoutPassword();
        Response response = courierClient.sendPostRequestV1Courier(courier);
        assertEquals("Неверный статус код", SC_BAD_REQUEST, response.statusCode());
        response.then().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @After
    public void tearDown() {
        courierClient.sendDeleteRequestV1Courier(courier);
        }
}