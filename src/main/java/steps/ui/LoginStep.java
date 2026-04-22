package steps.ui;

import pages.LoginPage;

public class LoginStep {

    private final LoginPage loginPage = new LoginPage();

    public void auth(String login, String password) {
        loginPage.openPage()
                .auth(login, password);
    }
}
