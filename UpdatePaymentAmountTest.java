package payments;

import base.BaseTest;
import config.ConfigLoader;
import io.restassured.path.xml.XmlPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.CSVDataProvider;

import static io.restassured.RestAssured.given;

import java.util.List;

public class UpdatePaymentAmountTest extends BaseTest {

    @Test(dataProvider = "updatePaymentAmountData", dataProviderClass = CSVDataProvider.class)
    public void testUpdatePaymentAmount(String[] row) {
        String paymentId = row[0];
        String newAmount = row[1];
        int expectedStatus = Integer.parseInt(row[2]);

        Response response =
                given()
                        .contentType("application/x-www-form-urlencoded")
                        .formParam("paymentId", paymentId)
                        .formParam("paymentAmount", newAmount)
                .when()
                        .put(ConfigLoader.get("payment_service_url") + "/updatePaymentAmount");

        Assert.assertEquals(response.statusCode(), expectedStatus);
        //System.out.println(response.asString());
        
        if (expectedStatus == 200) {
            XmlPath xml = new XmlPath(response.asString());
            
            // Get all paymentIds as a List
            List<String> paymentIds = xml.getList("payments.payment.paymentId");

            String updatedAmount = null;

            for (int i = 0; i < paymentIds.size(); i++) {
                if (paymentIds.get(i).equalsIgnoreCase(paymentId)) {
                    updatedAmount = xml.getString("payments.payment[" + i + "].paymentAmount");
                    break; // found the payment, no need to loop further
                }
            }

            Assert.assertNotNull(updatedAmount, "Payment ID not found in response");
            Assert.assertEquals(updatedAmount, newAmount, "Payment amount did not update correctly");
        }

        System.out.println("✅ Updated Payment ID: " + paymentId + " to amount: " + newAmount);
    }
}