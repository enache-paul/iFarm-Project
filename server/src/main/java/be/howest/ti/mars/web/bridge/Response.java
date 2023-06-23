package be.howest.ti.mars.web.bridge;

import be.howest.ti.mars.logic.data.MarsH2Repository;
import be.howest.ti.mars.logic.domain.*;
import be.howest.ti.mars.web.exceptions.ResponseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The Response class is responsible for translating the result of the controller into
 * JSON responses with an appropriate HTTP code.
 */
public class Response {
    private static final Logger LOGGER = Logger.getLogger(MarsH2Repository.class.getName());

    private Response() { }

    private static void sendOkJsonResponse(RoutingContext ctx, JsonObject response) {
        sendJsonResponse(ctx, 200, response);
    }

    private static void sendOkJsonResponse(RoutingContext ctx, JsonArray response) {
        sendJsonResponse(ctx, 200, response);
    }

    private static void sendJsonResponse(RoutingContext ctx, int statusCode, Object response) {
        ctx.response()
                .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .setStatusCode(statusCode)
                .end(Json.encodePrettily(response));
    }

    public static void sendFailure(RoutingContext ctx, int code, String quote) {
        sendJsonResponse(ctx, code, new JsonObject()
                .put("failure", code)
                .put("cause", quote));
    }

    public static void sendPlant(RoutingContext ctx, Plant plant) {
        sendOkJsonResponse(ctx, JsonObject.mapFrom(plant));
    }

    public static void sendPlantCreated(RoutingContext ctx, Plant plant) {
        sendJsonResponse(ctx, 201, JsonObject.mapFrom(plant));
    }

    public static void sendGarden(RoutingContext ctx, Garden garden) {
        sendOkJsonResponse(ctx, JsonObject.mapFrom(garden));
    }

    public static void sendGardenCreated(RoutingContext ctx, Garden garden) {
        sendJsonResponse(ctx, 201, JsonObject.mapFrom(garden));
    }

    public static void sendScanCreated(RoutingContext ctx, Scan scan) {
        sendJsonResponse(ctx, 201, JsonObject.mapFrom(scan));
    }

    public static void sendForumPost(RoutingContext ctx, ForumPost forumPost) {
        sendOkJsonResponse(ctx, JsonObject.mapFrom(forumPost));
    }

    public static void sendForumPosts(RoutingContext ctx, List<ForumPost> posts) {
        sendOkJsonResponse(ctx, fromPostsListToJsonArray(posts));
    }

    private static JsonArray fromPostsListToJsonArray(List<ForumPost> posts) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String arrayOfPostsToJson = objectMapper.writeValueAsString(posts);

            return new JsonArray(arrayOfPostsToJson);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "failed to create json array of posts", ex);
            throw new ResponseException("failed to create json array of posts");
        }
    }

    public static void sendForumPostCreated(RoutingContext ctx, ForumPost forumPost) {
        sendJsonResponse(ctx, 201, JsonObject.mapFrom(forumPost));
    }

    public static void sendPlantsList(RoutingContext ctx, List<Plant> plantsList) throws JsonProcessingException {
        sendOkJsonResponse(ctx, fromPlantsListToJsonArray(plantsList));
    }

    public static JsonArray fromPlantsListToJsonArray(List<Plant> plantList) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String arrayOfPlantsToJson = objectMapper.writeValueAsString(plantList);

        return new JsonArray(arrayOfPlantsToJson);
    }

    public static void sendGardenTierUpdated(RoutingContext ctx, Garden updateGardenTier) {
        sendOkJsonResponse(ctx, JsonObject.mapFrom(updateGardenTier));
    }

    public static void sendOwnedPlantCreated(RoutingContext ctx, OwnedPlant ownedPlant) {
        sendJsonResponse(ctx, 201, JsonObject.mapFrom(ownedPlant));
    }

    public static void sendCommentCreated(RoutingContext ctx, Comment comment) {
        sendJsonResponse(ctx, 201, JsonObject.mapFrom(comment));
    }

    public static void sendRobotCreated(RoutingContext ctx, Robot robot) {
        sendJsonResponse(ctx, 201, JsonObject.mapFrom(robot));
    }

    public static void sendHarvested(RoutingContext ctx) {
        sendJsonResponse(ctx, 204, new JsonObject());
    }
}
