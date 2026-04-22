package tests.api;

import org.testng.annotations.Test;
import tests.BaseTest;

import java.util.UUID;

public class LaunchesCrudApiTest extends BaseTest {

    @Test(description = "Create launch via API")
    public void createLaunchTest() {
        String launchName = "api-create-" + UUID.randomUUID();
        String launchId = launchesApiSteps.createLaunchAndGetId(launchName);
        launchesApiSteps.finishLaunch(launchId);
        launchesApiSteps.deleteLaunch(launchId);
    }

    @Test(description = "Read launch by id via API")
    public void readLaunchTest() {
        String launchName = "api-read-" + UUID.randomUUID();
        String launchId = launchesApiSteps.createLaunchAndGetId(launchName);
        launchesApiSteps.verifyLaunchExists(launchId);
        launchesApiSteps.finishLaunch(launchId);
        launchesApiSteps.deleteLaunch(launchId);
    }

    @Test(description = "Update launch via API")
    public void updateLaunchTest() {
        String launchName = "api-update-" + UUID.randomUUID();
        String launchId = launchesApiSteps.createLaunchAndGetId(launchName);
        launchesApiSteps.updateLaunchDescription(launchId, "Updated from API test");
        launchesApiSteps.finishLaunch(launchId);
        launchesApiSteps.deleteLaunch(launchId);
    }

    @Test(description = "Delete launch via API")
    public void deleteLaunchTest() {
        String launchName = "api-delete-" + UUID.randomUUID();
        String launchId = launchesApiSteps.createLaunchAndGetId(launchName);
        launchesApiSteps.finishLaunch(launchId);
        launchesApiSteps.deleteLaunch(launchId);
    }

    @Test(description = "Get launches list with invalid token")
    public void getLaunchesWithInvalidTokenTest() {
        launchesApiSteps.verifyGetLaunchesWithInvalidToken();
    }

    @Test(description = "Get launches list with invalid project name")
    public void getLaunchesWithInvalidProjectNameTest() {
        launchesApiSteps.verifyGetLaunchesWithInvalidProjectName();
    }
}
