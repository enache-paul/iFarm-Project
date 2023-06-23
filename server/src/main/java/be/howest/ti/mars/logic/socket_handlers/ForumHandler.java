package be.howest.ti.mars.logic.socket_handlers;

import be.howest.ti.mars.logic.controller.DefaultMarsController;
import be.howest.ti.mars.logic.domain.Constants;
import io.vertx.core.json.JsonObject;
import org.apache.commons.lang3.NotImplementedException;


public class ForumHandler {
    private static final ForumHandler instance = new ForumHandler();
    private ForumHandler() {}

    public static ForumHandler getInstance() {
        return instance;
    }

    public OutgoingEvent handleIncomingEvent(Event event) {
        OutgoingEvent outGoingEvent;
        switch (event.getType()) {
            case POST_LIKE:
                outGoingEvent = handleIncomingPostLike((PostLikeEvent)event);
                break;
            case COMMENT_LIKE:
                outGoingEvent = handleIncomingCommentLike((CommentLikeEvent)event);
                break;
            default:
                throw new NotImplementedException("not implemented");
        }

        return outGoingEvent;
    }

    private OutgoingEvent handleIncomingPostLike(PostLikeEvent event) {
        int receiverId = DefaultMarsController.getInstance().createPostLike(event.getGardenId(), event.getPostId());
        String gardenName = DefaultMarsController.getInstance().getBasicGarden(event.getGardenId()).getName();
        JsonObject body = new JsonObject();
        body.put("gardenName", gardenName);
        body.put("type", Constants.EventType.POST_LIKE);

        return new OutgoingEvent(event.getGardenId(), Constants.EventType.POST_LIKE, receiverId, body);
    }

    private OutgoingEvent handleIncomingCommentLike(CommentLikeEvent event) {
        int receiverId = DefaultMarsController.getInstance().createCommentLike(event.getGardenId(), event.getCommentId());

        String gardenName = DefaultMarsController.getInstance().getBasicGarden(event.getGardenId()).getName();
        JsonObject body = new JsonObject();
        body.put("type", Constants.EventType.COMMENT_LIKE);
        body.put("gardenName", gardenName);

        return new OutgoingEvent(event.getGardenId(), Constants.EventType.POST_LIKE, receiverId, body);
    }


}
