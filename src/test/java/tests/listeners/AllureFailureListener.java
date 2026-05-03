package tests.listeners;

import com.codeborne.selenide.WebDriverRunner;
import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.ByteArrayInputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

public class AllureFailureListener implements ITestListener {

    @Override
    public void onTestFailure(ITestResult result) {
        attachScreenshotIfPossible();
        Throwable throwable = result.getThrowable();
        if (throwable != null) {
            Allure.addAttachment("Failure stack trace", "text/plain",
                    stackTraceAsString(throwable));
        }
    }

    private static void attachScreenshotIfPossible() {
        try {
            if (!WebDriverRunner.hasWebDriverStarted()) {
                return;
            }
            WebDriver driver = WebDriverRunner.getWebDriver();
            if (driver instanceof TakesScreenshot takesScreenshot) {
                byte[] png = takesScreenshot.getScreenshotAs(OutputType.BYTES);
                Allure.addAttachment("Failure screenshot", "image/png",
                        new ByteArrayInputStream(png), ".png");
            }
            String url = driver.getCurrentUrl();
            Allure.addAttachment("Browser URL on failure", "text/plain",
                    new ByteArrayInputStream(url.getBytes(StandardCharsets.UTF_8)),
                    ".txt");
        } catch (Exception ignored) {
        }
    }

    private static String stackTraceAsString(Throwable throwable) {
        StringWriter sw = new StringWriter();
        throwable.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }

}
