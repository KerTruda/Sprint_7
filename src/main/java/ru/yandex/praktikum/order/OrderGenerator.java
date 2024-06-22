package ru.yandex.praktikum.order;

import ru.yandex.praktikum.models.Orders;

import java.util.List;

import static ru.yandex.praktikum.utils.Utils.*;

public class OrderGenerator {
    public static Orders randomOrder() {

        Orders orders = new Orders();
        orders.setFirstName(randomString(10));
        orders.setLastName(randomString(10));
        orders.setAddress(randomString(10));
        orders.setMetroStation(randomString(10));
        orders.setPhone(randomPhoneNumber());
        orders.setRentTime(randomInt(100,10));
        orders.setDeliveryDate(randomDate(2022, 2023));
        orders.setComment(randomString(10));
        orders.setColor(List.of(randomString(10)));

        return orders;
    }
}
