import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.courier.CourierClient;
import ru.yandex.praktikum.models.Courier;
import ru.yandex.praktikum.models.Orders;
import ru.yandex.praktikum.order.OrderClient;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static ru.yandex.praktikum.courier.CourierGenerator.randomCourier;
import static ru.yandex.praktikum.models.CourierCreds.credsFromCourier;
import static ru.yandex.praktikum.order.OrderGenerator.randomOrder;
import static ru.yandex.praktikum.utils.Utils.randomInt;

public class AcceptOrderTests {
    private CourierClient courierClient;
    private Courier courier;

    private int courierId;
    private OrderClient orderClient;
    private Orders orders;
    private int trackId;
    private int orderId;

    @Before
    public void setup(){

        courier = randomCourier();
        courierClient = new CourierClient();
        courierClient.create(courier);

        Response loginResponse = courierClient.login(credsFromCourier(courier));
        courierId = loginResponse.path("id");

        orderClient = new OrderClient();
        orders = randomOrder();
        trackId = orderClient.create(orders).path("track");

        Response orderResponse = orderClient.getTrack(String.valueOf(trackId));
        orderId = orderResponse.path("order.id");
    }
    @Test
    @DisplayName("Accept order")
    @Description("Проверка принятия заказа")
    public void getListOrdersTest(){

        Response response = orderClient.acceptOrder(String.valueOf(orderId),String.valueOf(courierId));

        assertThat("Статус код неверный при принятии заказа",
                response.statusCode(), equalTo(HttpStatus.SC_OK));

        assertThat("Неверное сообщение при принятии заказа",
                response.path("ok"),equalTo(true));

    }

    @Test
    @DisplayName("Accept order without courierId")
    @Description("Проверка принятия заказа без id курьера")
    public void getListOrdersWithoutCourierIdTest(){

        Response response = orderClient.acceptOrder(String.valueOf(orderId),"");

        assertThat("Статус код неверный при принятии заказа",
                response.statusCode(), equalTo(HttpStatus.SC_BAD_REQUEST));

        assertThat("Неверное сообщение при принятии заказа",
                response.path("message"), equalTo("Недостаточно данных для поиска"));

    }

    @Test
    @DisplayName("Accept order with invalid courierId")
    @Description("Проверка принятия заказа c несуществующим id курьера")
    public void getListOrdersWithInvalidCourierIdTest(){

        Response response = orderClient.acceptOrder(String.valueOf(orderId),String.valueOf(randomInt(2000,1000)));

        assertThat("Статус код неверный при принятии заказа",
                response.statusCode(), equalTo(HttpStatus.SC_NOT_FOUND));

        assertThat("Неверное сообщение при принятии заказа",
                response.path("message"), equalTo("Курьера с таким id не существует"));

    }

    @Test
    @DisplayName("Accept order with invalid orderId")
    @Description("Проверка принятия заказа c несуществующим id заказа")
    public void getListOrdersWithInvalidOrderIdTest(){

        Response response = orderClient.acceptOrder(String.valueOf(-1),String.valueOf(courierId));

        assertThat("Статус код неверный при принятии заказа",
                response.statusCode(), equalTo(HttpStatus.SC_NOT_FOUND));

        assertThat("Неверное сообщение при принятии заказа",
                response.path("message"),equalTo("Заказа с таким id не существует"));

    }


    @After
    public void tearDown() {

        orderClient.delete(trackId);
        courierClient.delete(courierId);
    }

}
