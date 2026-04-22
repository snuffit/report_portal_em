package steps.api;

import api.launches.LaunchesApi;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LaunchesApiSteps {

    private static final Logger logger = LogManager.getLogger(LaunchesApiSteps.class);
    private final LaunchesApi launchesApi = new LaunchesApi();
    private final Map<String, String> launchIdToUuid = new HashMap<>();

    public String createLaunchAndGetId(String launchName) {
        Response response = launchesApi.createLaunch(launchName);
        assertStatus(response, 201, "Create launch");
        String launchUuid = response.jsonPath().getString("id");
        Assert.assertNotNull(launchUuid, "Launch uuid should not be null");

        Response launchByUuidResponse = launchesApi.getLaunchByUuid(launchUuid);
        assertStatus(launchByUuidResponse, 200, "Get launch by uuid");

        String launchId = launchByUuidResponse.jsonPath().getString("id");
        Assert.assertNotNull(launchId, "Launch id should not be null");
        launchIdToUuid.put(launchId, launchUuid);
        logger.info("Launch created. uuid: {}, id: {}", launchUuid, launchId);
        return launchId;
    }

    public void verifyLaunchExists(String launchId) {
        Response response = launchesApi.getLaunchById(launchId);
        assertStatus(response, 200, "Get launch by id");
        Assert.assertEquals(response.jsonPath().getString("id"), launchId,
                "Launch id should match requested id");
    }

    public void updateLaunchDescription(String launchId, String description) {
        Response response = launchesApi.updateLaunch(launchId, description);
        assertStatus(response, 200, "Update launch");
    }

    public void finishLaunch(String launchId) {
        String launchUuid = launchIdToUuid.get(launchId);
        Assert.assertNotNull(launchUuid, "Launch uuid not found for id: " + launchId);
        Response response = launchesApi.finishLaunch(launchUuid);
        assertStatus(response, 200, "Finish launch");
    }

    public void deleteLaunch(String launchId) {
        Response response = launchesApi.deleteLaunch(launchId);
        assertStatus(response, 200, "Delete launch");
    }

    public void addAttributeToLaunch(String launchId, String key, String value) {
        Response response = launchesApi.updateLaunchAttributes(launchId, key, value);
        assertStatus(response, 200, "Add launch attribute");
    }

    public String createAndFinishLaunch(String launchName) {
        String launchId = createLaunchAndGetId(launchName);
        finishLaunch(launchId);
        return launchId;
    }

    public String getLatestLaunchName() {
        Response response = launchesApi.getLaunches();
        assertStatus(response, 200, "Get launches list");
        List<String> names = response.jsonPath().getList("content.name");
        if (names == null || names.isEmpty()) {
            names = response.jsonPath().getList("launches.name");
        }
        Assert.assertNotNull(names, "Launch names list should not be null");
        Assert.assertFalse(names.isEmpty(), "Launches list should not be empty");
        return names.get(0);
    }

    public void verifyGetLaunchesWithInvalidToken() {
        Response response = launchesApi.getLaunchesWithInvalidToken("invalid_token_value");
        assertStatus(response, 401, "Get launches with invalid token");
        String errorMessage = response.getBody().asString().toLowerCase();
        Assert.assertTrue(errorMessage.contains("error") || errorMessage.contains("unauthorized"),
                "Error body should describe unauthorized access. Response body: " + response.getBody().asString());
    }

    public void verifyGetLaunchesWithInvalidProjectName() {
        Response response = launchesApi.getLaunchesByProject("invalid_project_name");
        int status = response.getStatusCode();
        Assert.assertTrue(status == 403 || status == 404,
                "Expected status 403 or 404 for invalid project, but was " + status + ". Response body: " + response.getBody().asString());
        String errorMessage = response.getBody().asString().toLowerCase();
        Assert.assertTrue(errorMessage.contains("error")
                        || errorMessage.contains("not found")
                        || errorMessage.contains("permissions"),
                "Error body should describe project access problem. Response body: " + response.getBody().asString());
    }

    private void assertStatus(Response response, int expectedStatus, String action) {
        int actualStatus = response.getStatusCode();
        String body = response.getBody().asString();
        logger.info("{} response status: {}", action, actualStatus);
        Assert.assertEquals(actualStatus, expectedStatus,
                action + " failed. Expected status " + expectedStatus + " but was " + actualStatus + ". Response body: " + body);
    }
}
