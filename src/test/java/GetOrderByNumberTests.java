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
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static ru.yandex.praktikum.courier.CourierGenerator.randomCourier;
import static ru.yandex.praktikum.models.CourierCreds.credsFromCourier;
import static ru.yandex.praktikum.order.OrderGenerator.randomOrder;
import static ru.yandex.praktikum.utils.Utils.randomInt;

public class GetOrderByNumberTests {
    private CourierClient courierClient;
    private Courier courier;
    private OrderClient orderClient;
    private Orders orders;
    private int trackId;
    private int courierId;

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

    }

    @Test
    @DisplayName("Get order by Id")
    @Description("Проверка получения заказа по его номеру")
    public void getOrderByNumTest(){

        Response response = orderClient.getTrack(String.valueOf(trackId));


        assertThat("Статус код неверный при получении заказа по его номеру",
                response.statusCode(), equalTo(HttpStatus.SC_OK));

        assertThat("Неверное сообщение при получении заказа по его номеру",
                response.path("order"), notNullValue());

    }

    @Test
    @DisplayName("Get order by invalid Id")
    @Description("Проверка получения заказа по несуществующему номеру")
    public void getOrderByInvalidNumTest(){

        Response response = orderClient.getTrack(String.valueOf(randomInt(200000,500)));


        assertThat("Статус код неверный при запросе с несуществующим номером заказа",
                response.statusCode(), equalTo(HttpStatus.SC_NOT_FOUND));

        assertThat("Неверное сообщение при запросе с несуществующим номером заказа",
                response.path("message"), equalTo("Заказ не найден"));

    }

    @Test
    @DisplayName("Get order Without Id")
    @Description("Проверка получения заказа без номера")
    public void getOrderWithoutNumTest(){

        Response response = orderClient.getTrack("");


        assertThat("Статус код неверный при запросе заказа без номера",
                response.statusCode(), equalTo(HttpStatus.SC_BAD_REQUEST));

        assertThat("Неверное сообщение при запросе заказа без номера",
                response.path("message"), equalTo("Недостаточно данных для поиска"));

    }

    @After
    public void tearDown() {

        orderClient.delete(trackId);
        courierClient.delete(courierId);
    }

}
