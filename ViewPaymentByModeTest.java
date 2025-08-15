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

public class ViewPaymentByModeTest extends BaseTest {

    @Test(dataProvider = "viewPaymentByModeData", dataProviderClass = CSVDataProvider.class)
    public void testViewPaymentByMode(String[] row) {
        String paymentMode = row[1];
        int expectedStatus = Integer.parseInt(row[2]);

        Response response =
                given()
                        .accept("*/*")
                        .queryParam("paymentMode", paymentMode)
                .when()
                        .get(ConfigLoader.get("payment_service_url") + "/viewPaymentByMode");

        Assert.assertEquals(response.statusCode(), expectedStatus);
        //System.out.println(response.asString());
        
        if (expectedStatus == 200) {
            String contentType = response.getContentType();
            if (contentType.contains("xml")) {
                XmlPath xml = new XmlPath(response.asString());
                List<String> modes = xml.getList("payment.paymentMode");
                Assert.assertTrue(modes.stream().allMatch(m -> m.equals(paymentMode)),
                        "Some payments have different payment modes (XML)");
            } else {
                JsonPath json = new JsonPath(response.asString());
                List<String> modes = json.getList("paymentMode");
                Assert.assertTrue(modes.stream().allMatch(m -> m.equals(paymentMode)),
                        "Some payments have different payment modes (JSON)");
            }
        }

        System.out.println("✅ Checked Payment Mode: " + paymentMode + " | Expected Status: " + expectedStatus);
    }
}