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

public class AddCustomerTest extends TestBase {

    @Test(dataProviderClass = DataProviders.class, dataProvider = "bankManagerDP")
    public void addCustomerTest(Hashtable<String, String> data) throws MalformedURLException {
        setUp();
        test = extent.createTest("Add Customer Test " + data.get("browser"));
        setExtentTestThread(test);
        //ExcelReader excel = new ExcelReader(Constants.SUITE1_XL_PATH);
        ExcelReader excel = new ExcelReader("src/test/resources/testData/BankManagerSuite.xlsx");
        DataUtil.checkExecution("BankManagerSuite", "AddCustomerTest",
                data.get("RunMode"), excel);
        openBrowser(data.get("browser"));
        navigateTo("testsiteurl");
        click("bmlBtn_CSS");
        click("addCustBtn_CSS");
        type("firstname_CSS", data.get("firstName"));
        type("lastname_XPATH", data.get("lastName"));
        type("postcode_CSS", data.get("postCode"));
        click("addbtn_CSS");
        reportPass("Add Customer Test passed successfully");
    }

    @AfterMethod
    public void tearDown() {
        if (extent != null) {
            extent.flush();
        }
        getDriver().quit();
    }
}
