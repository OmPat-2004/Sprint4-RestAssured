package payments;

import base.BaseTest;
import config.ConfigLoader;
import io.restassured.path.json.JsonPath;
import io.restassured.path.xml.XmlPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.CSVDataProvider;

import java.util.List;

import static io.restassured.RestAssured.given;

public class DeletePaymentTest extends BaseTest {

    @Test(dataProvider = "deletePaymentData", dataProviderClass = CSVDataProvider.class)
    public void testDeletePayment(String[] row) {
        String paymentId = row[0];
        int expectedStatus = Integer.parseInt(row[1]);

        Response response =
                given()
                        .accept("*/*")
                .when()
                        .delete(ConfigLoader.get("payment_service_url") + "/deletePaymentById/" + paymentId);

        Assert.assertEquals(response.statusCode(), expectedStatus);

        if (expectedStatus == 200 || expectedStatus == 204) {
            String contentType = response.getContentType();
            if (contentType.contains("xml")) {
                XmlPath xml = new XmlPath(response.asString());
                List<String> ids = xml.getList("payments.payment.paymentId");
                Assert.assertTrue(ids == null || ids.stream().noneMatch(id -> id.equals(paymentId)),
                        "Payment ID still exists in XML after deletion");
            } else {
                JsonPath json = new JsonPath(response.asString());
                List<String> ids = json.getList("paymentId");
                Assert.assertTrue(ids == null || ids.stream().noneMatch(id -> id.equals(paymentId)),
                        "Payment ID still exists in JSON after deletion");
            }
        }

        System.out.println("✅ Deleted Payment ID: " + paymentId + " | Expected Status: " + expectedStatus);
    }
}