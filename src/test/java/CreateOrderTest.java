import dataForTesting.Url;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import orders.Order;
import orders.OrderOperations;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import users.User;
import users.UserActions;
import dataForTesting.GenerationData;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class CreateOrderTest {

    User user;
    Order order;
    String accessToken;
    int ingredientSublistSize;
    List<String> ingredients;
    List<String> allIngredients;


    @Before
    public void setUp() {
        RestAssured.baseURI = Url.Url;
        allIngredients = OrderOperations.getAllIngredients();

        user = GenerationData.generateUser();
        Response response = UserActions.createUser(user);
        accessToken = response.then().extract().path("accessToken").toString();
    }

    @After
    public void tearDown() {
        UserActions.deleteUser(accessToken);
    }

    @Test
    @DisplayName("Создание заказа, с ингредиентами, авторизованным пользователем")
    public void createCorrectOrderWithIngredientsAuthorizedUserGetSuccess() {
        ingredientSublistSize = GenerationData.generateSizeForIngredientSublist(allIngredients.size());
        ingredients = allIngredients.subList(0, ingredientSublistSize);
        order = new Order(ingredients);
        OrderOperations.createOrder(accessToken, order)
                .then().assertThat().statusCode(HttpStatus.SC_OK)
                .and()
                .body("success", equalTo(true))
                .and()
                .body("order.number", notNullValue())
                .and()
                .body("name", notNullValue());
    }

    @Test
    @DisplayName("Создание корректного заказа с ингредиентами, авторизованным пользователем")
    public void createOrderWithoutIngredientsAuthorizedUserGetError() {
        order = new Order(ingredients);
        OrderOperations.createOrder(accessToken, order)
                .then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("Ingredient ids must be provided"));
    }


    @Test
    @DisplayName("Создание заказа с недопустимым ингредиентом, авторизованным пользователем")
    public void createOrderInvalidIngredientAuthorizedUserGetSuccess() {
        ingredients = new ArrayList<>();
        ingredients.add(RandomStringUtils.randomAlphabetic(24));
        order = new Order(ingredients);
        OrderOperations.createOrder(accessToken, order)
                .then().assertThat().statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }


    @Test
    @DisplayName("Создание заказа с ингредиентами, неавторизованным пользователем")
    public void createOrderWithIngredientsUnauthorizedUserGetError() {
        ingredientSublistSize = GenerationData.generateSizeForIngredientSublist(allIngredients.size());
        ingredients = allIngredients.subList(0, ingredientSublistSize);
        order = new Order(ingredients);
        OrderOperations.createOrderWithoutAuth(order)
                .then().assertThat().statusCode(HttpStatus.SC_OK);
    }
}