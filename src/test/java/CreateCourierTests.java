import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.courier.CourierClient;
import ru.yandex.praktikum.models.Courier;

import static org.junit.Assert.assertEquals;
import static ru.yandex.praktikum.courier.CourierGenerator.*;
import static ru.yandex.praktikum.models.CourierCreds.credsFromCourier;
import static ru.yandex.praktikum.utils.Utils.randomString;


public class CreateCourierTests {
    private CourierClient courierClient;
    private Courier courier;
    private int courierId;

    @Before
    public void setUp() {

        courierClient = new CourierClient();
    }

    @Test
    @DisplayName("Create courier")
    @Description("Проверка возможности создания курьера")
    public void CourierCreateTest(){
        courier = randomCourier();

        Response response = courierClient.create(courier);

        assertEquals("Статус код неверный при создании курьера",
                HttpStatus.SC_CREATED, response.statusCode());

        assertEquals("Неверное сообщение при успешном создании курьера",
                true,response.path("ok"));

    }

    @Test
    @DisplayName("Create two same courier")
    @Description("Проверка возмоожности создания двух одинаковых курьеров")
    public void TwoSameCourierCreateTest(){
        courier = randomCourier();

        Response responseFirst = courierClient.create(courier);

        assertEquals("Статус код неверный при создании курьера",
                HttpStatus.SC_CREATED, responseFirst.statusCode());

        Response responseSecond = courierClient.create(courier);

        assertEquals("Статус код неверный при создании курьера c уже существующим логином",
                HttpStatus.SC_CONFLICT, responseSecond.statusCode());

        assertEquals("Некорректное сообщение об ошибке при создании курьера с уже использованным логином ",
                "Этот логин уже используется. Попробуйте другой.",responseSecond.path("message"));

    }

    @Test
    @DisplayName("Create courier negative data")
    @Description("Создание курьера без необходимых данных в запросе")
    public void CreateCourierWithoutLoginTest(){

        courier = randomCourierWithoutLogin();

        Response response = courierClient.create(courier);

        assertEquals("Статус код неверный при создании курьера без необходимых данных",
                HttpStatus.SC_BAD_REQUEST, response.statusCode());


        assertEquals("Некорректное сообщение об ошибке при создании необходимых данных",
                "Недостаточно данных для создания учетной записи",response.path("message"));


    }

    @Test
    @DisplayName("Create courier negative data")
    @Description("Создание курьера без необходимых данных в запросе")
    public void CreateCourierWithoutPasswordTest1(){

        courier = randomCourierWithoutPassword();

        Response response = courierClient.create(courier);

        assertEquals("Статус код неверный при создании курьера без необходимых данных",
                HttpStatus.SC_BAD_REQUEST, response.statusCode());


        assertEquals("Некорректное сообщение об ошибке при создании необходимых данных",
                "Недостаточно данных для создания учетной записи",response.path("message"));


    }

    @After
    public void tearDown() {
        Response loginResponse = courierClient.login(credsFromCourier(courier));
        if(loginResponse.statusCode() == HttpStatus.SC_OK){
        courierId = loginResponse.path("id");
        courierClient.delete(courierId);
        }
    }



}
