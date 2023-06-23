package be.howest.ti.mars.logic.socket_handlers;

import be.howest.ti.mars.logic.domain.Constants;
import io.vertx.core.json.JsonObject;

public class OutgoingEvent extends Event{
    private final int receiverId;
    private final JsonObject body;

    protected OutgoingEvent(int gardenId, Constants.EventType type, int receiverId, JsonObject body) {
        super(gardenId, type);
        this.receiverId = receiverId;
        this.body = body;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public JsonObject getBody() {
        return body;
    }
}
