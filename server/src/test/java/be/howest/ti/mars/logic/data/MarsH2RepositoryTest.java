package be.howest.ti.mars.logic.data;

import be.howest.ti.mars.logic.controller.MockMarsController;
import be.howest.ti.mars.logic.domain.*;
import be.howest.ti.mars.logic.exceptions.RepositoryException;
import io.netty.util.internal.StringUtil;
import io.vertx.core.json.JsonObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MarsH2RepositoryTest {
    private static final String URL = "jdbc:h2:./db-23";

    @BeforeEach
    void setupTestSuite() {
        Repositories.shutdown();
        JsonObject dbProperties = new JsonObject(Map.of("url",URL,
                "username", "",
                "password", "",
                "webconsole.port", 9000 ));
        Repositories.configure(dbProperties);
    }

    @Test
    void getPlant() {
        // Arrange
        int id = 1;
        // Act
        Plant plant = Repositories.getH2Repo().getPlant(id);
        // Assert
        Assertions.assertTrue(plant != null && !StringUtil.isNullOrEmpty(plant.getName()));
        Assertions.assertFalse(StringUtil.isNullOrEmpty(plant.getImgLink()));
    }

    @Test
    void insertPlant() {
        // Arrange
        String plantName = "Pepene";
        String imgLink = "https://pepeni-dulci.com";

        // Act
        Plant plant = Repositories.getH2Repo().insertPlant(plantName, imgLink);

        // Assert
        Assertions.assertNotNull(plant);
        Assertions.assertEquals(plantName, plant.getName());
    }

    @Test
    void insertPlantWrongName() {
        // Arrange
        String plantName = "";
        String imgLink = "https://pepeni-dulci.com";

        // Act
        Plant plant = Repositories.getH2Repo().insertPlant(plantName, imgLink);

        // Assert
        Assertions.assertNotNull(plant);
        Assertions.assertEquals(plantName, plant.getName());
    }

    @Test
    void insertOwnedPlant() {
        // Act
        OwnedPlant ownedPlant = Repositories.getH2Repo().insertOwnedPlant(1, 1, Constants.Status.FUTURE);

        // Assert
        Assertions.assertNotNull(ownedPlant);
    }

    @Test
    void insertScan() {
        // Act
        Date date = new Date();
        Scan scan = Repositories.getH2Repo().insertScan(1, date, 50, 50, 50, 50,50);

        // Assert
        Assertions.assertNotNull(scan);
    }

    @Test
    void getGarden() {
        // Arrange
        int id = 1;

        // Act
        Garden garden = Repositories.getH2Repo().getGarden(1);

        // Assert
        Assertions.assertNotNull(garden);
        Assertions.assertEquals(id, garden.getId());
        assertEquals(Constants.Tier.FREE, garden.getTier());
    }

    @Test
    void insertGarden() {
        // Act
        Garden garden = Repositories.getH2Repo().insertGarden("pol", Constants.Tier.FREE);

        // Assert
        Assertions.assertNotNull(garden);
    }

    @Test
    void getOwnedPlantsByGarden() {
        // Act
        Garden garden = Repositories.getH2Repo().getGarden(1);

        // Assert
        Assertions.assertTrue(garden.getPlants().size() > 0);
    }

    @Test
    void getScansByOwnedPlant() {
        // Act
        Garden garden = Repositories.getH2Repo().getGarden(1);

        // Assert
        Assertions.assertTrue(garden.getPlants().get(0).getScans().size() > 0);
    }

    @Test
    void getForumPostByGarden() {
        // Act
        Garden garden = Repositories.getH2Repo().getGarden(1);

        // Assert
        Assertions.assertTrue(garden.getPosts().size() > 0);
    }

    @Test
    void getRecentForumPosts() {
        // Act
        List<ForumPost> posts = Repositories.getH2Repo().getRecentForumPosts();

        // Assert
        Assertions.assertTrue(posts.size() > 0);
        Assertions.assertTrue(posts.get(0).getUploadTime().after(posts.get(1).getUploadTime()));
    }

    @Test
    void getForumPostById() {
        // Arrange
        int id = 1;

        // Act
        ForumPost forumPost = Repositories.getH2Repo().getForumPost(id);

        // Assert
        Assertions.assertNotNull(forumPost);
    }

    @Test
    void getLikesFromPost() {
        // Arrange
        int postId = 1;
        int count = 0;

        // Assert
        Assertions.assertEquals(count, Repositories.getH2Repo().getLikes(postId, "select count(*) as likes from likedPosts where postId = ?;"));
    }

    @Test
    void getComments() {
        // Arrange
        int postId = 1;

        // Act
        List<Comment> comments = Repositories.getH2Repo().getComments(postId);

        // Assert
        Assertions.assertEquals(1, comments.get(0).getId());
    }

    @Test
    void insertForumPost() {
        // Arrange
        String text = "yolo im going vegan";
        int gardenId = 2;

        // Act
        ForumPost forumPost = Repositories.getH2Repo().insertForumPost(
                gardenId,
                text,
                "https:veganofil.be",
                new Date());

        // Assert
        Assertions.assertNotNull(forumPost);
        Assertions.assertEquals(text, forumPost.getText());
        Assertions.assertEquals(gardenId, forumPost.getGarden().getId());
    }

    @Test
    void getAllPlants() {
        // Arrange & Act
        Plant testPlant = new Plant("avocado", "https:example.com");
        testPlant.setId(1);

        List<Plant> plantsList = Repositories.getH2Repo().getAllPlants();

        // Assert
        Assertions.assertNotNull(plantsList);
        Assertions.assertEquals(plantsList.get(0), testPlant);
    }

    @Test
    void updateGardenTier() {
        // Arrange
        int id = 1;

        // Act
        Garden updatedGarden = Repositories.getH2Repo().updateGardenTier(id, Constants.Tier.PREMIUM);

        // Assert
        Assertions.assertNotNull(updatedGarden);
        Assertions.assertNotEquals(Constants.Tier.FREE, updatedGarden.getTier());
    }

    @Test
    void getAllGardens() {
        // Arrange & Act
        Garden testGarden = new MockMarsController().getGarden(1);
        List<Garden> gardens = Repositories.getH2Repo().getAllGardens();

        // Assert
        Assertions.assertNotNull(gardens);
        Assertions.assertEquals(gardens.get(0), testGarden);
    }

    @Test
    void insertPostLike() {
        // Arrange
        int gardenId = 2;
        int postId = 2;

        // Assert
        Assertions.assertAll(() -> Repositories.getH2Repo().insertPostLike(postId, gardenId));
    }

    @Test
    void insertPostLikeDuplicate() {
        // Arrange
        int gardenId = 2;
        int postId = 2;

        // Assert
        Assertions.assertAll(() -> Repositories.getH2Repo().insertPostLike(postId, gardenId));
        Assertions.assertThrows(RepositoryException.class, () -> Repositories.getH2Repo().insertPostLike(postId, gardenId));
    }

    @Test
    void insertCommentLike() {
        // Arrange
        int gardenId = 2;
        int commentId = 2;

        // Act+Assert
        Assertions.assertAll(() -> Repositories.getH2Repo().insertCommentLike(commentId, gardenId));
    }

    @Test
    void insertCommentLikeDuplicate() {
        // Arrange
        int gardenId = 2;
        int commentId = 2;

        // Assert
        Assertions.assertAll(() -> Repositories.getH2Repo().insertCommentLike(commentId, gardenId));
        Assertions.assertThrows(RepositoryException.class, () -> Repositories.getH2Repo().insertCommentLike(commentId, gardenId));
    }

    @Test
    void getCommentGardenId() {
        // Arrange  & act
        int gardenId = Repositories.getH2Repo().getCommentGardenId(1);

        // Assert
        Assertions.assertEquals(1, gardenId);
    }

    @Test
    void insertComment() {
        // Arrange
        String text = "wauw zo mooi";

        // Act
        Comment comment = Repositories.getH2Repo().insertComment(1, 1, text);

        // Assert
        Assertions.assertNotNull(comment);
        Assertions.assertEquals(text, comment.getText());
    }

    @Test
    void insertRobot() {
        // Arrange
        String description = "Workhorse robot";

        // Act
        Robot robot = Repositories.getH2Repo().insertRobot("Paul's robot", 1, description);

        // Assert
        Assertions.assertNotNull(robot);
        Assertions.assertEquals(description, robot.getDescription());
    }

    @Test
    void getOwnedRobotsByGardenId() {
        // Arrange
        int gardenId = 1;
        String description = "Workhorse robot";


        // Act
        Robot robot = Repositories.getH2Repo().insertRobot("Paul's robot", 1, description);
        Garden garden = Repositories.getH2Repo().getGarden(gardenId);

        // Assert
        Assertions.assertNotNull(garden.getRobots());
        Assertions.assertTrue(garden.getRobots().size() > 1);
    }

    @Test
    void harvest() {
        // Act
        Repositories.getH2Repo().harvest(1);

        // assert
        assertEquals(1, Repositories.getH2Repo().getGarden(1).getPlants().size());
    }
}
