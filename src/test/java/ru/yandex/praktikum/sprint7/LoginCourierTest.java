package ru.yandex.praktikum.sprint7;

import io.qameta.allure.Description;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.sprint7.models.Courier;
import ru.yandex.praktikum.sprint7.client.CourierClient;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static ru.yandex.praktikum.sprint7.generators.CourierGenerator.*;

public class LoginCourierTest {

    private CourierClient courierClient;
    private Courier courier;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
    }

    @Test
    @DisplayName("Успешный логин курьера в системе")
    @Description("Проверка, что курьер может войти в систему с корректными данными")
    public void loginCourierTest() {
        courier = createDefaultCourier();
        courierClient.sendPostRequestV1Courier(courier);
        Response loginResponse = courierClient.sendPostRequestV1CourierLogin(courier);
        assertEquals("Неверный статус код", SC_OK, loginResponse.statusCode());
        loginResponse.then().assertThat().body("id", notNullValue());
    }

    @Test
    @DisplayName("Запрос без логина")
    @Description("Невозможно войти в систему без логина")
    public void loginCourierWithoutLoginTest() {
        courier = createDefaultCourierWithoutLogin();
        courierClient.sendPostRequestV1Courier(courier);
        Response loginResponse = courierClient.sendPostRequestV1CourierLogin(courier);
        assertEquals("Неверный статус код", SC_BAD_REQUEST, loginResponse.statusCode());
        loginResponse.then().body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Запрос без пароля")
    @Description("Невозможно войти в систему без пароля")
    public void loginCourierWithoutPasswordTest() {
        courier = createDefaultCourierWithoutPassword();
        courierClient.sendPostRequestV1Courier(courier);
        Response loginResponse = courierClient.sendPostRequestV1CourierLogin(courier);
        assertEquals("Неверный статус код", SC_BAD_REQUEST, loginResponse.statusCode());
        loginResponse.then().body("message", equalTo("Недостаточно данных для входа"));

    }

    @Test
    @DisplayName("Запрос с несуществующим логином")
    public void loginWithInvalidLoginTest() {
        courier = createDefaultCourier();
        courierClient.sendPostRequestV1Courier(courier);
        Response loginResponse = courierClient.sendPostRequestV1CourierLoginWithInvalidLogin(courier);
        assertEquals("Неверный статус код", SC_NOT_FOUND, loginResponse.statusCode());
        loginResponse.then().body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Запрос с несуществующим паролем")
    public void loginWithInvalidPasswordTest() {
        courier = createDefaultCourier();
        courierClient.sendPostRequestV1Courier(courier);
        Response loginResponse = courierClient.sendPostRequestV1CourierLoginWithInvalidPassword(courier);
        assertEquals("Неверный статус код", SC_NOT_FOUND, loginResponse.statusCode());
        loginResponse.then().body("message", equalTo("Учетная запись не найдена"));
    }

    @After
    public void tearDown() {
        courierClient.sendDeleteRequestV1Courier(courier);
    }
}