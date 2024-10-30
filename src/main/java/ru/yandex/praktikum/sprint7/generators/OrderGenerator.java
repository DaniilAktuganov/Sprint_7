package ru.yandex.praktikum.sprint7.generators;

import ru.yandex.praktikum.sprint7.models.Order;

public class OrderGenerator {

    public static Order createDefaultOrder() {
        return new Order()
                .withFirstName("Naruto")
                .withLastName("Uchiha")
                .withAddress("Konoha, 142 apt.")
                .withMetroStation("4")
                .withPhone("+7 800 355 35 35")
                .withRentTime(1)
                .withDeliveryDate("2024-12-12")
                .withComment("Saske, come to Konoha");
    }
}