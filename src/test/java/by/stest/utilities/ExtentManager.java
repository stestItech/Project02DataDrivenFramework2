package by.stest.utilities;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.awt.*;
import java.util.Date;

public class ExtentManager {

    private static ExtentReports extent;

	public static ExtentReports createInstance() {
        Date d = new Date();
        String fileName = "jenkins_" + d.toString()
                .replace(":","_").replace(" ","_") + ".html";

		ExtentSparkReporter htmlReporter =
                new ExtentSparkReporter("target//surefire-reports//html//" + fileName);

        htmlReporter.config().setTheme(Theme.STANDARD);
        htmlReporter.config().setDocumentTitle(fileName);
        htmlReporter.config().setEncoding("utf-8");
        // htmlReporter.config().setEncoding("windows-1251");
        htmlReporter.config().setReportName(fileName);
        
        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);
        extent.setSystemInfo("Automation Tester", "Rahul Arora");
        extent.setSystemInfo("Organization", "Way2Automation");
        extent.setSystemInfo("Build no", "W2A-1234");
        
        return extent;
	}

}
