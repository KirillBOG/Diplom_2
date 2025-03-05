import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import orders.OrderOperations;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import users.User;
import users.UserActions;
import dataForTesting.Url;
import dataForTesting.GenerationData;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;

public class GetUserOrdersTest {

    User user;
    String accessToken;

    @Before
    public void setUp() {
        RestAssured.baseURI = Url.Url;
        user = GenerationData.generateUser();
    }

    @After
    public void tearDown() {
        UserActions.deleteUser(accessToken);
    }

    @Test
    @DisplayName("Получение заказов авторизованного пользователя")
    public void getOrdersAuthorizedUserGetSuccess() {
        Response response = UserActions.createUser(user);
        //accessToken нужен для создания заказа и последующего удаления юзера
        accessToken = response.then().extract().path("accessToken").toString();
        OrderOperations.getOrdersAuthorizedUser(accessToken)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .assertThat()
                .body("success", equalTo(true))
                .and()
                .body("orders", notNullValue());
    }

    @Test
    @DisplayName("Получение заказов неавторизованного пользователя")
    public void getOrdersUnauthorizedUserGetError() {
        Response response = UserActions.createUser(user);
        //accessToken нужен для последующего удаления юзера
        accessToken = response.then().extract().path("accessToken").toString();
        OrderOperations.getOrdersUnauthorizedUser()
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .assertThat()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("You should be authorised"));
    }
}