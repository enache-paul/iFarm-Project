package be.howest.ti.mars.logic.socket_handlers;

import be.howest.ti.mars.logic.controller.DefaultMarsController;
import io.vertx.core.json.JsonObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class EventFactoryTest {
    @Test
    void createEventPostLike() {
        // Arrange
        JsonObject body = new JsonObject();
        body.put("type", "POST_LIKE");
        body.put("postId", 2);
        body.put("gardenId", 2);

        // Act
        Event event = EventFactory.getInstance().createEvent(body);

        // Assert
        Assertions.assertAll(() -> parsePostLikeEvent(event));
        Assertions.assertEquals(2, parsePostLikeEvent(event).getPostId());
    }
    PostLikeEvent parsePostLikeEvent(Event event) {
        return (PostLikeEvent)event;
    }
}
