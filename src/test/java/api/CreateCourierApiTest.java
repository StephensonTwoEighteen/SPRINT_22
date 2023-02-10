package api;

import com.google.gson.Gson;
import http.ApiCourier;
import http.Courier;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class CreateCourierApiTest {
    Gson gson = new Gson();

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
    }

    @Test
    public void createCourierTest() {
        ApiCourier apiCourier = new ApiCourier();
        Courier courier = new Courier("testLoginSamokat", "testPasswordSamokat", "testFirstNameSamokat");

        ValidatableResponse response = apiCourier.createCourier(courier);
        int statusCode = response.extract().statusCode();
        String actualBody = response.extract().body().asPrettyString();

        ValidatableResponse responseLogin = apiCourier.loginCourier(courier);
        String loginBody = responseLogin.extract().body().asPrettyString();

        System.out.println(statusCode);
        System.out.println(actualBody);

        String jsonResponse = "{\n    \"ok\": true\n}";

        assertThat(statusCode, equalTo(201));
        assertThat(actualBody, equalTo(jsonResponse));
    }

    @Test
    public void deleteCourierTest(){
        ApiCourier apiCourier = new ApiCourier();
        Courier courier = new Courier("testLoginSamokat", "testPasswordSamokat", "testFirstNameSamokat");

        apiCourier.createCourier(courier);
        ValidatableResponse responseLogin = apiCourier.loginCourier(courier);
        String loginBody = responseLogin.extract().body().asPrettyString();

        //Удаление курьера
        Courier newCourier = gson.fromJson(loginBody, Courier.class);
        ValidatableResponse responseDelete = apiCourier.deleteCouriere(newCourier.id);
        String deleteMethodBody = responseDelete.extract().body().asPrettyString();
        System.out.println(deleteMethodBody);
    }

    @Test
    public void createCourierNegativeTest() {
        ApiCourier apiCourierCreate = new ApiCourier("testLoginSamokat");
        Courier courier = new Courier();

        ValidatableResponse response = apiCourierCreate.createCourierNegative(courier);
        int statusCode = response.extract().statusCode();
        String actualBody = response.extract().body().asPrettyString();

        System.out.println(statusCode);
        System.out.println(actualBody);

        String jsonResponse = "{\n    \"code\": 400,\n    \"message\": \"Недостаточно данных для создания учетной записи\"\n}";

        assertThat(statusCode, equalTo(400));
        assertThat(actualBody, equalTo(jsonResponse));
    }

    @Test
    public void doubleCreateCourierTest() {
        ApiCourier apiCourier = new ApiCourier();
        Courier courier = new Courier("testLoginSamokat", "testPasswordSamokat", "testFirstNameSamokat");

        //Создание курьера
        apiCourier.createCourier(courier);

        //Логин курьера
        ValidatableResponse responseLogin = apiCourier.loginCourier(courier);
        String loginBody = responseLogin.extract().body().asPrettyString();
        System.out.println(loginBody);

        //Повторное создание курьера с идентичными данными
        ValidatableResponse secondCreateResponse = apiCourier.doubleCreateCourier(courier);
        String courierSecondCreateResponse = secondCreateResponse.extract().body().asPrettyString();
        int statusCodeNegative = secondCreateResponse.extract().statusCode();
        System.out.println(courierSecondCreateResponse);

        String jsonResponse = "{\n    \"code\": 409,\n    \"message\": \"Этот логин уже используется. Попробуйте другой.\"\n}";

        assertThat(statusCodeNegative, equalTo(409));
        assertThat(courierSecondCreateResponse, equalTo(jsonResponse));

        //Удаление курьера
        Courier newCourier = gson.fromJson(loginBody, Courier.class);
        ValidatableResponse responseDelete = apiCourier.deleteCouriere(newCourier.id);
        String deleteMethodBody = responseDelete.extract().body().asPrettyString();
        System.out.println(deleteMethodBody);
    }
}
