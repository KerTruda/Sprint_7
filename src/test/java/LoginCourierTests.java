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
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static ru.yandex.praktikum.courier.CourierGenerator.randomCourier;
import static ru.yandex.praktikum.models.CourierCreds.credsFromCourier;
import static ru.yandex.praktikum.utils.Utils.randomString;

public class LoginCourierTests {

    private CourierClient courierClient;
    private int courierId;
    private Courier courier;

    @Before
    public void setUp() {
        courier = randomCourier();
        courierClient = new CourierClient();
        courierClient.create(courier);

    }

    @Test
    @DisplayName("Login courier")
    @Description("Проверка авторизации курьера")
    public void loginCourierTest(){

        Response response = courierClient.login(credsFromCourier(courier));

        assertThat("Статус код неверный при авторизации курьера",
                response.statusCode(), equalTo(HttpStatus.SC_OK));

        assertThat("Неверное сообщение при успешной авторизации курьера",
                response.path("id"), instanceOf(Integer.class));

    }

    @Test
    @DisplayName("Login courier with wrong Login")
    @Description("Проверка авторизации курьера с неправильным логином")
    public void loginCourierWithWrongLoginTest(){

        Courier courierWithWrongLogin = new Courier(randomString(5), courier.getPassword(), courier.getFirstName());

        Response response = courierClient.login(credsFromCourier(courierWithWrongLogin));

        assertThat("Статус код неверный при авторизации курьера",
                response.statusCode(), equalTo(HttpStatus.SC_NOT_FOUND));

        assertThat("Неверное сообщение при успешной авторизации курьера",
                response.path("message"), equalTo("Учетная запись не найдена"));

    }

    @Test
    @DisplayName("Login courier with wrong Password")
    @Description("Проверка авторизации курьера с неправильным паролем")
    public void loginCourierWithWrongPasswordTest(){

        Courier courierWithWrongLogin = new Courier(courier.getLogin(), randomString(10), courier.getFirstName());

        Response response = courierClient.login(credsFromCourier(courierWithWrongLogin));

        assertThat("Статус код неверный при авторизации курьера",
                response.statusCode(), equalTo(HttpStatus.SC_NOT_FOUND));

        assertThat("Неверное сообщение при успешной авторизации курьера",
                response.path("message"), equalTo("Учетная запись не найдена"));

    }

    @Test
    @DisplayName("Login courier without Login")
    @Description("Проверка авторизации курьера без логина")
    public void loginCourierWithoutLoginTest(){

        Courier courierWithWrongLogin = new Courier("",courier.getPassword() , courier.getFirstName());

        Response response = courierClient.login(credsFromCourier(courierWithWrongLogin));

        assertThat("Статус код неверный при авторизации курьера",
                response.statusCode(), equalTo(HttpStatus.SC_BAD_REQUEST));

        assertThat("Неверное сообщение при успешной авторизации курьера",
                response.path("message"), equalTo("Недостаточно данных для входа"));

    }

    @Test
    @DisplayName("Login courier without Password")
    @Description("Проверка авторизации курьера без пароля")
    public void loginCourierWithoutPasswordTest(){

        Courier courierWithWrongLogin = new Courier(courier.getLogin(),"" , courier.getFirstName());

        Response response = courierClient.login(credsFromCourier(courierWithWrongLogin));

        assertThat("Статус код неверный при авторизации курьера",
                response.statusCode(), equalTo(HttpStatus.SC_BAD_REQUEST));

        assertThat("Неверное сообщение при успешной авторизации курьера",
                response.path("message"), equalTo("Недостаточно данных для входа"));

    }

    @Test
    @DisplayName("Account not found")
    @Description("Проверка авторизации под несуществующим курьером")
    public void loginCourierWithoutPasswordTest1(){

        Courier courierWithWrongLogin = new Courier(randomString(10),randomString(12), randomString(10));

        Response response = courierClient.login(credsFromCourier(courierWithWrongLogin));

        assertThat("Статус код неверный при авторизации курьера",
                response.statusCode(), equalTo(HttpStatus.SC_NOT_FOUND));

        assertThat("Неверное сообщение при успешной авторизации курьера",
                response.path("message"), equalTo("Учетная запись не найдена"));

    }

    @After
    public void tearDown() {
        Response loginResponse = courierClient.login(credsFromCourier(courier));
        courierId = loginResponse.path("id");
        courierClient.delete(courierId);
    }
}
