package tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.BeforeMethod;
import pages.LoginPage;

public class BaseTest {

    protected static WebDriver driver;
    protected static LoginPage loginPage;

    @BeforeMethod(alwaysRun = true, description = "Open browser")
    public void setup() {
        Configuration.timeout = 20000;
        Configuration.clickViaJs = true;
        Configuration.browserSize = null;
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--incognito");
        driver = new ChromeDriver(options);
        WebDriverRunner.setWebDriver(driver);
        loginPage = new LoginPage();
    }
}
