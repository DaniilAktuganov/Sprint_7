package ru.yandex.praktikum.sprint7;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

public class RequestSpecBuilder {
    private static final String BASE_URI = "https://qa-scooter.praktikum-services.ru/";

    public static RequestSpecification getRequestSpec() {
        return RestAssured.given()
                .baseUri(BASE_URI);
    }
}
