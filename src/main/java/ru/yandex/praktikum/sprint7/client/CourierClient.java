package ru.yandex.praktikum.sprint7.client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.yandex.praktikum.sprint7.models.Courier;
import ru.yandex.praktikum.sprint7.models.CourierId;

import static io.restassured.RestAssured.given;
import static ru.yandex.praktikum.sprint7.models.CourierCreds.*;

public class CourierClient {

    @Step("Создание курьера")
    public Response sendPostRequestV1Courier(Courier courier) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier");
    }

    @Step("Логин курьера в системе")
    public Response sendPostRequestV1CourierLogin(Courier courier) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(credsFromCourier(courier))
                .when()
                .post("/api/v1/courier/login");
    }
    @Step("Логин курьера в системе с несуществующим логином")
    public Response sendPostRequestV1CourierLoginWithInvalidLogin(Courier courier) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(credsFromCourierWithInvalidLogin(courier))
                .when()
                .post("/api/v1/courier/login");
    }

    @Step("Логин курьера в системе с несуществующим паролем")
    public Response sendPostRequestV1CourierLoginWithInvalidPassword(Courier courier) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(credsFromCourierWithInvalidPassword(courier))
                .when()
                .post("/api/v1/courier/login");
    }

    @Step("Получение id курьера")
    public int getCourierId(Courier courier) {
        Response loginResponse = sendPostRequestV1CourierLogin(courier);
        return loginResponse.as(CourierId.class).getId();
    }

    @Step("Удаление курьера")
    public Response sendDeleteRequestV1Courier(Courier courier) {
        int id = getCourierId(courier);
        return given()
                .header("Content-type", "application/json")
                .and()
                .when()
                .delete("/api/v1/courier/" + id);
    }

    @Step("Удаление курьера без id")
    public Response sendDeleteRequestV1CourierWithoutId() {
        return given().
                delete("/api/v1/courier/");
    }

    @Step("Удаление курьера с несуществующим id")
    public Response sendDeleteRequestV1CourierWithInvalidId() {
        return given().
                delete("/api/v1/courier/" + "invalidId");
    }
}