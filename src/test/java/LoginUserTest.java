import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import users.User;
import dataForTesting.GenerationData;
import users.UserActions;
import dataForTesting.Url;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class LoginUserTest {

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
    @DisplayName("Авторизация с корректными данными")
    public void logInGetSuccess() {
        Response responseCreating = UserActions.createUser(user);
        //accessToken нужен для последующего удаления юзера
        accessToken = responseCreating.then().extract().path("accessToken").toString();
        Response responseLogin = UserActions.logInUser(user);
        responseLogin.then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("success", equalTo(true))
                .and()
                .body("accessToken", notNullValue())
                .and()
                .body("refreshToken", notNullValue())
                .and()
                .body("user", notNullValue());
    }

    @Test
    @DisplayName("Вход с некорректным паролем")
    public void logInWithIncorrectPasswordGetError() {
        UserActions.createUser(user);
        User incorrectUser = new User(user.getEmail(), RandomStringUtils.randomAlphabetic(10), user.getName());
        UserActions.logInUser(incorrectUser)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Вход с некорректной почтой")
    public void logInWithIncorrectEmailGetError() {
        UserActions.createUser(user);
        User incorrectUser = new User(RandomStringUtils.randomAlphabetic(10) + "@gmail.com", user.getPassword(), user.getName());
        UserActions.logInUser(incorrectUser)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("email or password are incorrect"));
    }
}