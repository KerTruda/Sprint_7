import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.models.Orders;
import ru.yandex.praktikum.order.OrderClient;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static ru.yandex.praktikum.order.OrderGenerator.randomOrder;

public class ListOrderTest {
    private OrderClient orderClient;
    private Orders orders;
    private int trackId;

    @Before
    public void setup(){
        orderClient = new OrderClient();
        orders = randomOrder();
        trackId = orderClient.create(orders).path("track");
    }

    @Test
    @DisplayName("Get list order")
    @Description("Проверка получения списка заказов")
    public void getListOrdersTest(){

        Response response = orderClient.get();

        assertThat("Статус код неверный при получении списка заказов",
                response.statusCode(), equalTo(HttpStatus.SC_OK));

        assertThat("Список заказов пустой",
                response.path("orders"), notNullValue());

    }

    @After
    public void tearDown() {
        orderClient.delete(trackId);
    }
}
