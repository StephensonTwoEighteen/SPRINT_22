package http;

import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class ApiCourier extends SamokatClient {
    private String login;
    private String password;
    private String firstName;
    private Long id;

    public ApiCourier(String login, String password, String firstName){
        this.login = login;
        this.password = password;
        this.firstName = firstName;
    }
    public ApiCourier(String login){
        this.login = login;
        this.password = password;
    }
    public ApiCourier(Long id){
        this.id = id;
    }
    public ApiCourier() {
    }

    public ValidatableResponse createCourier(Courier courier) {
        return given().spec(baseSpec())
                .body(courier)
                .when()
                .post("/api/v1/courier")
                .then().statusCode(201);
    }
    public ValidatableResponse createCourierNegative(Courier courier) {
        return given().spec(baseSpec())
                .body(courier)
                .when()
                .post("/api/v1/courier")
                .then().statusCode(400);
    }
    public ValidatableResponse doubleCreateCourier(Courier courier) {
        return given().spec(baseSpec())
                .body(courier)
                .when()
                .post("/api/v1/courier")
                .then().statusCode(409);
    }

    public ValidatableResponse loginCourier(Courier courier) {
        return given().spec(baseSpec())
                .body(courier)
                .when()
                .post("/api/v1/courier/login")
                .then().statusCode(200);
    }

    public ValidatableResponse deleteCouriere(Long id){
        return given().spec(baseSpec())
                .when()
                .delete("/api/v1/courier/" + id)
                .then().statusCode(200);
    }
}