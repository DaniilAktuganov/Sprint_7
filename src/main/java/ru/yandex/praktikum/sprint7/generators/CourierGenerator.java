package ru.yandex.praktikum.sprint7.generators;

import ru.yandex.praktikum.sprint7.models.Courier;

public class CourierGenerator {

    public static Courier randomCourier() {
        return new Courier()
                .withLogin("Mihail")
                .withPassword("1234")
                .withFirstName("Mihail");
    }

    public static Courier randomCourierWithoutLogin() {
        return new Courier()
                .withPassword("1234")
                .withFirstName("saske");
    }

    public static Courier randomCourierWithoutPassword() {
        return new Courier()
                .withLogin("ninma")
                .withFirstName("saske");
    }
}