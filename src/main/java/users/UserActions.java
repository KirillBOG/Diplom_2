package users;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import dataForTesting.ApiHandles;

import static io.restassured.RestAssured.given;

public class UserActions {

    @Step("Создание пользователя")
    public static Response createUser(User user) {
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .post(ApiHandles.REGISTER_PATH);
        return response;
    }

    @Step("Авторизация")
    public static Response logInUser(User user) {
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .post(ApiHandles.LOGIN_PATH);
        return response;
    }

    @Step("Удаление юзера")
    public static void deleteUser(String accessToken) {
        if (accessToken != null)
            given()
                    .header("Authorization", accessToken)
                    .when()
                    .delete(ApiHandles.USER_PATH);
    }

    @Step("Редактирование пользователя, авторизированного")
    public static Response editAuthorizedUser(String accessToken, UserEdited userEditedData) {
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .header("Authorization", accessToken)
                .and()
                .body(userEditedData)
                .when()
                .patch(ApiHandles.USER_PATH);
        return response;
    }

    @Step("Редактирование пользователя, неавториз.")
    public static Response editUnauthorizedUser(String accessToken, UserEdited userEditedData) {
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(userEditedData)
                .when()
                .patch(ApiHandles.USER_PATH);
        return response;
    }
}