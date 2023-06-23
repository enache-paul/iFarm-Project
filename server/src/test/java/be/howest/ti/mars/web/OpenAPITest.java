package be.howest.ti.mars.web;

import be.howest.ti.mars.logic.controller.DefaultMarsController;
import be.howest.ti.mars.logic.controller.MockMarsController;
import be.howest.ti.mars.logic.data.Repositories;
import be.howest.ti.mars.web.bridge.MarsOpenApiBridge;
import be.howest.ti.mars.web.bridge.MarsRtcBridge;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.util.StringUtils;

import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(VertxExtension.class)
@SuppressWarnings({"PMD.JUnitTestsShouldIncludeAssert","PMD.AvoidDuplicateLiterals"})
/*
 * PMD.JUnitTestsShouldIncludeAssert: VertxExtension style asserts are marked as false positives.
 * PMD.AvoidDuplicateLiterals: Should all be part of the spec (e.g., urls and names of req/res body properties, ...)
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OpenAPITest {

    private static final Logger LOGGER = Logger.getLogger(MarsOpenApiBridge.class.getName());
    private static final int PORT = 8080;
    private static final String HOST = "localhost";
    public static final String MSG_200_EXPECTED = "If all goes right, we expect a 200 status";
    public static final String MSG_201_EXPECTED = "If a resource is successfully created.";
    public static final String MSG_204_EXPECTED = "If a resource is successfully deleted";
    private Vertx vertx;
    private WebClient webClient;

    @BeforeAll
    void deploy(final VertxTestContext testContext) {
        Repositories.shutdown();
        vertx = Vertx.vertx();

        WebServer webServer = new WebServer(new MarsOpenApiBridge(), new MarsRtcBridge());
        vertx.deployVerticle(
                webServer,
                testContext.succeedingThenComplete()
        );
        webClient = WebClient.create(vertx);
    }

    @AfterAll
    void close(final VertxTestContext testContext) {
        vertx.close(testContext.succeedingThenComplete());
        webClient.close();
        Repositories.shutdown();
    }

    @Test
    void getPlant(final VertxTestContext testContext) {
        webClient.get(PORT, HOST, "/api/plants/1").send()
                .onFailure(testContext::failNow)
                .onSuccess(response -> testContext.verify(() -> {
                    assertEquals(200, response.statusCode(), MSG_200_EXPECTED);
                    assertTrue(StringUtils.isNotBlank(response.bodyAsJsonObject().getString("name")));
                    assertTrue(StringUtils.isNotBlank(response.bodyAsJsonObject().getString("imgLink")), "No img found");

                    testContext.completeNow();
                }));
    }

    @Test
    void createOwnedPlant(final VertxTestContext testContext) {
        webClient.post(PORT, HOST, "/api/gardens/1/ownedPlants").sendJsonObject(createTestOwnedPlant())
                .onFailure(testContext::failNow)
                .onSuccess(response -> testContext.verify(() -> {
                    System.out.println(response.body());
                    assertEquals(201, response.statusCode(), MSG_201_EXPECTED);
                    assertEquals(
                            3,
                            response.bodyAsJsonObject().getInteger("id"),
                            "owned plant does not match");
                    testContext.completeNow();
                }));
    }

    @Test
    void createScan(final VertxTestContext testContext) {
        webClient.get(PORT, HOST, "/api/gardens/1").send();
        webClient.post(PORT, HOST, "/api/gardens/1/scans").sendJsonObject(createTestScan())
                .onFailure(testContext::failNow)
                .onSuccess(response -> testContext.verify(() -> {
                    System.out.println(response.body());
                    assertEquals(201, response.statusCode(), MSG_201_EXPECTED);
                    assertEquals(
                            2,
                            response.bodyAsJsonObject().getJsonObject("plant").getInteger("id"),
                            "scan does not match");
                    testContext.completeNow();
                }));
    }

    @Test
    void createForumPost(final VertxTestContext testContext) {
        int gardenId = 1;
        String uploadTime = "1200-01-02 00:00:00"; // THIS IS HARDCODED IN MockMarsController FIX ME!!!

        webClient.post(PORT, HOST, "/api/forumposts/").sendJsonObject(createForumPost(gardenId, uploadTime))
                .onFailure(testContext::failNow)
                .onSuccess(response -> testContext.verify(() -> {
                    assertEquals(201, response.statusCode(), MSG_201_EXPECTED);
                    assertEquals(
                            String.valueOf(gardenId),
                            response.bodyAsJsonObject().getJsonObject("garden").getString("id"),
                            "gardenId does not match " + gardenId);
                    testContext.completeNow();
                }));
    }

    @Test
    void getForumPosts(final VertxTestContext testContext) {
        webClient.get(PORT, HOST, "/api/forumposts").send()
                .onFailure(testContext::failNow)
                .onSuccess(response -> testContext.verify(() -> {
                    assertEquals(200, response.statusCode(), MSG_200_EXPECTED);
                    assertNotNull(response.bodyAsJsonArray());
                    testContext.completeNow();
                }));
    }

    private JsonObject createTestOwnedPlant() {
        return new JsonObject().put("plantId", 1);
    }

    private JsonObject createForumPost(int gardenId, String uploadTime) {
        JsonObject json = new JsonObject();

        json.put("gardenId", gardenId);
        json.put("text", "Post content");
        json.put("imgLink", "https://go.com");
        json.put("uploadTime", uploadTime);

        return json;
    }

    private JsonObject createTestScan() {
        return new JsonObject().put("ownedPlantId", 2);
    }

    @Test
    void getAllPlants(final VertxTestContext testContext) {
        webClient.get(PORT, HOST, "/api/plants").send()
                .onFailure(testContext::failNow)
                .onSuccess(response -> testContext.verify(() -> {
                    assertEquals(200, response.statusCode(), MSG_200_EXPECTED);
                    testContext.completeNow();
                }));
    }

    @Test
    void getGarden(@NotNull final VertxTestContext testContext) {
        webClient.get(PORT, HOST, "/api/gardens/1").send()
                .onFailure(testContext::failNow)
                .onSuccess(response -> testContext.verify(() -> {
                    assertEquals(200, response.statusCode(), MSG_200_EXPECTED);
                    testContext.completeNow();
                }));
    }

    @Test
    void createGarden(final VertxTestContext testContext) {
        webClient.post(PORT, HOST, "/api/gardens/").sendJsonObject(createGardenBody())
                .onFailure(testContext::failNow)
                .onSuccess(response -> testContext.verify(() -> {
                    assertEquals(201, response.statusCode(), MSG_201_EXPECTED);
                    testContext.completeNow();
                }));
    }

    private JsonObject createGardenBody() {
        JsonObject body = new JsonObject();
        body.put("name", "bobby");
        body.put("tier", "FREE");
        return body;
    }

    @Test
    void updateGardenTier(final VertxTestContext testContext) {
        String tier = "PREMIUM";

        webClient.put(PORT, HOST, "/api/gardens/1").sendJsonObject(createGardenTierBody(tier))
                .onFailure(testContext::failNow)
                .onSuccess(response -> testContext.verify(() -> {
                    assertEquals(200, response.statusCode(), MSG_200_EXPECTED);
                    assertEquals(
                            tier,
                            response.bodyAsJsonObject().getString("tier"),
                            "Tier does not match " + tier);
                    testContext.completeNow();
                }));
    }

    private JsonObject createGardenTierBody(String tier) {
        return new JsonObject().put("tier", tier);
    }

    @Test
    void createComment(final VertxTestContext testContext) {
        webClient.post(PORT, HOST, "/api/forumposts/comments").sendJsonObject(createCommentBody())
                .onFailure(testContext::failNow)
                .onSuccess(response -> testContext.verify(() -> {
                    assertEquals(201, response.statusCode(), MSG_201_EXPECTED);
                    testContext.completeNow();
                }));
    }

    private JsonObject createCommentBody() {
        JsonObject body = new JsonObject();
        body.put("postId", 1);
        body.put("gardenId", 1);
        body.put("text", "hallo");

        return body;
    }

    @Test
    void createRobot(final VertxTestContext testContext) {
        int gardenId = 1;
        webClient.post(PORT, HOST, "/api/robots/1").sendJsonObject(createRobotBody())
                .onFailure(testContext::failNow)
                .onSuccess(response -> testContext.verify(() -> {
                    assertEquals(201, response.statusCode(), MSG_201_EXPECTED);
                    assertEquals(
                            gardenId,
                            response.bodyAsJsonObject().getInteger("gardenId"),
                            "gardenId does not match " + gardenId);
                    testContext.completeNow();
                }));
    }

    private JsonObject createRobotBody() {
        JsonObject body = new JsonObject();
        body.put("robotName", "pretty robot");
        body.put("description", "this robot can rock your earthly existence");

        return body;
    }

    @Test
    void harvest(final VertxTestContext testContext) {
        webClient.put(PORT, HOST, "/api/gardens/1/ownedPlants").sendJsonObject(createHarvestBody())
                .onFailure(testContext::failNow)
                .onSuccess(response -> testContext.verify(() -> {
                    LOGGER.log(Level.SEVERE, response.bodyAsString());

                    assertEquals(204, response.statusCode(), MSG_204_EXPECTED);
                    testContext.completeNow();
                }));
    }

    private JsonObject createHarvestBody() {
        return new JsonObject().put("ownedPlantId", 1);
    }
}