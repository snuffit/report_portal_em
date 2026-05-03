package tests.api;

import io.qameta.allure.Allure;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import tests.BaseTest;

import java.util.UUID;

@Test(groups = "API")
public class LaunchesCrudApiTest extends BaseTest {

    @Test(description = "Create launch via API")
    public void createLaunchTest() {
        String launchName = "api-create-" + UUID.randomUUID();
        String launchId = launchesApiSteps.createLaunchAndGetId(launchName);
        Response finishResponse = launchesApiSteps.finishLaunch(launchId);
        Response deleteResponse = launchesApiSteps.deleteLaunch(launchId);
        Response launchAfterDeleteResponse = launchesApiSteps.getLaunchById(launchId);

        int statusAfterDelete = launchAfterDeleteResponse.getStatusCode();
        boolean createdAndDeletedCorrectly = finishResponse.getStatusCode() == 200
                && deleteResponse.getStatusCode() == 200
                && (statusAfterDelete == 404 || statusAfterDelete == 410);
        Allure.step("Assertion: launch finished, deleted, absent on subsequent GET", () ->
                Assert.assertTrue(createdAndDeletedCorrectly,
                        "Expected finish=200, delete=200 and launch absence=404 after deletion"));
    }

    @Test(description = "Read launch by id via API")
    public void readLaunchTest() {
        String launchName = "api-read-" + UUID.randomUUID();
        String launchId = launchesApiSteps.createLaunchAndGetId(launchName);
        Response getResponse = launchesApiSteps.getLaunchById(launchId);
        launchesApiSteps.finishLaunch(launchId);
        launchesApiSteps.deleteLaunch(launchId);

        boolean launchIsReadable = getResponse.getStatusCode() == 200
                && launchId.equals(getResponse.jsonPath().getString("id"));
        Allure.step("Assertion: launch readable by id after creation", () ->
                Assert.assertTrue(launchIsReadable, "Launch should be available by id right after creation"));
    }

    @Test(description = "Update launch via API")
    public void updateLaunchTest() {
        String launchName = "api-update-" + UUID.randomUUID();
        String description = "Updated from API test";
        String launchId = launchesApiSteps.createLaunchAndGetId(launchName);
        Response updateResponse = launchesApiSteps.updateLaunchDescription(launchId, description);
        Response getResponse = launchesApiSteps.getLaunchById(launchId);
        launchesApiSteps.finishLaunch(launchId);
        launchesApiSteps.deleteLaunch(launchId);

        boolean launchUpdated = updateResponse.getStatusCode() == 200
                && description.equals(getResponse.jsonPath().getString("description"));
        Allure.step("Assertion: description updated via API", () ->
                Assert.assertTrue(launchUpdated, "Launch description should be updated via API"));
    }

    @Test(description = "Delete launch via API")
    public void deleteLaunchTest() {
        String launchName = "api-delete-" + UUID.randomUUID();
        String launchId = launchesApiSteps.createLaunchAndGetId(launchName);
        Response finishResponse = launchesApiSteps.finishLaunch(launchId);
        Response deleteResponse = launchesApiSteps.deleteLaunch(launchId);
        Response launchAfterDeleteResponse = launchesApiSteps.getLaunchById(launchId);

        int statusAfterDelete = launchAfterDeleteResponse.getStatusCode();
        boolean launchDeleted = finishResponse.getStatusCode() == 200
                && deleteResponse.getStatusCode() == 200
                && (statusAfterDelete == 404 || statusAfterDelete == 410);
        Allure.step("Assertion: deleted launch not returned by GET", () ->
                Assert.assertTrue(launchDeleted, "Deleted launch should be unavailable on next read"));
    }

    @Test(description = "Get launches list with invalid token")
    public void getLaunchesWithInvalidTokenTest() {
        Response response = launchesApiSteps.getLaunchesWithInvalidToken("invalid_token_value");
        String errorMessage = response.getBody().asString().toLowerCase();

        boolean invalidTokenHandled = response.getStatusCode() == 401
                && (errorMessage.contains("error") || errorMessage.contains("unauthorized"));
        Allure.step("Assertion: invalid token yields 401 and error payload", () ->
                Assert.assertTrue(invalidTokenHandled, "Invalid token request should return 401 with error message"));
    }

    @Test(description = "Get launches list with invalid project name")
    public void getLaunchesWithInvalidProjectNameTest() {
        Response response = launchesApiSteps.getLaunchesByProject("invalid_project_name");
        int status = response.getStatusCode();
        String errorMessage = response.getBody().asString().toLowerCase();

        boolean invalidProjectHandled = (status == 403 || status == 404)
                && (errorMessage.contains("error")
                || errorMessage.contains("not found")
                || errorMessage.contains("permissions"));
        Allure.step("Assertion: invalid project yields 403/404 and explanatory body", () ->
                Assert.assertTrue(invalidProjectHandled,
                        "Invalid project request should return 403/404 with access error in response body"));
    }
}
