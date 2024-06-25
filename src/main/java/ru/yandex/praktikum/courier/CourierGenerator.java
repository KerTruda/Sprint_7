package ru.yandex.praktikum.courier;

import ru.yandex.praktikum.models.Courier;

import static ru.yandex.praktikum.utils.Utils.randomString;

public class CourierGenerator {

    public static Courier randomCourier() {
        return new Courier(randomString(10), randomString(12), randomString(20));
    }

    public static Courier randomCourierWithoutLogin() {
        return new Courier("", randomString(12), randomString(20));
    }

    public static Courier randomCourierWithoutPassword() {
        return new Courier(randomString(10), "", randomString(20));
    }
}
