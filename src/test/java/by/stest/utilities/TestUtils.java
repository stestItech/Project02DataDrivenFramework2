package by.stest.utilities;

import by.stest.datadriven.base.TestBase;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class TestUtils extends TestBase {

    public static String screenshotName;

    public static void captureScreenshot() throws IOException {
        System.setProperty("org.uncommons.reportng.escape-output", "false");
        // org.uncommons.reportng.escape-output
        // Used to turn off escaping for log output in the reports (not recommended). The default is for output
        // to be escaped, since this prevents characters such as '<' and '&' from causing mark-up problems.
        // If escaping is turned off, then log text is included as raw HTML/XML, which allows for the insertion
        // of hyperlinks and other nasty hacks.
        File scrFile = ((TakesScreenshot)getDriver()).getScreenshotAs(OutputType.FILE);
        Date d = new Date();
        screenshotName = d.toString().replace(":", "_")
                .replace(" ", "_") + ".jpg";
        FileUtils.copyFile(scrFile,
                new File("target//surefire-reports//html//" + screenshotName));
        getExtentTestThread().fail("<b>" + "<font color=" + "red>" +
                        "Screenshot of failure" + "</font>" + "</b>",
                MediaEntityBuilder.createScreenCaptureFromPath(screenshotName).build());
    }


}
