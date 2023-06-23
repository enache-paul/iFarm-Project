package be.howest.ti.mars.web.bridge;

import be.howest.ti.mars.logic.controller.DefaultMarsController;
import be.howest.ti.mars.logic.domain.*;
import be.howest.ti.mars.web.exceptions.MalformedRequestException;
import be.howest.ti.mars.web.exceptions.ResponseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.openapi.RouterBuilder;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * In the MarsOpenApiBridge class you will create one handler-method per API operation.
 * The job of the "bridge" is to bridge between JSON (request and response) and Java (the controller).
 * <p>
 * For each API operation you should get the required data from the `Request` class.
 * The Request class will turn the HTTP request data into the desired Java types (int, String, Custom class,...)
 * This desired type is then passed to the controller.
 * The return value of the controller is turned to Json or another Web data type in the `Response` class.
 */
public class MarsOpenApiBridge {
    private static final Logger LOGGER = Logger.getLogger(MarsOpenApiBridge.class.getName());

    public Router buildRouter(RouterBuilder routerBuilder) {
        LOGGER.log(Level.INFO, "Installing cors handlers");
        routerBuilder.rootHandler(createCorsHandler());

        LOGGER.log(Level.INFO, "Installing failure handlers for all operations");
        routerBuilder.operations().forEach(op -> op.failureHandler(this::onFailedRequest));

        LOGGER.log(Level.INFO, "Installing handler for: getPlant");
        routerBuilder.operation("getPlant").handler(this::getPlant);

        LOGGER.log(Level.INFO, "Installing handler for: createPlant");
        routerBuilder.operation("createPlant").handler(this::createPlant);

        LOGGER.log(Level.INFO, "Installing handler for: getGarden");
        routerBuilder.operation("getGarden").handler(this::getGarden);

        LOGGER.log(Level.INFO, "Installing handler for: createScan");
        routerBuilder.operation("createScan").handler(this::createScan);

        LOGGER.log(Level.INFO, "Installing handler for: getForumPost");
        routerBuilder.operation("getForumPost").handler(this::getForumPost);

        LOGGER.log(Level.INFO, "Installing handler for: getForumPosts");
        routerBuilder.operation("getForumPosts").handler(this::getForumPosts);

        LOGGER.log(Level.INFO, "Installing handler for: createForumPost");
        routerBuilder.operation("createForumPost").handler(this::createForumPost);

        LOGGER.log(Level.INFO, "Installing handler for: getAllPlants");
        routerBuilder.operation("getAllPlants").handler(this::getAllPlants);

        LOGGER.log(Level.INFO, "Installing handler for: updateGarden");
        routerBuilder.operation("updateGarden").handler(this::updateGarden);

        LOGGER.log(Level.INFO, "Installing handler for: createGarden");
        routerBuilder.operation("createGarden").handler(this::createGarden);

        LOGGER.log(Level.INFO, "Installing handler for: addPlantToGarden");
        routerBuilder.operation("addPlantToGarden").handler(this::createOwnedPlant);

        LOGGER.log(Level.INFO, "Installing handler for: createComment");
        routerBuilder.operation("createComment").handler(this::createComment);

        LOGGER.log(Level.INFO, "Installing handler for: addRobotToGarden");
        routerBuilder.operation("addRobotToGarden").handler(this::addRobotToGarden);

        LOGGER.log(Level.INFO, "Installing handler for: harvest");
        routerBuilder.operation("harvest").handler(this::harvest);

        LOGGER.log(Level.INFO, "All handlers are installed, creating router.");
        return routerBuilder.createRouter();
    }

    private void onFailedRequest(RoutingContext ctx) {
        Throwable cause = ctx.failure();
        int code = ctx.statusCode();
        String quote = Objects.isNull(cause) ? "" + code : cause.getMessage();

        // Map custom runtime exceptions to a HTTP status code.
        LOGGER.log(Level.INFO, "Failed request", cause);
        if (cause instanceof IllegalArgumentException) {
            code = 400;
        } else if (cause instanceof MalformedRequestException) {
            code = 400;
        } else if (cause instanceof NoSuchElementException) {
            code = 404;
        } else {
            LOGGER.log(Level.WARNING, "Failed request", cause);
        }

        Response.sendFailure(ctx, code, quote);
    }

