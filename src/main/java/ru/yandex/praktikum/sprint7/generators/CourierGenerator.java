package ru.yandex.praktikum.sprint7.generators;

import ru.yandex.praktikum.sprint7.models.Courier;

public class CourierGenerator {

    public static Courier createDefaultCourier() {
        return new Courier("Mihail", "1234", "Misha");
    }

    public static Courier createDefaultCourierWithoutLogin() {
        return new Courier(null, "1234", "Misha");
    }

    public static Courier createDefaultCourierWithoutPassword() {
        return new Courier("Mihail", null, "Misha");
    }
}