package by.stest.utilities;

import org.testng.annotations.DataProvider;

import java.lang.reflect.Method;

public class DataProviders {

    @DataProvider(name = "bankManagerDP", parallel = true)
    public static Object[][] getDataSuite1(Method m) {
        ExcelReader excel = new ExcelReader(Constants.SUITE1_XL_PATH);
        //ExcelReader excel = new ExcelReader("src/test/resources/testData/BankManagerSuite.xlsx");
        String testCase = m.getName();
        return DataUtil.getData(testCase, excel);
    }

    @DataProvider(name = "customerDP", parallel = true)
    public static Object[][] getDataSuite2(Method m) {
        ExcelReader excel = new ExcelReader(Constants.SUITE2_XL_PATH);
        String testCase = m.getName();
        return DataUtil.getData(testCase, excel);
    }
}
