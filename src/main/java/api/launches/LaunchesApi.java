package api.launches;

import api.client.ApiClient;
import config.ApiConfig;
import io.restassured.response.Response;

import java.util.List;
import java.util.Map;

public class LaunchesApi extends ApiClient {

    private static final String PATH_LAUNCH = "/%s/launch";
    private static final String PATH_LAUNCH_BY_ID = "/%s/launch/%s";
    private static final String PATH_LAUNCH_BY_UUID = "/%s/launch/uuid/%s";
    private static final String PATH_LAUNCH_UPDATE = "/%s/launch/%s/update";
    private static final String PATH_LAUNCH_FINISH = "/%s/launch/%s/finish";

    private static final String FIELD_NAME = "name";
    private static final String FIELD_START_TIME = "startTime";
    private static final String FIELD_MODE = "mode";
    private static final String FIELD_DESCRIPTION = "description";
    private static final String FIELD_ATTRIBUTES = "attributes";
    private static final String FIELD_KEY = "key";
    private static final String FIELD_VALUE = "value";
    private static final String FIELD_END_TIME = "endTime";

    private static final String DEFAULT_MODE = "DEFAULT";
    private static final String DEFAULT_CREATE_DESCRIPTION = "Created by API automated test";
    private static final String DEFAULT_UPDATE_DESCRIPTION = "Updated attributes from API test";
    private static final String ATTRIBUTE_TEST_KEY = "test";
    private static final String ATTRIBUTE_UPDATED_VALUE = "updated";

    private final String project = ApiConfig.getProjectName();

    public Response getLaunches() {
        return get(String.format(PATH_LAUNCH, project));
    }

    public Response getLaunchesWithInvalidToken(String invalidToken) {
        return getWithCustomToken(String.format(PATH_LAUNCH, project), invalidToken);
    }

    public Response getLaunchesByProject(String projectName) {
        return get(String.format(PATH_LAUNCH, projectName));
    }

    public Response getLaunchById(String launchId) {
        return get(String.format(PATH_LAUNCH_BY_ID, project, launchId));
    }

    public Response getLaunchByUuid(String launchUuid) {
        return get(String.format(PATH_LAUNCH_BY_UUID, project, launchUuid));
    }

    public Response createLaunch(String launchName) {
        Map<String, Object> payload = Map.of(
                FIELD_NAME, launchName,
                FIELD_START_TIME, String.valueOf(System.currentTimeMillis()),
                FIELD_MODE, DEFAULT_MODE,
                FIELD_DESCRIPTION, DEFAULT_CREATE_DESCRIPTION
        );
        return post(String.format(PATH_LAUNCH, project), payload);
    }

    public Response updateLaunch(String launchId, String description) {
        Map<String, Object> payload = Map.of(
                FIELD_DESCRIPTION, description,
                FIELD_ATTRIBUTES, List.of(Map.of(FIELD_KEY, ATTRIBUTE_TEST_KEY, FIELD_VALUE, ATTRIBUTE_UPDATED_VALUE))
        );
        return put(String.format(PATH_LAUNCH_UPDATE, project, launchId), payload);
    }

    public Response updateLaunchAttributes(String launchId, String key, String value) {
        Map<String, Object> payload = Map.of(
                FIELD_DESCRIPTION, DEFAULT_UPDATE_DESCRIPTION,
                FIELD_ATTRIBUTES, List.of(Map.of(FIELD_KEY, key, FIELD_VALUE, value))
        );
        return put(String.format(PATH_LAUNCH_UPDATE, project, launchId), payload);
    }

    public Response finishLaunch(String launchUuid) {
        Map<String, Object> payload = Map.of(FIELD_END_TIME, String.valueOf(System.currentTimeMillis()));
        return put(String.format(PATH_LAUNCH_FINISH, project, launchUuid), payload);
    }

    public Response deleteLaunch(String launchId) {
        return delete(String.format(PATH_LAUNCH_BY_ID, project, launchId));
    }
}