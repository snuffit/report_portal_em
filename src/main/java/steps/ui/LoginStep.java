package steps.ui;

import io.qameta.allure.Allure;
import pages.LoginPage;

public class LoginStep {

    private final LoginPage loginPage = new LoginPage();

    public void auth(String login, String password) {
        Allure.step("UI: log in (user: " + login + ")", () ->
                loginPage.openPage()
                        .auth(login, password));
    }
}
