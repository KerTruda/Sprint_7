package ru.yandex.praktikum.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CourierCreds {

    private String login;
    private String password;


    public static CourierCreds credsFromCourier(Courier courier) {
        return new CourierCreds(courier.getLogin(), courier.getPassword());
    }

}
