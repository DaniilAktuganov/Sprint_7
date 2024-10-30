package ru.yandex.praktikum.sprint7.client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.yandex.praktikum.sprint7.RequestSpecBuilder;
import ru.yandex.praktikum.sprint7.models.Courier;
import ru.yandex.praktikum.sprint7.models.Order;
import ru.yandex.praktikum.sprint7.models.OrderTrack;

import static io.restassured.RestAssured.given;

public class OrderClient {

    private static final String CREATE_ORDER_URL = "/api/v1/orders";
    private static final String GET_ORDERS_LIST_URL = "/api/v1/orders";
    private static final String GET_ORDER_URL = "/api/v1/orders/track";
    private static final String ACCEPT_ORDER_URL = "/api/v1/orders/accept/";

    @Step("Создание заказа")
    public Response sendPostRequestV1Orders(Order order) {
        return given(RequestSpecBuilder.getRequestSpec())
                .header("Content-type", "application/json")
                .body(order)
                .when()
                .post(CREATE_ORDER_URL);
    }

    @Step("Получение списка заказов")
    public Response sendGetRequestV1Orders() {
        return given(RequestSpecBuilder.getRequestSpec())
                .get(GET_ORDERS_LIST_URL);
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
        return given(RequestSpecBuilder.getRequestSpec())
                .queryParam("t", track)
                .get(GET_ORDER_URL);
    }

    @Step("Получение заказа без номера")
    public Response sendGetRequestV1OrdersTrackWithoutTrack() {
        return given(RequestSpecBuilder.getRequestSpec())
                .get(GET_ORDER_URL);
    }


    @Step("Принятие заказа")
    public Response sendPutRequestV1OrdersAcceptId(Courier courier, Order order) {
        CourierClient courierClient = new CourierClient();
        int courierId = courierClient.getCourierId(courier);
        int orderId = getOrderId(order);
        return given(RequestSpecBuilder.getRequestSpec())
                .queryParam("courierId", courierId)
                .when()
                .put(ACCEPT_ORDER_URL + orderId);
    }

    @Step("Принятие заказа без id курьера")
    public Response sendPutRequestV1OrdersAcceptIdWithoutCourierId(Order order) {
        int orderId = getOrderId(order);
        Response response = given(RequestSpecBuilder.getRequestSpec())
                .when()
                .put(ACCEPT_ORDER_URL + orderId);
        return response;
    }

    @Step("Принятие заказа с неверным id курьера")
    public Response sendPutRequestV1OrdersAcceptIdWithInvalidCourierId(Order order) {
        int orderId = getOrderId(order);
        Response response = given(RequestSpecBuilder.getRequestSpec())
                .queryParam("courierId", (-1))
                .when()
                .put(ACCEPT_ORDER_URL + orderId);
        return response;
    }

    @Step("Принятие заказа без id заказа")
    public Response sendPutRequestV1OrdersAcceptIdWithoutOrderId(Courier courier) {
        CourierClient courierClient = new CourierClient();
        int courierId = courierClient.getCourierId(courier);
        Response response = given(RequestSpecBuilder.getRequestSpec())
                .queryParam("courierId", courierId)
                .when()
                .put(ACCEPT_ORDER_URL);
        return response;
    }

    @Step("Принятие заказа с неверным id заказа")
    public Response sendPutRequestV1OrdersAcceptIdWithInvalidOrderId(Courier courier) {
        CourierClient courierClient = new CourierClient();
        int courierId = courierClient.getCourierId(courier);
        Response response = given(RequestSpecBuilder.getRequestSpec())
                .queryParam("courierId", courierId)
                .when()
                .put(ACCEPT_ORDER_URL + (-1));
        return response;
    }
}