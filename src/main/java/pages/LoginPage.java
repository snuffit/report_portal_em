package pages;

import io.qameta.allure.Allure;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class LoginPage {

    // URLs
    private static final String URL_LOGIN = "https://demo.reportportal.io/ui/#login";

    // Locators
    private static final By USERNAME_INPUT = By.name("login");
    private static final By PASSWORD_INPUT = By.name("password");
    private static final By LOGIN_BUTTON = By.cssSelector("button[type='submit']");

    public LoginPage openPage() {
        return Allure.step("Open login page", () -> {
            open(URL_LOGIN);
            return this;
        });
    }

    public LaunchesPage auth(String login, String password) {
        return Allure.step("Submit credentials and open app", () -> inputLogin(login)
                .inputPassword(password)
                .clickLogin());
    }

    private LoginPage inputLogin(String login) {
        $(USERNAME_INPUT).setValue(login);
        return this;
    }

    private LoginPage inputPassword(String password) {
        $(PASSWORD_INPUT).setValue(password);
        return this;
    }

    private LaunchesPage clickLogin() {
        $(LOGIN_BUTTON).click();
        return new LaunchesPage();
    }
}
