package ru.yandex.praktikum.courier;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.yandex.praktikum.models.Courier;
import ru.yandex.praktikum.models.CourierCreds;
import ru.yandex.praktikum.models.DeleteCourier;
import ru.yandex.praktikum.utils.Specification;

import static io.restassured.RestAssured.given;

public class CourierClient {

    private static final String CREATE_ENDPOINT = "api/v1/courier";
    private static final String LOGIN_ENDPOINT = "api/v1/courier/login";
    private static final String DELETE_ENDPOINT = "api/v1/courier/";

    @Step("Send post request to /api/v1/courier")
    public Response create(Courier courier) {
        return given()
                .spec(Specification.requestSpecification())
                .and()
                .body(courier)
                .when()
                .post(CREATE_ENDPOINT);
    }

    @Step("Send post request to /api/v1/courier/login")
    public Response login(CourierCreds creds) {
        return given()
                .spec(Specification.requestSpecification())
                .and()
                .body(creds)
                .when()
                .post(LOGIN_ENDPOINT);
    }

    @Step("Send delete request to /api/v1/courier/:id")
    public Response delete(int Id) {

        DeleteCourier deleteCourier = new DeleteCourier(String.valueOf(Id));

        return given()
                .spec(Specification.requestSpecification())
                .and()
                .body(deleteCourier)
                .when()
                .delete(DELETE_ENDPOINT + Id);
    }
}
