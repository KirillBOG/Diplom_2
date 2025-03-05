import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import users.User;
import users.UserEdited;
import dataForTesting.GenerationData;
import users.UserActions;
import dataForTesting.Url;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class EditUserTest {

    User user;
    UserEdited userEditedData;
    String accessToken;

    @Before
    public void setUp() {
        RestAssured.baseURI = Url.Url;
        user = GenerationData.generateUser();
        userEditedData = GenerationData.generateUserEditedData();
    }

    @After
    public void tearDown() {
        UserActions.deleteUser(accessToken);
    }

    @Test
    @DisplayName("Редактирование почты, авторизованного юзера")
    public void editEmailAuthorizedUserGetSuccess() {

        Response responseCreating = UserActions.createUser(user);
        accessToken = responseCreating.then().extract().path("accessToken").toString();
        UserActions.editAuthorizedUser(accessToken, userEditedData)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("success", equalTo(true))
                .and()
                .body("user", notNullValue());
    }

    @Test
    @DisplayName("Редактирование почты, неавторизованного юзера")
    public void editEmailUnauthorizedUserGetError() {

        Response responseCreating = UserActions.createUser(user);
        accessToken = responseCreating.then().extract().path("accessToken").toString();
        UserActions.editUnauthorizedUser(accessToken, userEditedData)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("You should be authorised"));
    }
}