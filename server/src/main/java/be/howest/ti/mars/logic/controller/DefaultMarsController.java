package be.howest.ti.mars.logic.controller;

import be.howest.ti.mars.logic.data.Repositories;
import be.howest.ti.mars.logic.domain.Garden;
import be.howest.ti.mars.logic.domain.Plant;
import be.howest.ti.mars.logic.domain.*;
import be.howest.ti.mars.web.exceptions.MalformedRequestException;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * DefaultMarsController is the default implementation for the MarsController interface.
 * The controller shouldn't even know that it is used in an API context..
 *
 * This class and all other classes in the logic-package (or future sub-packages)
 * should use 100% plain old Java Objects (POJOs). The use of Json, JsonObject or
 * Strings that contain encoded/json data should be avoided here.
 * Keep libraries and frameworks out of the logic packages as much as possible.
 * Do not be afraid to create your own Java classes if needed.
 */
public class DefaultMarsController implements MarsController {
    private static final DefaultMarsController instance = new DefaultMarsController();

    private static final String MSG_PLANT_ID_UNKNOWN = "No plant with id: %d";
    private static final String MSG_GARDEN_ID_UNKNOWN = "No garden with id: %d";
    private static final String MSG_FORUM_POST_ID_UNKNOWN = "No forum post with id: %d";
    private List<Garden> gardens = new ArrayList<>();

    private DefaultMarsController() {}
    public static DefaultMarsController getInstance() {
        return instance;
    }

    @Override
    public Plant getPlant(int plantId) {
        Plant plant = Repositories.getH2Repo().getPlant(plantId);
        if (null == plant)
            throw new NoSuchElementException(String.format(MSG_PLANT_ID_UNKNOWN, plantId));

        return plant;
    }

    @Override
    public Plant createPlant(String plantName, String imgLink) {
        if (StringUtils.isBlank(plantName) || StringUtils.isBlank(imgLink))
            throw new IllegalArgumentException("An empty field is not allowed.");

        return Repositories.getH2Repo().insertPlant(plantName, imgLink);
    }

    @Override
    public Scan createScan(int gardenId, int ownedPlantId) {
        if (!gardenContainsPlant(gardenId, ownedPlantId))
            throw new MalformedRequestException("Garden doesn't contain this plant");

        Scan latest = getNewestScan(ownedPlantId);
        Date scanTime = new Date();
        int fertilizerLevel = latest.getFertilizerLevel() - 1;
        int waterLevel = latest.getWaterLevel() - 1;
        int expectedYield = latest.getExpectedYield() + new Random().nextInt(10);
        int health = latest.getHealth() + 1;
        int growth = latest.getGrowth() + 33;

        return Repositories.getH2Repo().insertScan(ownedPlantId, scanTime, fertilizerLevel, waterLevel, expectedYield, health, growth);
    }
    
    private Scan getNewestScan(int ownedPlantId) {
        List<Scan> scans = Repositories.getH2Repo().getScansByOwnedPlant(ownedPlantId);
        if (!scans.isEmpty()) {
            scans.sort(Comparator.comparing(Scan::getScanTime));
            return scans.get(scans.size() - 1);
        }
        return new Scan(new Date(), 50, 50, 50, 50, 0 );
    }

    @Override
    public Garden getGarden(int gardenId) {
        gardens = Repositories.getH2Repo().getAllGardens();

        Garden garden = Repositories.getH2Repo().getGarden(gardenId);
        if (null == garden)
            throw new NoSuchElementException(String.format(MSG_GARDEN_ID_UNKNOWN, gardenId));
        return garden;
    }

    @Override
    public Garden createGarden(String name, Constants.Tier tier) {
        Garden garden = Repositories.getH2Repo().insertGarden(name, tier);
        gardens.add(garden);
        return garden;
    }

    @Override
    public OwnedPlant createOwnedPlant(int plantId, int gardenId, Constants.Status status) {
        if (plantId == -1 || gardenId == -1)
            throw new IllegalArgumentException("Plant and garden id's must be valid.");

        return Repositories.getH2Repo().insertOwnedPlant(plantId, gardenId, status);
    }

    @Override
    public ForumPost getForumPost(int forumPostId) {
        ForumPost forumPost = Repositories.getH2Repo().getForumPost(forumPostId);
        if (null == forumPost)
            throw new NoSuchElementException(String.format(MSG_FORUM_POST_ID_UNKNOWN, forumPostId));
        return forumPost;
    }

    @Override
    public List<ForumPost> getRecentForumPosts() {
        return Repositories.getH2Repo().getRecentForumPosts();
    }

    @Override
    public ForumPost createForumPost(int gardenId, String text, String imgLink) {
        if (gardenId == -1)
            throw new IllegalArgumentException(String.format(MSG_GARDEN_ID_UNKNOWN, gardenId));

        Date uploadTime = new Date();
        return Repositories.getH2Repo().insertForumPost(gardenId, text, imgLink, uploadTime);
    }

    @Override
    public List<Plant> getAllPlants() {
        return Repositories.getH2Repo().getAllPlants();
    }

    @Override
    public Garden updateGardenTier(int gardenId, Constants.Tier tier) {
        if (gardenId == -1)
            throw new IllegalArgumentException(String.format(MSG_GARDEN_ID_UNKNOWN, gardenId));

        return Repositories.getH2Repo().updateGardenTier(gardenId, tier);
    }

    private Boolean gardenContainsPlant(int gardenId, int plantId) {
        return gardens.stream()
                .filter(g -> g.getId() == gardenId).collect(Collectors.toList()).get(0)
                .getPlants().stream().anyMatch(ownedPlant -> ownedPlant.getId() == plantId);
    }

    @Override
    public void fillGardens() {
        gardens = Repositories.getH2Repo().getAllGardens();
    }

    @Override
    public Garden getBasicGarden(int id) {
        return Repositories.getH2Repo().getBasicGarden(id);
    }

    @Override
    public int createPostLike(int gardenId, int postId) {
        Repositories.getH2Repo().insertPostLike(postId, gardenId);
        return Repositories.getH2Repo().getForumPost(postId).getGarden().getId();
    }

    @Override
    public int createCommentLike(int gardenId, int commentId) {
        Repositories.getH2Repo().insertCommentLike(commentId, gardenId);
        return Repositories.getH2Repo().getCommentGardenId(commentId);
    }

    @Override
    public Comment createComment(int postId, int gardenId, String text) {
        return Repositories.getH2Repo().insertComment(postId, gardenId, text);
        // somehow unicast to post owner for notification
    }

    @Override
    public Robot createRobot(String robotName, int gardenId, String description) {
        return Repositories.getH2Repo().insertRobot(robotName, gardenId, description);
    }

    @Override
    public void harvest(int ownedPlantId, int gardenId) {
        fillGardens();
        Garden garden = gardens.stream().filter(g -> g.getId() == gardenId).findAny().orElse(null);
        assert garden != null;
        if (garden.getPlants().stream().anyMatch(plant -> plant.getId() == ownedPlantId)) {
            Repositories.getH2Repo().harvest(ownedPlantId);
        } else {
            throw new MalformedRequestException("this garden doesn't own this plant");
        }
    }
}