package be.howest.ti.mars.logic.controller;

import be.howest.ti.mars.logic.data.Repositories;
import be.howest.ti.mars.logic.domain.Garden;
import be.howest.ti.mars.logic.domain.Plant;
import be.howest.ti.mars.logic.domain.*;
import be.howest.ti.mars.logic.exceptions.RepositoryException;
import be.howest.ti.mars.web.exceptions.MalformedRequestException;
import io.vertx.core.json.JsonObject;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DefaultMarsControllerTest {

    private static final String URL = "jdbc:h2:./db-23";

    @BeforeAll
    void setupTestSuite() {
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
    void getPlant() {
        // Act
        Plant plant = DefaultMarsController.getInstance().getPlant(1);

        //Assert
        assertTrue(plant != null && StringUtils.isNoneBlank(plant.getName()));
    }

    @Test
    void getPlantWithIdNotFound() {
        //Assert
        assertThrows(NoSuchElementException.class, () -> DefaultMarsController.getInstance().getPlant(-1));
    }

    @Test
    void createPlant() {
        // Act
        Plant plant = DefaultMarsController.getInstance().createPlant("Floarea Soarelui", "https://lfwof.com");

        // Assert
        assertEquals("Floarea Soarelui", plant.getName());
    }

    @Test
    void createOwnedPlant() {
        // Act
        OwnedPlant ownedPlant = DefaultMarsController.getInstance().createOwnedPlant(1, 1, Constants.Status.FUTURE);

        // Assert
        assertNotNull(ownedPlant);
    }

    @Test
    void createOwnedPlantInvalidIds() {
        // Arrange
        int plantId = -1;
        int gardenId = -1;

        // Assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> DefaultMarsController.getInstance()
                .createOwnedPlant(plantId, gardenId, Constants.Status.FUTURE));
    }

    @Test
    void createScan() {
        // Arrange
        DefaultMarsController.getInstance().fillGardens();

        // Act
        Scan scan = DefaultMarsController.getInstance().createScan(1, 1);

        // Assert
        assertNotNull(scan);
    }

    @Test
    void getGardenWithNoIdFound() {
        //Assert
        assertThrows(NoSuchElementException.class, () -> DefaultMarsController.getInstance().getGarden(-1));
        Garden garden = DefaultMarsController.getInstance().getGarden(1);

        // Assert
        assertTrue(garden != null && garden.getId() == 1);
        assertEquals(Constants.Tier.FREE, garden.getTier());

    }

    @Test
    void getGarden() {
        // Act
        Garden garden = DefaultMarsController.getInstance().getGarden(1);

        //Assert
        assertNotNull(garden);
    }

    @Test
    void createGarden() {
        // Act
        Garden garden = DefaultMarsController.getInstance().createGarden("bobby", Constants.Tier.FREE);

        // Assert
        assertNotNull(garden);
    }

    @Test
    void getForumPost() {
        // Act
        ForumPost forumPost = DefaultMarsController.getInstance().getForumPost(1);

        // Assert
        assertNotNull(forumPost);
        assertEquals(1, forumPost.getId());
        assertEquals(1, forumPost.getGarden().getId());
    }

    @Test
    void getRecentForumPosts() {
        // Act
        List<ForumPost> posts = DefaultMarsController.getInstance().getRecentForumPosts();

        // Arrange
        assertNotNull(posts);
        Assertions.assertTrue(posts.get(0).getUploadTime().after(posts.get(1).getUploadTime()));
    }

    @Test
    void createForumPost() {
        // Act
        ForumPost forumPost = DefaultMarsController.getInstance().createForumPost(
                2,
                "Everybody, take a look at my cucumber!!",
                "https://google.com");

        //Assert
        assertNotNull(forumPost);
    }

    @Test
    void createForumPostWithInvalidGardenId() {
        // Assert & Act
        assertThrows(RepositoryException.class, () -> DefaultMarsController.getInstance().createForumPost(
                10000334,
                "Everybody, take a look at my cucumber!!",
                "https://google.com"));
    }

    @Test
    void getAllPlants() {
        // Act
        List<Plant> plantList = DefaultMarsController.getInstance().getAllPlants();
        // Assert
        assertNotNull(plantList);
        assertEquals(1, plantList.get(0).getId());
        assertEquals(3, plantList.get(2).getId());
    }

    @Test
    void getBasicGarden() {
        // Act
        Garden garden = DefaultMarsController.getInstance().getBasicGarden(1);

        // Assert
        assertNotNull(garden);
        assertEquals(1, garden.getId());
    }

    @Test
    void createPostLike() {
        int likes = DefaultMarsController.getInstance().getForumPost(2).getLikes();

        // Assert
        Assertions.assertAll(() -> DefaultMarsController.getInstance().createPostLike(2, 2));
        Assertions.assertEquals(likes + 1, DefaultMarsController.getInstance().getForumPost(2).getLikes());
    }

    @Test
    void createCommentLike() {
        // Assert
        Assertions.assertAll(() -> DefaultMarsController.getInstance().createCommentLike(2, 2));
    }

    @Test
    void createComment() {
        // Arrange
        String text = "wauw zo mooi";

        // Act
        Comment comment = DefaultMarsController.getInstance().createComment(1, 1, text);

        // Assert
        Assertions.assertNotNull(comment);
        Assertions.assertEquals(text, comment.getText());
    }

    @Test
    void createRobot() {
        // Arrange
        String name = "Unforgiven Wood";

        // Act
        Robot robot = DefaultMarsController.getInstance().createRobot(name, 1, "Good description");

        // Assert
        Assertions.assertNotNull(robot);
        Assertions.assertEquals(name, robot.getName());
    }

    @Test
    void harvest() {
        // Arrange
        DefaultMarsController.getInstance().fillGardens();

        // Act
        DefaultMarsController.getInstance().harvest(1, 1);

        // Assert
        Assertions.assertEquals(1, DefaultMarsController.getInstance().getGarden(1).getPlants().size());
    }

    @Test
    void harvestWrongId() {
        // Arrange
        DefaultMarsController.getInstance().fillGardens();

        // Act
        Assertions.assertThrows(MalformedRequestException.class, () -> DefaultMarsController.getInstance().harvest(1, 2));
    }
 }
