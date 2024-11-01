package ru.yandex.praktikum.sprint7.models;

public class CourierCreds {

    private String login;
    private String password;

    public CourierCreds(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public static CourierCreds credsFromCourier(Courier courier) {
        return new CourierCreds(courier.getLogin(), courier.getPassword());
    }

    public static CourierCreds credsFromCourierWithInvalidLogin(Courier courier) {
        return new CourierCreds("invalidLogin", courier.getPassword());
    }

    public static CourierCreds credsFromCourierWithInvalidPassword(Courier courier) {
        return new CourierCreds(courier.getLogin(), "invalidPassword");
    }
}