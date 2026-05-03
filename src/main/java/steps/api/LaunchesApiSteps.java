package steps.api;

import api.launches.LaunchesApi;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LaunchesApiSteps {

    private static final Logger logger = LogManager.getLogger(LaunchesApiSteps.class);
    private final LaunchesApi launchesApi = new LaunchesApi();
    private final Map<String, String> launchIdToUuid = new HashMap<>();

    public String createLaunchAndGetId(String launchName) {
        Response response = launchesApi.createLaunch(launchName);
        String launchUuid = response.jsonPath().getString("id");

        Response launchByUuidResponse = launchesApi.getLaunchByUuid(launchUuid);

        String launchId = launchByUuidResponse.jsonPath().getString("id");
        launchIdToUuid.put(launchId, launchUuid);
        logger.info("Launch created. uuid: {}, id: {}", launchUuid, launchId);
        return launchId;
    }

    public Response createLaunch(String launchName) {
        return launchesApi.createLaunch(launchName);
    }

    public Response getLaunchById(String launchId) {
        return launchesApi.getLaunchById(launchId);
    }

    public Response getLaunchByUuid(String launchUuid) {
        return launchesApi.getLaunchByUuid(launchUuid);
    }

    public Response updateLaunchDescription(String launchId, String description) {
        return launchesApi.updateLaunch(launchId, description);
    }

    public Response finishLaunch(String launchId) {
        String launchUuid = launchIdToUuid.get(launchId);
        if (launchUuid == null) {
            throw new IllegalStateException("Launch uuid not found for id: " + launchId);
        }
        return launchesApi.finishLaunch(launchUuid);
    }

    public Response deleteLaunch(String launchId) {
        return launchesApi.deleteLaunch(launchId);
    }

    public Response addAttributeToLaunch(String launchId, String key, String value) {
        return launchesApi.updateLaunchAttributes(launchId, key, value);
    }

    public String createAndFinishLaunch(String launchName) {
        String launchId = createLaunchAndGetId(launchName);
        finishLaunch(launchId);
        return launchId;
    }

    public String getLatestLaunchName() {
        Response response = launchesApi.getLaunches();
        List<String> names = response.jsonPath().getList("content.name");
        if (names == null || names.isEmpty()) {
            names = response.jsonPath().getList("launches.name");
        }
        if (names == null || names.isEmpty()) {
            return null;
        }
        return names.get(0);
    }

    public Response getLaunches() {
        return launchesApi.getLaunches();
    }

    public Response getLaunchesWithInvalidToken(String token) {
        return launchesApi.getLaunchesWithInvalidToken(token);
    }

    public Response getLaunchesByProject(String projectName) {
        return launchesApi.getLaunchesByProject(projectName);
    }
}
