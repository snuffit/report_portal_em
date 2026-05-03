package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Allure;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class LaunchesPage extends BasePage {

    // URLs
    private static final String URL_BASE_UI = "https://demo.reportportal.io/ui";
    private static final String PATH_LAUNCHES_ALL = "/#default_personal/launches/all";
    private static final String URL_LAUNCHES_PAGE = URL_BASE_UI + PATH_LAUNCHES_ALL;

    public static final String ATTR_KEY = "test";
    public static final String ATTR_VALUE = "test";

    // Locators
    private static final By LAUNCHES_LINK = By.cssSelector("a[href*='/launches']");
    private static final By SEARCH_INPUT = By.cssSelector("input[placeholder*='Search'], input[type='search']");

    // Texts
    private static final String TEXT_TAB_ALL_LAUNCHES = "all launches";
    private static final String TEXT_TAB_LATEST_LAUNCHES = "latest launches";
    private static final String REGEX_LAUNCHES_PAGE_MARKER = "(?is).*launches.*";

    public LaunchesPage openPage() {
        return Allure.step("Open Launches (All) page", () -> {
            open(URL_LAUNCHES_PAGE);
            if ($(LAUNCHES_LINK).exists()) {
                $(LAUNCHES_LINK).click();
            }
            shouldBeOpened();
            return this;
        });
    }

    public LaunchesPage shouldBeOpened() {
        return Allure.step("Verify Launches page is open", () -> {
            shouldSeeBodyByPattern(REGEX_LAUNCHES_PAGE_MARKER);
            return this;
        });
    }

    public LaunchesPage openAllTab() {
        return Allure.step("Open tab: All launches", () -> {
            clickElementByText(TEXT_TAB_ALL_LAUNCHES);
            return this;
        });
    }

    public boolean openLatestTabIfVisible() {
        return Allure.step("Open tab: Latest launches (if visible)", () ->
                clickElementIfVisibleAndGetResult(TEXT_TAB_LATEST_LAUNCHES));
    }

    public LaunchesPage searchLaunch(String launchName) {
        return Allure.step("Search launch: " + launchName, () -> {
            SelenideElement input = $(SEARCH_INPUT);
            if (input.exists()) {
                input.shouldBe(Condition.visible).clear();
                input.setValue(launchName);
            }
            return this;
        });
    }

    public boolean waitUntilContainsText(String text) {
        return Allure.step("Wait for text in page body: " + text, () -> {
            try {
                shouldSeeTextInBody(text);
                return true;
            } catch (AssertionError error) {
                return false;
            }
        });
    }

    public DashboardPage openDashboardPage() {
        return Allure.step("Navigate to Dashboard", () -> new DashboardPage().openPage());
    }
}
