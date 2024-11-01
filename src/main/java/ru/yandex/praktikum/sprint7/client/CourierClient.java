package ru.yandex.praktikum.sprint7.client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.yandex.praktikum.sprint7.RequestSpecBuilder;
import ru.yandex.praktikum.sprint7.models.Courier;
import ru.yandex.praktikum.sprint7.models.CourierId;

import static io.restassured.RestAssured.given;
import static ru.yandex.praktikum.sprint7.models.CourierCreds.*;

public class CourierClient {

    private static final String CREATE_COURIER_URL = "/api/v1/courier";
    private static final String LOGIN_COURIER_URL = "/api/v1/courier/login";
    private static final String DELETE_COURIER_URL = "/api/v1/courier/";

    @Step("Создание курьера")
    public Response sendPostRequestV1Courier(Courier courier) {
        return given(RequestSpecBuilder.getRequestSpec())
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post(CREATE_COURIER_URL);
    }

    @Step("Логин курьера в системе")
    public Response sendPostRequestV1CourierLogin(Courier courier) {
        return given(RequestSpecBuilder.getRequestSpec())
                .header("Content-type", "application/json")
                .body(credsFromCourier(courier))
                .when()
                .post(LOGIN_COURIER_URL);
    }
    @Step("Логин курьера в системе с несуществующим логином")
    public Response sendPostRequestV1CourierLoginWithInvalidLogin(Courier courier) {
        return given(RequestSpecBuilder.getRequestSpec())
                .header("Content-type", "application/json")
                .body(credsFromCourierWithInvalidLogin(courier))
                .when()
                .post(LOGIN_COURIER_URL);
    }

    @Step("Логин курьера в системе с несуществующим паролем")
    public Response sendPostRequestV1CourierLoginWithInvalidPassword(Courier courier) {
        return given(RequestSpecBuilder.getRequestSpec())
                .header("Content-type", "application/json")
                .body(credsFromCourierWithInvalidPassword(courier))
                .when()
                .post(LOGIN_COURIER_URL);
    }

    @Step("Получение id курьера")
    public int getCourierId(Courier courier) {
        Response loginResponse = sendPostRequestV1CourierLogin(courier);
        return loginResponse.as(CourierId.class).getId();
    }

    @Step("Удаление курьера")
    public Response sendDeleteRequestV1Courier(Courier courier) {
        int id = getCourierId(courier);
        return given(RequestSpecBuilder.getRequestSpec())
                .delete(DELETE_COURIER_URL + id);
    }

    @Step("Удаление курьера без id")
    public Response sendDeleteRequestV1CourierWithoutId() {
        return given(RequestSpecBuilder.getRequestSpec())
            .delete(DELETE_COURIER_URL);
    }

    @Step("Удаление курьера с несуществующим id")
    public Response sendDeleteRequestV1CourierWithInvalidId() {
        return given(RequestSpecBuilder.getRequestSpec())
            .delete(DELETE_COURIER_URL + (-1));
    }
}