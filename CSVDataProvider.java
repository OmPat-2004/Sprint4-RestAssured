package utils;

import com.opencsv.CSVReader;
import org.testng.annotations.DataProvider;

import java.io.FileReader;
import java.util.Iterator;
import java.util.List;

public class CSVDataProvider {

    private static Iterator<Object[]> readCSV(String filePath) throws Exception {
        CSVReader reader = new CSVReader(new FileReader(filePath));
        List<String[]> allRows = reader.readAll();
        reader.close();

        // Remove header row
        if (!allRows.isEmpty()) {
            allRows.remove(0);
        }

        return allRows.stream()
                .map(row -> new Object[]{row})
                .iterator();
    }

    @DataProvider(name = "loginData")
    public static Iterator<Object[]> loginData() throws Exception {
        return readCSV("src/test/resources/data/login.csv");
    }

    @DataProvider(name = "viewPaymentByIdData")
    public static Iterator<Object[]> viewPaymentByIdData() throws Exception {
        return readCSV("src/test/resources/data/viewPaymentById.csv");
    }

    @DataProvider(name = "viewPaymentByModeData")
    public static Iterator<Object[]> viewPaymentByModeData() throws Exception {
        return readCSV("src/test/resources/data/viewPaymentByMode.csv");
    }

    @DataProvider(name = "addPaymentData")
    public static Iterator<Object[]> addPaymentData() throws Exception {
        return readCSV("src/test/resources/data/addPayment.csv");
    }

    @DataProvider(name = "updatePaymentAmountData")
    public static Iterator<Object[]> updatePaymentAmountData() throws Exception {
        return readCSV("src/test/resources/data/updatePaymentAmount.csv");
    }

    @DataProvider(name = "deletePaymentData")
    public static Iterator<Object[]> deletePaymentData() throws Exception {
        return readCSV("src/test/resources/data/deletePayment.csv");
    }
}