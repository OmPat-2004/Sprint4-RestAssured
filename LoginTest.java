package auth;

import base.BaseTest;
import config.ConfigLoader;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.CSVDataProvider;
import utils.ExtentReportListener;

import static io.restassured.RestAssured.given;

public class LoginTest extends BaseTest {
    public static String authCode;

    @Test(dataProvider = "loginData", dataProviderClass = CSVDataProvider.class)
    public void loginAndGetAuthCode(String[] row) {
        String username = row[0];
        String password = row[1];
        int expectedStatus = Integer.parseInt(row[2]);
        String url = ConfigLoader.get("auth_endpoint") + "/login";

        Response response =
                given()
                        .contentType(ContentType.URLENC)
                        .formParam("username", username)
                        .formParam("password", password)
                .when()
                        .post(url);
        
        // Logging into Extent Report
//        ExtentReportListener.getTest().info("Request URL: " + url);
//        ExtentReportListener.getTest().info("Form Parameters-> username: " + username + " & password: " + password);
//        ExtentReportListener.getTest().info("Response Status Code: " + response.statusCode());
//        ExtentReportListener.getTest().info("Response Body: " + response.asPrettyString());
        
        Assert.assertEquals(response.statusCode(), expectedStatus);

        if (expectedStatus == 200) {
            authCode = response.jsonPath().getString("auth_code");
            Assert.assertNotNull(authCode, "Auth code should not be null");
            System.out.println("✅ Auth Code: " + authCode);
        }
    }
}