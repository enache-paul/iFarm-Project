package be.howest.ti.mars.logic.domain;

import be.howest.ti.mars.logic.data.Repositories;
import io.vertx.core.json.JsonObject;
import org.junit.Assert;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BasicClassTests {
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
    void changeTier() {
        Garden garden = new Garden(1, "bob", Constants.Tier.FREE);

        garden.changeTier(Constants.Tier.PREMIUM);

        Assertions.assertEquals(Constants.Tier.PREMIUM, garden.getTier());
    }

    @Test
    void addPost() {
        Garden garden = new Garden(1, "bob", Constants.Tier.FREE);

        ForumPost post = new ForumPost(1, garden, "Look at my big tomato", "tomato.png", new Date(), 0, new ArrayList<>());
        garden.addPost("Look at my big tomato", "tomato.png");

        Assertions.assertNotEquals(0, garden.getPosts().size());
        Assertions.assertEquals(post, garden.getPosts().get(0));
    }

    @Test
    void addPlant() {
        Garden garden = new Garden(1, "bob", Constants.Tier.FREE);

        Plant basePlant = new Plant(1, "tomato", "tomato.png");
        OwnedPlant plant = new OwnedPlant(1, basePlant, garden, Constants.Status.FUTURE);
        garden.addPlant(basePlant, Constants.Status.FUTURE);

        Assertions.assertEquals(1, garden.getPlants().size());
    }

    @Test
    void Comment() {
        // Arrange
        int id = 1;
        ForumPost post = new ForumPost(1, null, " ", " ", new Date(), 1, new ArrayList<>());
        Garden garden = new Garden(1, "garden", Constants.Tier.FREE);
        String text = "text";
        int likes = 2;

        // Act
        Comment comment = new Comment(id, post, garden, text, likes);

        // Assert
        Assertions.assertEquals(id, comment.getId());
        Assertions.assertEquals(post, comment.getPost());
        Assertions.assertEquals(garden, comment.getGarden());
        Assertions.assertEquals(text, comment.getText());
        Assertions.assertEquals(likes, comment.getLikes());
    }

    @Test
    void MarketplaceAd() {
        // Arrange
        int id = 1;
        Plant plant = new Plant(1, " ", " ");
        String description = "epic";
        int quantity = 2;
        Constants.AdStatus status = Constants.AdStatus.LIVE;
        Date date = new Date();
        String imgLink = " ";

        // Act
        MarketplaceAd ad = new MarketplaceAd(id, plant, description, quantity, status, date);
        ad.setImg(imgLink);

        // Assert
        Assertions.assertEquals(id, ad.getId());
        Assertions.assertEquals(plant, ad.getPlant());
        Assertions.assertEquals(description, ad.getDescription());
        Assertions.assertEquals(quantity, ad.getQuantity());
        Assertions.assertEquals(status, ad.getStatus());
        Assertions.assertEquals(date, ad.getUploadTime());
        Assertions.assertEquals(imgLink, ad.getImgLink());
    }

    @Test
    void OwnedPlant() {
        // Arrange
        int id = 1;
        int plantId = 2;
        String name = "name";
        String imgLink = "link";
        Garden garden = new Garden(1, "garden", Constants.Tier.FREE);
        Constants.Status status = Constants.Status.FUTURE;
        List<Scan> scans = new ArrayList<>();

        // Act
        OwnedPlant plant = new OwnedPlant(id, plantId, name, imgLink, garden, status, scans);

        // Assert
        Assertions.assertEquals(id, plant.getId());
        Assertions.assertEquals(name, plant.getName());
        Assertions.assertEquals(imgLink, plant.getImgLink());
        Assertions.assertEquals(garden, plant.getGarden());
        Assertions.assertEquals(status, plant.getStatus());
        Assertions.assertEquals(scans, plant.getScans());
    }

    @Test
    void Robot() {
        // Arrange
        String name = "robot";
        Garden garden = new Garden(1, "garden", Constants.Tier.FREE);
        String description = "description";
        int batteryLevel = 10;

        // Act
        Robot robot = new Robot(name, garden, description, batteryLevel);
        robot.setDescription(description);

        // Assert
        Assertions.assertEquals(name, robot.getName());
        Assertions.assertEquals(garden, robot.getGarden());
        Assertions.assertEquals(description, robot.getDescription());
        Assertions.assertEquals(batteryLevel, robot.getBatteryLevel());
        Assertions.assertEquals(1, robot.getGardenId());
    }

    @Test
    void Scan() {
        // Arrange
        OwnedPlant plant = new OwnedPlant(1, new Plant(1, " ", " "), Constants.Status.FUTURE);
        Date date = new Date();
        int fl = 10;
        int wl = 11;
        int ey = 12;
        int health = 13;
        int growth = 14;

        // Act
        Scan scan = new Scan(plant, date, fl, wl, ey, health, growth);

        // Assert
        Assertions.assertEquals(plant, scan.getPlant());
        Assertions.assertEquals(date, scan.getScanTime());
        Assertions.assertEquals(fl, scan.getFertilizerLevel());
        Assertions.assertEquals(wl, scan.getWaterLevel());
        Assertions.assertEquals(ey, scan.getExpectedYield());
        Assertions.assertEquals(health, scan.getHealth());
        Assertions.assertEquals(growth, scan.getGrowth());
    }
}
