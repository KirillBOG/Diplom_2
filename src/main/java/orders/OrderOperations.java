package orders;

import dataForTesting.ApiHandles;
import io.qameta.allure.Step;
import io.restassured.response.Response;


import java.util.List;

import static io.restassured.RestAssured.given;

public class OrderOperations {

    @Step
    public static Response getOrdersAuthorizedUser(String accessToken) {
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .header("Authorization", accessToken)
                .when()
                .get(ApiHandles.ORDERS_PATH);
        return response;
    }

    @Step
    public static Response getOrdersUnauthorizedUser() {
        Response response = given()
                .header("Content-type", "application/json")
                .when()
                .get(ApiHandles.ORDERS_PATH);
        return response;
    }

    @Step
    public static Response createOrder(String accessToken, Order order) {
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .header("Authorization", accessToken)
                .and()
                .body(order)
                .when()
                .post(ApiHandles.ORDERS_PATH);
        return response;
    }

    @Step
    public static Response createOrderWithoutAuth(Order order) {
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(order)
                .when()
                .post(ApiHandles.ORDERS_PATH);
        return response;
    }

    @Step
    public static List<String> getAllIngredients() {
        List<String> allIngredients = given()
                .header("Content-type", "application/json")
                .when()
                .get(ApiHandles.INGREDIENTS_PATH)
                .then()
                .extract()
                .path("data._id");
        return allIngredients;
    }
}