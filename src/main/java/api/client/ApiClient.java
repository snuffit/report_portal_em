package api.client;

import config.ApiConfig;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static io.restassured.RestAssured.given;

public class ApiClient {

    private static final Logger logger = LogManager.getLogger(ApiClient.class);

    protected RequestSpecification baseRequest() {
        return given()
                .baseUri(ApiConfig.getBaseUrl())
                .basePath(ApiConfig.getApiPrefix())
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .header("Authorization", "Bearer " + ApiConfig.getToken());
    }

    protected Response get(String path) {
        logger.info("GET {}", path);
        return baseRequest()
                .when()
                .get(path)
                .then()
                .extract()
                .response();
    }

    protected Response post(String path, Object body) {
        logger.info("POST {}", path);
        return baseRequest()
                .body(body)
                .when()
                .post(path)
                .then()
                .extract()
                .response();
    }

    protected Response put(String path, Object body) {
        logger.info("PUT {}", path);
        return baseRequest()
                .body(body)
                .when()
                .put(path)
                .then()
                .extract()
                .response();
    }

    protected Response delete(String path) {
        logger.info("DELETE {}", path);
        return baseRequest()
                .when()
                .delete(path)
                .then()
                .extract()
                .response();
    }

    protected Response getWithCustomToken(String path, String token) {
        logger.info("GET {} (custom token)", path);
        return given()
                .baseUri(ApiConfig.getBaseUrl())
                .basePath(ApiConfig.getApiPrefix())
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .when()
                .get(path)
                .then()
                .extract()
                .response();
    }
}
