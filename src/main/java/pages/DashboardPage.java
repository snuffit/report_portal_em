package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import io.qameta.allure.Allure;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.open;

public class DashboardPage extends BasePage {

    // URLs
    private static final String URL_BASE_UI = "https://demo.reportportal.io/ui";
    private static final String PATH_DASHBOARD = "/#default_personal/dashboard";
    private static final String URL_DASHBOARD_PAGE = URL_BASE_UI + PATH_DASHBOARD;

    // Locators
    private static final By BODY = By.tagName("body");
    private static final By DASHBOARD_LINKS = By.cssSelector("a[href*='/dashboard/']");
    private static final By DASHBOARD_NAME_INPUT = By.cssSelector("input[placeholder*='name'], input[name='name']");

    // Texts and markers
    private static final String TEXT_NO_DASHBOARDS = "You have no dashboards";
    private static final String TEXT_ADD_NEW_DASHBOARD = "add new dashboard";
    private static final String TEXT_ADD_NEW_WIDGET = "add new widget";
    private static final String TEXT_ADD = "add";
    private static final String TEXT_SAVE = "save";
    private static final String TEXT_CREATE = "create";
    private static final String REGEX_WIDGET_AREA_MARKERS = "(?is).*(widget|add\\s+new\\s+widget).*";
    private static final String REGEX_DASHBOARD_MARKERS = "(?is).*(dashboard|add\\s+new\\s+widget).*";

    public DashboardPage openPage() {
        return Allure.step("Open Dashboard page", () -> {
            open(URL_DASHBOARD_PAGE);
            shouldSeeBodyByPattern(REGEX_DASHBOARD_MARKERS);
            return this;
        });
    }

    public DashboardPage ensureDashboardExistsAndOpened() {
        if (isBodyContainsText(TEXT_NO_DASHBOARDS)) {
            clickElementByText(TEXT_ADD_NEW_DASHBOARD);
            SelenideElement nameInput = $(DASHBOARD_NAME_INPUT);
            nameInput.shouldBe(Condition.visible).setValue("autotest-dashboard-" + System.currentTimeMillis());
            clickElementIfVisible(TEXT_ADD);
            clickElementIfVisible(TEXT_SAVE);
            clickElementIfVisible(TEXT_CREATE);
        }

        ElementsCollection dashboardLinks = $$(DASHBOARD_LINKS)
                .filter(Condition.visible)
                .filterBy(Condition.not(Condition.attribute("href", PATH_DASHBOARD)));
        if (!dashboardLinks.isEmpty()) {
            SelenideElement dashboardLink = dashboardLinks.first();
            String dashboardHref = dashboardLink.getAttribute("href");
            if (dashboardHref != null && !dashboardHref.contains(PATH_DASHBOARD)) {
                open(WebDriverRunner.url().replaceAll("#.*$", "") + dashboardHref);
            } else {
                dashboardLink.click();
            }
        }

        return this;
    }

    public WidgetCreationModal clickAddNewWidget() {
        return Allure.step("Open Add new widget flow", () -> {
            ensureDashboardExistsAndOpened();
            if (isBodyMatchesPattern(REGEX_WIDGET_AREA_MARKERS)) {
                clickElementByText(TEXT_ADD_NEW_WIDGET);
            }
            return new WidgetCreationModal();
        });
    }

    private void assertWidgetAreaVisible() {
        $(BODY).shouldHave(Condition.or("widget markers",
                Condition.text("Widget"),
                Condition.text("Add New Dashboard"),
                Condition.text("LAUNCHES"),
                Condition.text("INVESTIGATIONS")));
    }

    public boolean isWidgetAreaVisible() {
        return Allure.step("Check widget area is visible", () -> {
            try {
                assertWidgetAreaVisible();
                return true;
            } catch (AssertionError error) {
                return false;
            }
        });
    }

    public DashboardPage addAnyWidget() {
        return Allure.step("Add any widget (wizard)", () -> clickAddNewWidget()
                .createDefaultWidget());
    }
}