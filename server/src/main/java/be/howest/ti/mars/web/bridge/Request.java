package be.howest.ti.mars.web.bridge;

import be.howest.ti.mars.logic.domain.Constants;
import be.howest.ti.mars.logic.domain.ForumPost;
import be.howest.ti.mars.logic.domain.Garden;
import be.howest.ti.mars.web.exceptions.MalformedRequestException;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.validation.RequestParameters;
import io.vertx.ext.web.validation.ValidationHandler;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The Request class is responsible for translating information that is part of the
 * request into Java.
 *
 * For every piece of information that you need from the request, you should provide a method here.
 * You can find information in:
 * - the request path: params.pathParameter("some-param-name")
 * - the query-string: params.queryParameter("some-param-name")
 * Both return a `RequestParameter`, which can contain a string or an integer in our case.
 * The actual data can be retrieved using `getInteger()` or `getString()`, respectively.
 * You can check if it is an integer (or not) using `isNumber()`.
 *
 * Finally, some requests have a body. If present, the body will always be in the json format.
 * You can acces this body using: `params.body().getJsonObject()`.
 *
 * **TIP:** Make sure that al your methods have a unique name. For instance, there is a request
 * that consists of more than one "player name". You cannot use the method `getPlayerName()` for both,
 * you will need a second one with a different name.
 */
public class Request {
    private static final Logger LOGGER = Logger.getLogger(Request.class.getName());
    public static final String SPEC_PLANT_ID = "plantId";
    public static final String SPEC_PLANT = "plant";
    private static final String SPEC_NAME = "name";
    private static final String SPEC_PLANT_IMG_LINK = "imgLink";
    public static final String UNABLE_TO_DECIPHER_THE_DATA_IN_THE_BODY = "Unable to decipher the data in the body";
    public static final String UNABLE_TO_DECIPHER_CHECK_LOGS = "Unable to decipher the data in the request body. See logs for details.";
    private static final String SPEC_GARDEN_ID = "gardenId";
    private static final String SPEC_OWNEDPLANT_ID = "ownedPlantId";
    private static final String SPEC_FORUM_POST_ID = "forumId";
    private static final String SPEC_GARDEN_TIER = "tier";
    private static final String BODY_ISNT_A_JSON_OBJECT = "Body isn't a json object";
    private final RequestParameters params;
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    public static Request from(RoutingContext ctx) {
        return new Request(ctx);
    }

    private Request(RoutingContext ctx) {
        this.params = ctx.get(ValidationHandler.REQUEST_CONTEXT_KEY);
    }

    public int getPlantId() {
        return params.pathParameter("planId").getInteger();
    }

    public String getPlantName() {
        try {
            if (params.body().isJsonObject())
                return params.body().getJsonObject().getString(SPEC_NAME);
            return params.body().get().toString();
        } catch (IllegalArgumentException ex) {
            LOGGER.log(Level.INFO, UNABLE_TO_DECIPHER_THE_DATA_IN_THE_BODY, ex);
            throw new MalformedRequestException(UNABLE_TO_DECIPHER_CHECK_LOGS);
        }
    }

    public String getPlantLink() {
        try {
            if (params.body().isJsonObject())
                return params.body().getJsonObject().getString(SPEC_PLANT_IMG_LINK);
            return params.body().get().toString();
        } catch (IllegalArgumentException ex) {
            LOGGER.log(Level.INFO, UNABLE_TO_DECIPHER_THE_DATA_IN_THE_BODY, ex);
            throw new MalformedRequestException(UNABLE_TO_DECIPHER_CHECK_LOGS);
        }
    }

    public int getGardenId() {
        return params.pathParameter(SPEC_GARDEN_ID).getInteger();
    }

