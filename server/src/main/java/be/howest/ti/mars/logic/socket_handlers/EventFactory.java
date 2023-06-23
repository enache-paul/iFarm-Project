package be.howest.ti.mars.logic.socket_handlers;

import be.howest.ti.mars.logic.domain.Constants;
import io.vertx.core.json.JsonObject;
import org.apache.commons.lang3.NotImplementedException;

public class EventFactory {
    private static final EventFactory instance = new EventFactory();
    private EventFactory() {}

    public static EventFactory getInstance() {
        return instance;
    }

    public Event createEvent(JsonObject body) {
        Event event;
        int gardenId = body.getInteger("gardenId");
        Constants.EventType type = Constants.EventType.valueOf(body.getString("type"));
        switch (type) {
            case POST_LIKE:
                event = new PostLikeEvent(gardenId, body.getInteger("postId"));
                break;
            case COMMENT_LIKE:
                event = new CommentLikeEvent(gardenId, body.getInteger("commentId"));
                break;
            default:
                throw new NotImplementedException("not implemented");
        }

        return event;
    }
}
