package tests.ui;

import config.ApiConfig;
import pages.LaunchesPage;
import tests.BaseTest;
import org.testng.annotations.Test;

import java.util.UUID;

public class LaunchesTest extends BaseTest {

    @Test(description = "Latest launches tab shows latest launch from API")
    public void latestLaunchesTabShouldShowLatestLaunchFromApi() {
        String latestLaunchName = "ui-latest-" + UUID.randomUUID();
        String olderLaunchName = "ui-latest-old-" + UUID.randomUUID();

        launchesApiSteps.createAndFinishLaunch(olderLaunchName);
        launchesApiSteps.createAndFinishLaunch(latestLaunchName);

        loginStep.auth(ApiConfig.getLogin(), ApiConfig.getPassword());
        launchesPage.openPage()
                .openAllTab()
                .openLatestTabIfPresent()
                .shouldContainText(latestLaunchName);
    }

    @Test(description = "Launch with test:test attribute is displayed in UI")
    public void launchAttributeShouldBeVisible() {
        String launchName = "ui-attr-" + UUID.randomUUID();
        String launchId = launchesApiSteps.createLaunchAndGetId(launchName);
        launchesApiSteps.addAttributeToLaunch(launchId, LaunchesPage.ATTR_KEY, LaunchesPage.ATTR_VALUE);
        launchesApiSteps.finishLaunch(launchId);

        loginStep.auth(ApiConfig.getLogin(), ApiConfig.getPassword());
        launchesPage.openPage()
                .searchLaunch(launchName)
                .shouldContainText(launchName)
                .shouldContainText(LaunchesPage.ATTR_KEY)
                .shouldContainText(LaunchesPage.ATTR_VALUE);
    }

    @Test(description = "Search on launches page finds launch prepared by API")
    public void launchesSearchShouldFindPreparedLaunch() {
        String launchName = "ui-search-" + UUID.randomUUID();
        launchesApiSteps.createAndFinishLaunch(launchName);

        loginStep.auth(ApiConfig.getLogin(), ApiConfig.getPassword());
        launchesPage.openPage()
                .searchLaunch(launchName)
                .shouldContainText(launchName);
    }

    @Test(description = "Widget can be added on dashboard")
    public void shouldAddWidgetOnDashboard() {
        loginStep.auth(ApiConfig.getLogin(), ApiConfig.getPassword());
        launchesPage.openDashboardPage()
                .addAnyWidget()
                .shouldSeeWidgetArea();
    }
}
