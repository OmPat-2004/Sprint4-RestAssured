package payments;

import base.BaseTest;
import config.ConfigLoader;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.ExtentReportListener;

import static io.restassured.RestAssured.given;

public class ViewPaymentListTest extends BaseTest {

    @Test
    public void viewPaymentList() {
        String url = ConfigLoader.get("payment_service_url") + "/viewPaymentList";

        Response response =
                given()
                        .accept("*/*")
                .when()
                        .get(url);

        // Extent logging
//        ExtentReportListener.getTest().info("Request URL: " + url);
//        ExtentReportListener.getTest().info("Response Status Code: " + response.getStatusCode());
//        ExtentReportListener.getTest().info("<pre>" + response.asPrettyString() + "</pre>");

        // Assertions
        Assert.assertEquals(response.statusCode(), 200, "Unexpected status code");
        Assert.assertFalse(response.asString().isEmpty(), "Payment list should not be empty");

        //ExtentReportListener.getTest().pass("✅ View Payment List checked successfully");
    }
}