package pages;

import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class LoginPage {

    private final String URI = "https://demo.reportportal.io/ui/#login",
            LOGIN = "login",
            PASSWORD = "password",
            CSS_BUTTON_LOGIN = "[type=submit]";

    public void openPage() {
        open(URI);
    }

    public void auth(String login, String password) {
        inputLogin(login);
        inputPassword(password);
        clickLogin();
    }

    private void inputLogin(String login) {
        $(By.name(LOGIN)).setValue(login);
    }

    private void inputPassword(String password) {
        $(By.name(PASSWORD)).setValue(password);
    }

    private void clickLogin() {
        $(CSS_BUTTON_LOGIN).click();
    }
}
