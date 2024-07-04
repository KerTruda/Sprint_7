package ru.yandex.praktikum.order;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.yandex.praktikum.models.OrderCreds;
import ru.yandex.praktikum.models.Orders;
import ru.yandex.praktikum.utils.Specification;

import static io.restassured.RestAssured.given;

public class OrderClient {
    private static final String CREATE_ORDER_ENDPOINT = "api/v1/orders";
    private static final String TRACK_ORDER_ENDPOINT = "/api/v1/orders/track";
    private static final String CANCEL_ORDER_ENDPOINT = "api/v1/orders/cancel";
    private static final String ACCEPT_ORDER_ENDPOINT = "api/v1/orders/accept/";

    @Step("Send post request to /api/v1/orders")
    public Response create(Orders order) {

        return given()
                .spec(Specification.requestSpecification())
                .and()
                .body(order)
                .when()
                .post(CREATE_ORDER_ENDPOINT);
    }

    @Step("Send put request to /api/v1/orders/cancel")
    public Response delete(int trackId) {

        OrderCreds orderCreds = new OrderCreds(trackId);

        return given()
                .spec(Specification.requestSpecification())
                .and()
                .body(orderCreds)
                .when()
                .put(CANCEL_ORDER_ENDPOINT);
    }

    @Step("Send get request to /api/v1/orders")
    public Response get(){
        return given()
                .spec(Specification.requestSpecification())
                .get(CREATE_ORDER_ENDPOINT);
    }

    @Step("Send get request to /api/v1/orders")
    public Response getTrack(String courierId){
        return given()
                .spec(Specification.requestSpecification())
                .queryParam("t", courierId)
                .when()
                .get(TRACK_ORDER_ENDPOINT);
    }

    @Step("Send put request to api/v1/orders/accept/")
    public Response acceptOrder(String orderId, String courierId) {

        return given()
                .spec(Specification.requestSpecification())
                .queryParam("courierId", courierId)
                .when()
                .put(ACCEPT_ORDER_ENDPOINT + Integer.parseInt(orderId));
    }

}
