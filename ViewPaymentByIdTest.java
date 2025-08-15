package payments;

import base.BaseTest;
import config.ConfigLoader;
import io.restassured.path.json.JsonPath;
import io.restassured.path.xml.XmlPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.CSVDataProvider;

import static io.restassured.RestAssured.given;

public class ViewPaymentByIdTest extends BaseTest {

    @Test(dataProvider = "viewPaymentByIdData", dataProviderClass = CSVDataProvider.class)
    public void testViewPaymentById(String[] row) {
        String paymentId = row[1];
        int expectedStatus = Integer.parseInt(row[2]);

        Response response =
                given()
                        .accept("*/*")
                .when()
                        .get(ConfigLoader.get("payment_service_url") + "/viewPaymentById/" + paymentId);

        Assert.assertEquals(response.statusCode(), expectedStatus);
        //System.out.println(response.asString());
        
        if (expectedStatus == 200) {
            String contentType = response.getContentType();
            if (contentType.contains("xml")) {
                XmlPath xml = new XmlPath(response.asString());
                String actualId = xml.getString("payments.paymentId");
                
                //System.out.println("Actual: " + actualId + " and " + paymentId);
                
                Assert.assertEquals(actualId, paymentId, "Payment ID mismatch in XML response");
            } else {
                JsonPath json = new JsonPath(response.asString());
                String actualId = json.getString("paymentId");
                Assert.assertEquals(actualId, paymentId, "Payment ID mismatch in JSON response");
            }
        }

        System.out.println("✅ Checked Payment ID: " + paymentId + " | Expected Status: " + expectedStatus);
    }
}