    private CorsHandler createCorsHandler() {
        return CorsHandler.create(".*.")
                .allowedHeader("x-requested-with")
                .allowedHeader("Access-Control-Allow-Origin")
                .allowedHeader("Access-Control-Allow-Credentials")
                .allowCredentials(true)
                .allowedHeader("origin")
                .allowedHeader("Content-Type")
                .allowedHeader("Authorization")
                .allowedHeader("accept")
                .allowedMethod(HttpMethod.HEAD)
                .allowedMethod(HttpMethod.GET)
                .allowedMethod(HttpMethod.POST)
                .allowedMethod(HttpMethod.OPTIONS)
                .allowedMethod(HttpMethod.PATCH)
                .allowedMethod(HttpMethod.DELETE)
                .allowedMethod(HttpMethod.PUT);
    }

    public void getPlant(RoutingContext ctx) {
        Plant plant = Request.from(ctx).getPlantId();

        Response.sendPlant(ctx, plant);
    }

    public void createPlant(RoutingContext ctx) {
        String platName = Request.from(ctx).getPlantName();
        String imgLink = Request.from(ctx).getPlantLink();

        Response.sendPlantCreated(ctx, DefaultMarsController.getInstance().createPlant(platName, imgLink));
    }

    public void getGarden(RoutingContext ctx) {
        Garden garden = DefaultMarsController.getInstance().getGarden(Request.from(ctx).getGardenId());

        Response.sendGarden(ctx, garden);
    }

    private void createGarden(RoutingContext ctx) {
        String name = Request.from(ctx).getGardenName();
        Constants.Tier tier = Request.from(ctx).getGardenTier();

        Garden garden = DefaultMarsController.getInstance().createGarden(name, tier);
        Response.sendGardenCreated(ctx, garden);
    }

    private void createScan(RoutingContext ctx) {
        int gardenId = Request.from(ctx).getGardenId();
        int ownedPlantId = Request.from(ctx).getOwnedPlantId();

        Response.sendScanCreated(ctx, DefaultMarsController.getInstance().createScan(gardenId, ownedPlantId));
    }

    private void getForumPost(RoutingContext ctx) {
        ForumPost forumPost = DefaultMarsController.getInstance().getForumPost(Request.from(ctx).getForumPostId());

        Response.sendForumPost(ctx, forumPost);
    }

    private void getForumPosts(RoutingContext ctx) {
        List<ForumPost> posts = DefaultMarsController.getInstance().getRecentForumPosts();
        Response.sendForumPosts(ctx, posts);
    }

    public void createForumPost(RoutingContext ctx) {
        ForumPost forumPost = Request.from(ctx).getForumPost();

        Response.sendForumPostCreated(ctx, DefaultMarsController.getInstance().createForumPost(
                forumPost.getGarden().getId(),
                forumPost.getText(),
                forumPost.getImgLink()
        ));
    }

    public void getAllPlants(RoutingContext ctx) {
        List<Plant> plantsList = DefaultMarsController.getInstance().getAllPlants();
        List<Game> gameList = getAllgames();


        try {
            Response.sendPlantsList(ctx, plantsList);
        } catch (JsonProcessingException e) {
            throw new ResponseException("failed to send plants");
        }
    }

    public void updateGarden(RoutingContext ctx) {
        Request request = Request.from(ctx);

        int gardenId = request.getGardenId();
        Constants.Tier tier = request.getGardenTier();

        Response.sendGardenTierUpdated(ctx, DefaultMarsController.getInstance().updateGardenTier(gardenId, tier));
    }

    public void createOwnedPlant(RoutingContext ctx) {
        Request request = Request.from(ctx);

        int gardenId = request.getGardenId();
        int plantId = request.getPlantIdFromBody();

        Response.sendOwnedPlantCreated(ctx, DefaultMarsController.getInstance().createOwnedPlant(plantId, gardenId, Constants.Status.FUTURE));
    }

    public void createComment(RoutingContext ctx) {
        Request request = Request.from(ctx);

        JsonObject json = request.getCommentBody();
        int postId = json.getInteger("postId");
        int gardenId = json.getInteger("gardenId");
        String text = json.getString("text");

        Response.sendCommentCreated(ctx, DefaultMarsController.getInstance().createComment(postId, gardenId, text));
    }

    private void addRobotToGarden(RoutingContext ctx) {
        JsonObject json = Request.from(ctx).getRobotBody();

        String name = json.getString("robotName");
        int gardenId = Request.from(ctx).getGardenId();
        String description = json.getString("description");

        Response.sendRobotCreated(ctx, DefaultMarsController.getInstance().createRobot(name, gardenId, description));
    }

    private void harvest(RoutingContext ctx) {
        Request request = Request.from(ctx);
        int ownedPlantId = request.getOwnedPlantId();
        int gardenId = request.getGardenId();
        DefaultMarsController.getInstance().harvest(ownedPlantId, gardenId);

        Response.sendHarvested(ctx);
    }
}
