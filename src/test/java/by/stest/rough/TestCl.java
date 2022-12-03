package by.stest.rough;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;

import java.io.File;
import java.io.IOException;

public class TestCl {

    public static Logger log2 = LogManager.getLogger(TestCl.class.getName());

    public static void main(String[] args) throws IOException {
        File f = new File("src/test/resources/properties/log4j2.properties");
        LoggerContext context = (LoggerContext) LogManager.getContext(false);
        context.setConfigLocation(f.toURI());
        log2.error("Some error");
    }
}
