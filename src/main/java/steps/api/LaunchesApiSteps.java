package steps.api;

import api.launches.LaunchesApi;
import io.qameta.allure.Allure;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class LaunchesApiSteps {

    private static final Logger logger = LogManager.getLogger(LaunchesApiSteps.class);
    private final LaunchesApi launchesApi = new LaunchesApi();
    private final Map<String, String> launchIdToUuid = new HashMap<>();

    public String createLaunchAndGetId(String launchName) {
        return Allure.step("Create launch and resolve numeric id: " + launchName, () -> {
            Response response = launchesApi.createLaunch(launchName);
            String launchUuid = response.jsonPath().getString("id");

            Response launchByUuidResponse = launchesApi.getLaunchByUuid(launchUuid);

            String launchId = launchByUuidResponse.jsonPath().getString("id");
            launchIdToUuid.put(launchId, launchUuid);
            logger.info("Launch created. uuid: {}, id: {}", launchUuid, launchId);
            return launchId;
        });
    }

    public Response getLaunchById(String launchId) {
        return Allure.step("Get launch by id: " + launchId, () -> launchesApi.getLaunchById(launchId));
    }

    public Response updateLaunchDescription(String launchId, String description) {
        return Allure.step("Update launch description: id=" + launchId, () ->
                launchesApi.updateLaunch(launchId, description));
    }

    public Response finishLaunch(String launchId) {
        return Allure.step("Finish launch: id=" + launchId, () -> {
            String launchUuid = launchIdToUuid.get(launchId);
            if (launchUuid == null) {
                throw new IllegalStateException("Launch uuid not found for id: " + launchId);
            }
            return launchesApi.finishLaunch(launchUuid);
        });
    }

    public Response deleteLaunch(String launchId) {
        return Allure.step("Delete launch: id=" + launchId, () -> launchesApi.deleteLaunch(launchId));
    }

    public Response addAttributeToLaunch(String launchId, String key, String value) {
        return Allure.step("Add attribute to launch: id=" + launchId + ", " + key + "=" + value, () ->
                launchesApi.updateLaunchAttributes(launchId, key, value));
    }

    public String createAndFinishLaunch(String launchName) {
        return Allure.step("Create and finish launch: " + launchName, () -> {
            String launchId = createLaunchAndGetId(launchName);
            finishLaunch(launchId);
            return launchId;
        });
    }

    public Response getLaunchesWithInvalidToken(String token) {
        return Allure.step("Get launches with invalid token", () ->
                launchesApi.getLaunchesWithInvalidToken(token));
    }

    public Response getLaunchesByProject(String projectName) {
        return Allure.step("Get launches for project: " + projectName, () ->
                launchesApi.getLaunchesByProject(projectName));
    }
}
