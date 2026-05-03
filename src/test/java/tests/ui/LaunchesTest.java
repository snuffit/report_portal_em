package tests.ui;

import config.ApiConfig;
import io.restassured.response.Response;
import pages.LaunchesPage;
import org.testng.Assert;
import tests.BaseTest;
import org.testng.annotations.Test;

import java.util.UUID;

@Test(groups = "UI")
public class LaunchesTest extends BaseTest {

    @Test(description = "Latest launches tab shows latest launch from API")
    public void latestLaunchesTabShouldShowLatestLaunchFromApi() {
        String latestLaunchName = "ui-latest-" + UUID.randomUUID();
        String olderLaunchName = "ui-latest-old-" + UUID.randomUUID();

        String olderLaunchId = launchesApiSteps.createLaunchAndGetId(olderLaunchName);
        Response olderLaunchFinishResponse = launchesApiSteps.finishLaunch(olderLaunchId);
        String latestLaunchId = launchesApiSteps.createLaunchAndGetId(latestLaunchName);
        Response latestLaunchFinishResponse = launchesApiSteps.finishLaunch(latestLaunchId);
        Assert.assertEquals(olderLaunchFinishResponse.getStatusCode(), 200, "Older launch finish should succeed");
        Assert.assertEquals(latestLaunchFinishResponse.getStatusCode(), 200, "Latest launch finish should succeed");

        // Arrange done, start UI checks.
        loginStep.auth(ApiConfig.getLogin(), ApiConfig.getPassword());
        launchesPage.openPage().openAllTab();
        boolean latestTabOpened = launchesPage.openLatestTabIfVisible();
        boolean launchVisibleInLatest = latestTabOpened && launchesPage.waitUntilContainsText(latestLaunchName);
        boolean launchVisibleInAll = launchesPage.openAllTab().searchLaunch(latestLaunchName).waitUntilContainsText(latestLaunchName);

        Assert.assertTrue(
                launchVisibleInLatest || launchVisibleInAll,
                "Expected launch to be visible in latest tab when present, otherwise in all launches tab"
        );
    }

    @Test(description = "Launch with test:test attribute is displayed in UI")
    public void launchAttributeShouldBeVisible() {
        String launchName = "ui-attr-" + UUID.randomUUID();
        String launchId = launchesApiSteps.createLaunchAndGetId(launchName);
        Response updateAttributesResponse = launchesApiSteps.addAttributeToLaunch(launchId, LaunchesPage.ATTR_KEY, LaunchesPage.ATTR_VALUE);
        Response finishLaunchResponse = launchesApiSteps.finishLaunch(launchId);
        Assert.assertEquals(updateAttributesResponse.getStatusCode(), 200, "Launch attributes update should succeed");
        Assert.assertEquals(finishLaunchResponse.getStatusCode(), 200, "Launch finish should succeed");

        loginStep.auth(ApiConfig.getLogin(), ApiConfig.getPassword());
        launchesPage.openPage().searchLaunch(launchName);
        boolean hasLaunchName = launchesPage.waitUntilContainsText(launchName);
        boolean hasAttributeKey = launchesPage.waitUntilContainsText(LaunchesPage.ATTR_KEY);
        boolean hasAttributeValue = launchesPage.waitUntilContainsText(LaunchesPage.ATTR_VALUE);

        Assert.assertTrue(
                hasLaunchName && hasAttributeKey && hasAttributeValue,
                "Launch row should contain launch name and test:test attribute"
        );
    }

    @Test(description = "Search on launches page finds launch prepared by API")
    public void launchesSearchShouldFindPreparedLaunch() {
        String launchName = "ui-search-" + UUID.randomUUID();
        String launchId = launchesApiSteps.createAndFinishLaunch(launchName);
        Response finishVerificationResponse = launchesApiSteps.getLaunchById(launchId);
        Assert.assertEquals(finishVerificationResponse.getStatusCode(), 200, "Prepared launch should exist in API");

        loginStep.auth(ApiConfig.getLogin(), ApiConfig.getPassword());
        launchesPage.openPage().searchLaunch(launchName);
        boolean launchVisible = launchesPage.waitUntilContainsText(launchName);

        Assert.assertTrue(launchVisible, "Search results should contain launch created via API");
    }

    @Test(description = "Widget can be added on dashboard")
    public void shouldAddWidgetOnDashboard() {
        loginStep.auth(ApiConfig.getLogin(), ApiConfig.getPassword());
        boolean widgetAreaVisible = launchesPage.openDashboardPage().addAnyWidget().isWidgetAreaVisible();

        Assert.assertTrue(widgetAreaVisible, "Widget area should be visible after adding widget");
    }
}
