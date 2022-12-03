package by.stest.utilities;

import org.testng.SkipException;
import org.testng.annotations.DataProvider;

import java.util.Hashtable;

public class DataUtil {

    public static void checkExecution(String suiteName, String testCaseName, String dataRunMode,
                                      ExcelReader excel) {
        if (!isSuiteRunnable(suiteName)) {
           throw new SkipException("Skipping the test: " + testCaseName + " as the RunMode for " +
                   suiteName + " is No");
        }

        if (!isTestRunnable(testCaseName, excel)) {
            throw new SkipException("Skipping the test: " + testCaseName + " as the RunMode " +
                    "for test case is No");
        }

        if (dataRunMode.equalsIgnoreCase(Constants.RUNMODE_NO)) {
            throw new SkipException("Skipping the test case: " + testCaseName + " as the " +
                    "data run mode is No");
        }

    }

    public static boolean isSuiteRunnable(String suitName) {
        //ExcelReader excel = new ExcelReader(Constants.SUITE_XL_PATH);
        ExcelReader excel = new ExcelReader("src/test/resources/testData/Suite.xlsx");
        int rows = excel.getRowCount(Constants.SUITE_SHEET);

        for (int rowNum = 2; rowNum <= rows; rowNum++) {
            String data = excel.getCellData(Constants.SUITE_SHEET, Constants.SUITNAME_COL, rowNum);

            if (data.equals(suitName)) {
                String runMode = excel.getCellData(Constants.SUITE_SHEET, Constants.RUNMODE_COL, rowNum);
                return runMode.equals(Constants.RUNMODE_YES);
            }
        }
        return false;
    }

    public static boolean isTestRunnable(String testName, ExcelReader excel) {
        int rows = excel.getRowCount(Constants.TESTCASE_SHEET);

        for (int rowNum = 2; rowNum <= rows; rowNum++) {
            String data = excel.getCellData(Constants.TESTCASE_SHEET, Constants.TESTCASES_COL, rowNum);

            if (data.equals(testName)) {
                String runMode = excel.getCellData(Constants.TESTCASE_SHEET, Constants.RUNMODE_COL,
                        rowNum);
                return runMode.equals(Constants.RUNMODE_YES);
            }
        }
        return false;
    }

    @DataProvider
    public static Object[][] getData(String testCase, ExcelReader excel) {

        int rows = excel.getRowCount(Constants.DATA_SHEET);
        // System.out.println("Total number of rows is: " + rows);
        String testName = testCase;

        // Find row where test case starts
        int testCaseRowNum;

        for (testCaseRowNum = 1; testCaseRowNum < rows; testCaseRowNum++) {
            String testCaseName = excel.getCellData(Constants.DATA_SHEET, 0, testCaseRowNum);

            if (testCaseName.equalsIgnoreCase(testName)) {
                break;
            }
        }

        // System.out.println("Test case starts from row number: " + testCaseRowNum);

        // Check number of rows with data in particular test case
        int dataStartRowNum = testCaseRowNum + 2;
        int testRows = 0;

        while (!excel.getCellData(Constants.DATA_SHEET, 0, dataStartRowNum + testRows)
                .equals("")) {
            testRows++;
        }

        // System.out.println("Total number of rows with data is: " + testRows);

        // Check number of columns with data in particular test case
        int testColumns = 0;

        while (!excel.getCellData(Constants.DATA_SHEET, testColumns, dataStartRowNum + 1)
                .equals("")) {
            testColumns++;
        }

        // System.out.println("Total number of columns with data is: " + testColumns);

        // Print data
        Object[][] data = new Object[testRows][1];
        int i = 0;
        for (int rNum = dataStartRowNum; rNum < (dataStartRowNum + testRows); rNum++) {
            Hashtable<String, String> table = new Hashtable<>();
            for (int colNum = 0; colNum < testColumns; colNum ++) {
                // System.out.println(excel.getCellData(Constants.DATA_SHEET, colNum, rNum));
                String testData = excel.getCellData(Constants.DATA_SHEET, colNum, rNum);
                String colName = excel.getCellData(Constants.DATA_SHEET,
                        colNum, testCaseRowNum + 1);
                table.put(colName,testData);
            }
            data[i][0] = table;
            i++;
        }

        return data;
    }
}
