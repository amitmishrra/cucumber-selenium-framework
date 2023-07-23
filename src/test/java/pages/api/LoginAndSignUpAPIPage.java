package pages.api;

import common.Utils;
import stepdefintions.CommonVariablesAndMethods;
import java.io.IOException;
import static common.Utils.requestSpecification;
import static io.restassured.RestAssured.given;

public class LoginAndSignUpAPIPage extends CommonVariablesAndMethods {

    String accessToken = null;
    public void userSignUp(String username, String password) throws IOException {
        String body = "{\n" +
                "    \"email\": \""+username+"\",\n" +
                "    \"password\": \""+password+"\",\n" +
                "    \"role\": \"patient\"\n" +
                "}";

        String response = given()
                .spec(requestSpecification())
                .body(body)
                .when()
                .post(Utils.getPropertyData("kaliber.api.baserurl") + "/users/signup")
                .then()
                .extract()
                .response()
                .asString();
        System.out.println(response);
    }

    public void accessTokenLogin(String username, String password) throws IOException {
        String body = "{\n" +
                "    \"username\": \""+username+"\",\n" +
                "    \"password\": \""+password+"\"\n" +
                "}";
        String response = given().spec(requestSpecification()).body(body)
                .when().post(Utils.getPropertyData("kaliber.api.baserurl")+"/users/login")
                .then().assertThat().statusCode(200).extract().response().asString();
        System.out.println("Access Token: "+Utils.getStringResponse(response, "data.access_token"));
        accessToken = Utils.getStringResponse(response, "data.access_token");
    }

    public void addNewFacilityPage() throws IOException {
        String body = "{\n" +
                "    \"facility_name\": \"Long Brook\",\n" +
                "    \"facility_address\": \"142 Park Ave, San Francisco, CA\",\n" +
                "    \"facility_phone\": \"4157554\",\n" +
                "    \"facility_fax\": \"2136523\",\n" +
                "    \"facility_email\": \"info@longbrook.com\",\n" +
                "    \"facility_after_hours_phone\": \"52461276\",\n" +
                "    \"facility_website\": \"www.longbrook.com\",\n" +
                "    \"facility_timings\": [{\n" +
                "        \"day_of_week\": 1,\n" +
                "        \"opening_time\": \"8.30am\",\n" +
                "        \"closing_time\": \"6pm\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"day_of_week\": 2,\n" +
                "        \"opening_time\": \"8.30am\",\n" +
                "        \"closing_time\": \"6pm\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"day_of_week\": 3,\n" +
                "        \"opening_time\": \"8.30am\",\n" +
                "        \"closing_time\": \"6pm\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"day_of_week\": 4,\n" +
                "        \"opening_time\": \"8.30am\",\n" +
                "        \"closing_time\": \"6pm\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"day_of_week\": 5,\n" +
                "        \"opening_time\": \"8.30am\",\n" +
                "        \"closing_time\": \"6pm\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"day_of_week\": 6,\n" +
                "        \"opening_time\": \"10am\",\n" +
                "        \"closing_time\": \"3pm\"\n" +
                "    }]\n" +
                "}";

        String response = given()
                .spec(requestSpecification())
                .header("Authorization", "Bearer ")
                .body(body)
                .when()
                .post(Utils.getPropertyData("kaliber.api.baserurl")+"/facility/new")
                .then()
                .extract()
                .response()
                .asString();
        System.out.println(response);
    }

}
