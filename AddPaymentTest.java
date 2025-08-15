package payments;

import static io.restassured.RestAssured.given;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseTest;
import config.ConfigLoader;
import io.restassured.path.xml.XmlPath;
import io.restassured.response.Response;
import utils.CSVDataProvider;

public class AddPaymentTest extends BaseTest {

    @Test(dataProvider = "addPaymentData", dataProviderClass = CSVDataProvider.class)
    public void testAddPayment(String[] row) {
        String paymentId = row[0];
        String paymentAmount = row[1];
        String paymentDesc = row[2];
        String paymentDate = row[3];
        String customerName = row[4];
        String paymentStatus = row[5];
        String paymentMode = row[6];
        int expectedStatus = Integer.parseInt(row[7]);

        Response response =
                given()
                        .contentType("application/x-www-form-urlencoded")
                        .formParam("paymentId", paymentId)
                        .formParam("paymentAmount", paymentAmount)
                        .formParam("paymentDesc", paymentDesc)
                        .formParam("paymentDate", paymentDate)
                        .formParam("customerName", customerName)
                        .formParam("paymentStatus", paymentStatus)
                        .formParam("paymentMode", paymentMode)
                .when()
                        .post(ConfigLoader.get("payment_service_url") + "/addPayment");

        Assert.assertEquals(response.statusCode(), expectedStatus);
        //System.out.println(response.asString());
        
        if (expectedStatus == 200 || expectedStatus == 201) {
            XmlPath xml = new XmlPath(response.asString());
            // Get all paymentIds as a List
            List<String> paymentIds = xml.getList("payments.payment.paymentId");

            // Check if the expected paymentId exists in the list
            Assert.assertTrue(paymentIds.contains(paymentId),
                    "Payment ID " + paymentId + " not found in XML response");
        }

        System.out.println("✅ Added Payment ID: " + paymentId + " | Expected Status: " + expectedStatus);
    }
}