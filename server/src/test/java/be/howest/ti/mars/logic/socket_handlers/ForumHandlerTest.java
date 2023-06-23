package be.howest.ti.mars.logic.socket_handlers;

import be.howest.ti.mars.logic.data.Repositories;
import io.vertx.core.json.JsonObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class ForumHandlerTest {
    @BeforeAll
    static void setupTestSuite() {
        Repositories.shutdown();
        JsonObject dbProperties = new JsonObject(Map.of("url", "jdbc:h2:./db-23",
                "username", "",
                "password", "",
                "webconsole.port", 9000));
        Repositories.configure(dbProperties);
    }

    @BeforeEach
    void setupTest() {
        Repositories.getH2Repo().generateData();
    }

    @Test
    void handleIncomingPostLike() {
        // Arrange
        PostLikeEvent event = new PostLikeEvent(2, 2);

        // Act
        OutgoingEvent outgoingEvent = ForumHandler.getInstance().handleIncomingEvent(event);

        // Assert
        Assertions.assertEquals("POST_LIKE", outgoingEvent.getBody().getString("type"));
    }

    @Test
    void handleIncomingCommentLike() {
        // Arrange
        CommentLikeEvent event = new CommentLikeEvent(2, 2);

        // Act
        OutgoingEvent outgoingEvent = ForumHandler.getInstance().handleIncomingEvent(event);

        // Assert
        Assertions.assertEquals("COMMENT_LIKE", outgoingEvent.getBody().getString("type"));
    }
}
