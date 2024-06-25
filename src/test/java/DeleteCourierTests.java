import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.courier.CourierClient;
import ru.yandex.praktikum.models.Courier;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static ru.yandex.praktikum.courier.CourierGenerator.randomCourier;
import static ru.yandex.praktikum.models.CourierCreds.credsFromCourier;
import static ru.yandex.praktikum.utils.Utils.randomInt;

public class DeleteCourierTests {
    private CourierClient courierClient;
    private Courier courier;
    private int courierId;


    @Before
    public void setUp() {
        courier = randomCourier();
        courierClient = new CourierClient();
        courierClient.create(courier);
        Response loginResponse = courierClient.login(credsFromCourier(courier));
        courierId = loginResponse.path("id");
    }

    @Test
    @DisplayName("Delete courier with wrong id")
    @Description("Проверка удаления курьера с несуществующим id")
    public void DeleteCourierWithWrongIdTest(){

        Response response = courierClient.delete(randomInt(200000,100000));

        assertThat("Статус код неверный при удалении курьера",
                response.statusCode(), equalTo(HttpStatus.SC_NOT_FOUND));

        assertThat("Неверное сообщение при успешной авторизации курьера",
                response.path("message"),equalTo("Курьера с таким id нет."));

    }

    @Test
    @DisplayName("Delete courier")
    @Description("Проверка удаления курьера")
    public void DeleteCourierTest(){

        Response response = courierClient.delete(courierId);

        assertThat("Статус код неверный при удалении курьера",
                response.statusCode(), equalTo(HttpStatus.SC_OK));

        assertThat("Неверное сообщение при успешной авторизации курьера",
                response.path("ok"), equalTo(true));

    }


    @After
    public void tearDown() {
        courierClient.delete(courierId);
    }

}
