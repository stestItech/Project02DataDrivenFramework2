package by.stest.suite.bankManager.testcases;

import by.stest.datadriven.base.TestBase;
import by.stest.utilities.Constants;
import by.stest.utilities.DataProviders;
import by.stest.utilities.DataUtil;
import by.stest.utilities.ExcelReader;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.util.Hashtable;

public class OpenAccountTest extends TestBase {

    @Test(dataProviderClass = DataProviders.class, dataProvider = "bankManagerDP")
    public void openAccountTest(Hashtable<String, String> data) throws MalformedURLException {
        setUp();
        test = extent.createTest("Open Account Test " + data.get("browser"));
        setExtentTestThread(test);
        ExcelReader excel = new ExcelReader(Constants.SUITE1_XL_PATH);
        DataUtil.checkExecution("BankManagerSuite", "OpenAccountTest",
                data.get("RunMode"), excel);
        openBrowser(data.get("browser"));
        navigateTo("testsiteurl");
        click("bmlBtn_CSS");
        click("openAccount_CSS");
        select("customer_CSS", data.get("customer"));
        select("currency_ID", data.get("currency"));
        click("process_CSS");
        reportPass("Open Account Test passed successfully");
    }

    @AfterMethod
    public void tearDown() {
        if (extent != null) {
            extent.flush();
        }
        getDriver().quit();
    }
}
