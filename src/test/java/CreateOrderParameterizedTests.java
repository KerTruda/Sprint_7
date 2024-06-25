import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.praktikum.models.Orders;
import ru.yandex.praktikum.order.OrderClient;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static ru.yandex.praktikum.order.OrderGenerator.randomOrder;

@RunWith(Parameterized.class)
public class CreateOrderParameterizedTests {

    private OrderClient orderClient;
    private Orders orders;
    private List<String> colour;
    private int trackId;

    @Before
    public void setup(){
        orderClient = new OrderClient();
    }

    public CreateOrderParameterizedTests(List<String> colour) {
        this.colour = colour;
    }

    @Parameterized.Parameters
    public static Object[][] data() {
        return new Object[][] {
                {List.of("BLACK")},
                {List.of("GREY")},
                {List.of("GREY","BLACK")},
                {List.of()},
        };
    }

    @Test
    @DisplayName("Create order")
    @Description("Проверка возможности создания заказа c выбором цвета")
    public void CreateOrderTest(){

        orders = randomOrder();

        orders.setColor(colour);

        Response response = orderClient.create(orders);

        trackId = response.path("track");

        assertThat("Статус код неверный при создании заказа",
                response.statusCode(), equalTo(HttpStatus.SC_CREATED));

        assertThat("Неверное сообщение при создании заказа",
                response.path("track"),instanceOf(Integer.class));

    }

    @After
    public void tearDown() {

        orderClient.delete(trackId);

    }

}
