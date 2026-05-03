package tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import browser.BrowserSessionFactory;
import io.qameta.allure.Allure;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import pages.LaunchesPage;
import steps.api.LaunchesApiSteps;
import steps.ui.LoginStep;

public class BaseTest {

    protected WebDriver driver;
    protected LoginStep loginStep;
    protected LaunchesApiSteps launchesApiSteps;
    protected LaunchesPage launchesPage;

    @BeforeMethod(alwaysRun = true, description = "Open browser")
    public void setup() {
        Configuration.timeout = 20000;
        Configuration.clickViaJs = true;
        Configuration.browserSize = null;
        launchesApiSteps = new LaunchesApiSteps();

        if (requiresUiSession()) {
            Allure.step("Start browser session", () -> {
                driver = BrowserSessionFactory.createSession();
                WebDriverRunner.setWebDriver(driver);
            });
            loginStep = new LoginStep();
            launchesPage = new LaunchesPage();
        }
    }

    @AfterMethod(alwaysRun = true, description = "Close browser")
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    private boolean requiresUiSession() {
        return getClass().getPackageName().contains(".ui");
    }
}
