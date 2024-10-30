package ru.yandex.praktikum.sprint7;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.sprint7.client.CourierClient;
import ru.yandex.praktikum.sprint7.models.Courier;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static ru.yandex.praktikum.sprint7.generators.CourierGenerator.randomCourier;

public class DeleteCourierTest {

    private CourierClient courierClient;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
        courierClient = new CourierClient();
    }

    @Test
    @DisplayName("Успешное удаление курьера")
    @Description("Проверка, что при вводе существующего id курьер удаляется")
    public void deleteCourier() {
        Courier courier = randomCourier();
        courierClient.sendPostRequestV1Courier(courier);
        Response response = courierClient.sendDeleteRequestV1Courier(courier);
        assertEquals("Неверный статус код", SC_OK, response.statusCode());
        response.then().body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Удаление курьера без id")
    @Description("Невозможно удалить курьера без id")
    public void deleteCourierWithoutId() {
        Response response = courierClient.sendDeleteRequestV1CourierWithoutId();
        assertEquals("Неверный статус код", SC_BAD_REQUEST, response.statusCode());
        response.then().body("message", equalTo("Недостаточно данных для удаления курьера"));
    }

    @Test
    @DisplayName("Удаление курьера с несуществующим id")
    @Description("Невозможно удалить курьера с несуществующим id")
    public void deleteCourierWithInvalidId() {
        Response response = courierClient.sendDeleteRequestV1CourierWithInvalidId();
        assertEquals("Неверный статус код", SC_NOT_FOUND, response.statusCode());
        response.then().body("message", equalTo("Курьера с таким id нет"));
    }
}