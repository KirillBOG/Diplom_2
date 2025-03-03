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

import static org.hamcrest.CoreMatchers.*;

public class CreateUserTest {

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
    @DisplayName("Создание юзера с корректными данными")
    public void createNewUserGetSuccess() {
        Response response = UserActions.createUser(user);
        accessToken = response.then().extract().path("accessToken").toString();
        response.then().assertThat().statusCode(HttpStatus.SC_OK)
                .and()
                .body("success", equalTo(true))
                .and()
                .body("accessToken", notNullValue())
                .and()
                .body("refreshToken", notNullValue());
    }

    @Test
    @DisplayName("Создание двух одинаковых юзеров")
    public void createTwoSimilarUsersGetError() {
        Response response = UserActions.createUser(user);
        accessToken = response.then().extract().path("accessToken").toString();
        UserActions.createUser(user)
                .then().assertThat().statusCode(HttpStatus.SC_FORBIDDEN)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Создание юзера без почты")
    public void createUserWithoutEmailGetError() {

        String password = RandomStringUtils.randomAlphabetic(8);
        String name = RandomStringUtils.randomAlphabetic(8);
        user = new User(password, name);

        UserActions.createUser(user)
                .then().assertThat().statusCode(HttpStatus.SC_FORBIDDEN)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("Email, password and name are required fields"));

    }

    @Test
    @DisplayName("Создание юзера без пароля")
    public void createUserWithoutPasswordGetError() {

        String email = RandomStringUtils.randomAlphabetic(8) + "@gmail.com";
        String name = RandomStringUtils.randomAlphabetic(8);
        user = new User(email, name);

        UserActions.createUser(user)
                .then().assertThat().statusCode(HttpStatus.SC_FORBIDDEN)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("Email, password and name are required fields"));

    }

    @Test
    @DisplayName("Создание пользователя без имени")
    public void createUserWithoutNameGetError() {

        String email = RandomStringUtils.randomAlphabetic(8) + "@gmail.com";
        String password = RandomStringUtils.randomAlphabetic(8);

        user = new User(email, password);
        UserActions.createUser(user)
                .then().assertThat().statusCode(HttpStatus.SC_FORBIDDEN)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("Email, password and name are required fields"));

    }
}