    public int getOwnedPlantId() {
        try {
            if(params.body().isJsonObject())
                return params.body().getJsonObject().getInteger(SPEC_OWNEDPLANT_ID);
            throw new IllegalArgumentException(BODY_ISNT_A_JSON_OBJECT);
        } catch (IllegalArgumentException ex) {
            LOGGER.log(Level.INFO, UNABLE_TO_DECIPHER_THE_DATA_IN_THE_BODY, ex);
            throw new MalformedRequestException(UNABLE_TO_DECIPHER_CHECK_LOGS);
        }
    }
    
    public int getForumPostId() {
        return params.pathParameter(SPEC_FORUM_POST_ID).getInteger();
    }

    public ForumPost getForumPost() {
        try {
            if (params.body().isJsonObject())
                return jsonToForumPost(params);
            return null;
        } catch (IllegalArgumentException ex) {
            LOGGER.log(Level.INFO, UNABLE_TO_DECIPHER_THE_DATA_IN_THE_BODY, ex);
            throw new MalformedRequestException(UNABLE_TO_DECIPHER_THE_DATA_IN_THE_BODY + " See logs for details.");
        }
    }

    private ForumPost jsonToForumPost(RequestParameters params) {
        JsonObject json = params.body().getJsonObject();

        int garden = json.getInteger(SPEC_GARDEN_ID);
        String text = json.getString("text");
        String imgLink = json.getString(SPEC_PLANT_IMG_LINK);
        Date uploadTime = new Date();

        return new ForumPost(new Garden(garden), text, imgLink, uploadTime, 0, new ArrayList<>());
    }

    public JsonObject getCommentBody() {
        try {
            if (params.body().isJsonObject())
                return params.body().getJsonObject();
            return null;
        } catch (IllegalArgumentException ex) {
            LOGGER.log(Level.INFO, UNABLE_TO_DECIPHER_THE_DATA_IN_THE_BODY, ex);
            throw new MalformedRequestException(UNABLE_TO_DECIPHER_THE_DATA_IN_THE_BODY + " See logs for details.");
        }
    }

    public Constants.Tier getGardenTier() {
        try {
            if(params.body().isJsonObject())
                return Constants.Tier.valueOf(params.body().getJsonObject().getString(SPEC_GARDEN_TIER));
            throw new IllegalArgumentException(BODY_ISNT_A_JSON_OBJECT);
        } catch (IllegalArgumentException ex) {
            LOGGER.log(Level.INFO, UNABLE_TO_DECIPHER_THE_DATA_IN_THE_BODY, ex);
            throw new MalformedRequestException(UNABLE_TO_DECIPHER_CHECK_LOGS);
        }
    }

    public int getPlantIdFromBody() {
        try {
            if(params.body().isJsonObject())
                return params.body().getJsonObject().getInteger(SPEC_PLANT_ID);
            throw new IllegalArgumentException(BODY_ISNT_A_JSON_OBJECT);
        } catch (IllegalArgumentException ex) {
            LOGGER.log(Level.INFO, UNABLE_TO_DECIPHER_THE_DATA_IN_THE_BODY, ex);
            throw new MalformedRequestException(UNABLE_TO_DECIPHER_CHECK_LOGS);
        }
    }

    public String getGardenName() {
        try {
            if(params.body().isJsonObject())
                return params.body().getJsonObject().getString(SPEC_NAME);
            throw new IllegalArgumentException(BODY_ISNT_A_JSON_OBJECT);
        } catch (IllegalArgumentException ex) {
            LOGGER.log(Level.INFO, UNABLE_TO_DECIPHER_THE_DATA_IN_THE_BODY, ex);
            throw new MalformedRequestException(UNABLE_TO_DECIPHER_CHECK_LOGS);
        }
    }

    public JsonObject getRobotBody() {
        try {
            if (params.body().isJsonObject())
                return params.body().getJsonObject();
            return null;
        } catch (IllegalArgumentException ex) {
            LOGGER.log(Level.INFO, UNABLE_TO_DECIPHER_THE_DATA_IN_THE_BODY, ex);
            throw new MalformedRequestException(UNABLE_TO_DECIPHER_THE_DATA_IN_THE_BODY + " See logs for details.");
        }
    }
}
