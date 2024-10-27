package ru.yandex.praktikum.sprint7.client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.yandex.praktikum.sprint7.models.Courier;
import ru.yandex.praktikum.sprint7.models.CourierId;
import ru.yandex.praktikum.sprint7.models.Order;
import ru.yandex.praktikum.sprint7.models.OrderTrack;

import static io.restassured.RestAssured.given;
import static ru.yandex.praktikum.sprint7.generators.OrderGenerator.randomOrder;

public class OrderClient {

    @Step("Создание заказа")
    public Response sendPostRequestV1Orders(Order order) {
        return given()
                .header("Content-type", "application/json")
                .body(order)
                .when()
                .post("/api/v1/orders");
    }

    @Step("Получение списка заказов")
    public Response sendGetRequestV1Orders() {
        return given()
                .get("/api/v1/orders");
    }

    @Step("Получение номера заказа")
    public int getOrderTrack(Order order) {
        Response orderTrack = sendPostRequestV1Orders(order);
        return orderTrack.as(OrderTrack.class).getTrack();
    }

    @Step("Получение id заказа")
    public int getOrderId(Order order) {
        int track = getOrderTrack(order);
        Response orderResponse = sendGetRequestV1OrdersTrack(track);
        return orderResponse.jsonPath().getInt("order.id");
    }

    @Step("Получение заказа по его номеру")
    public Response sendGetRequestV1OrdersTrack(int track) {
        return given()
                .queryParam("t", track)
                .get("/api/v1/orders/track");
    }

    @Step("Получение заказа без номера")
    public Response sendGetRequestV1OrdersTrackWithoutTrack() {
        return given()
                .get("/api/v1/orders/track");
    }


    @Step("Принятие заказа")
    public Response sendPutRequestV1OrdersAcceptId(Courier courier, Order order) {
        CourierClient courierClient = new CourierClient();
        int courierId = courierClient.getCourierId(courier);
        int orderId = getOrderId(order);
        return given()
                .queryParam("courierId", courierId)
                .when()
                .put("/api/v1/orders/accept/" + orderId);
    }

    @Step("Принятие заказа без id курьера")
    public Response sendPutRequestV1OrdersAcceptIdWithoutCourierId(Order order) {
        int orderId = getOrderId(order);
        Response response = given()
                .when()
                .put("/api/v1/orders/accept/" + orderId);
        return response;
    }

    @Step("Принятие заказа с неверным id курьера")
    public Response sendPutRequestV1OrdersAcceptIdWithInvalidCourierId(Order order) {
        int orderId = getOrderId(order);
        Response response = given()
                .queryParam("courierId", "invalidId")
                .when()
                .put("/api/v1/orders/accept/" + orderId);
        return response;
    }

    @Step("Принятие заказа без id заказа")
    public Response sendPutRequestV1OrdersAcceptIdWithoutOrderId(Courier courier) {
        CourierClient courierClient = new CourierClient();
        int courierId = courierClient.getCourierId(courier);
        Response response = given()
                .queryParam("courierId", courierId)
                .when()
                .put("/api/v1/orders/accept");
        return response;
    }

    @Step("Принятие заказа с неверным id заказа")
    public Response sendPutRequestV1OrdersAcceptIdWithInvalidOrderId(Courier courier) {
        CourierClient courierClient = new CourierClient();
        int courierId = courierClient.getCourierId(courier);
        Response response = given()
                .queryParam("courierId", courierId)
                .when()
                .put("/api/v1/orders/accept/" + "invalidId");
        return response;
    }
}