package be.howest.ti.mars.logic.controller;

import be.howest.ti.mars.logic.domain.Garden;
import be.howest.ti.mars.logic.domain.Plant;
import be.howest.ti.mars.logic.domain.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MockMarsController implements MarsController {
    @Override
    public Plant getPlant(int plantId) {
        return new Plant(plantId, "avocado", "https");
    }

    @Override
    public Plant createPlant(String plantName, String imgLink) {
        return new Plant(1, plantName, imgLink);
    }

    @Override
    public Scan createScan(int gardenId, int ownedPlantId) {
        OwnedPlant plant = new OwnedPlant(ownedPlantId, 1, "tomato", "tomato.png",
                new Garden(1, "bob",  Constants.Tier.FREE), Constants.Status.FUTURE);
        Date date = new Date();
        return new Scan(plant, date, 50, 50, 50, 50, 50);
    }

    @Override
    public OwnedPlant createOwnedPlant(int plantId, int gardenId, Constants.Status status) {
        return new OwnedPlant(1, plantId, "tomato", "tomato.png", new Garden(gardenId, "bob", Constants.Tier.FREE), status);
    }

    @Override
    public Garden getGarden(int gardenId) {
        return new Garden(gardenId, "bob", Constants.Tier.FREE);
    }

    @Override
    public Garden createGarden(String name, Constants.Tier tier) {
        return new Garden(1, name, tier);
    }

    @Override
    public ForumPost getForumPost(int forumPostId) {
        return new ForumPost(forumPostId, getGarden(1),"check out my totatoes" ,"https://google.com", new Date(210102), 0, new ArrayList<>());
    }

    @Override
    public List<ForumPost> getRecentForumPosts() {
        List<ForumPost> posts = new ArrayList<>();
        ForumPost post1 = createForumPost(1, "hoi", "example.com");
        ForumPost post2 = createForumPost(1, "hallo", "example.com");
        posts.add(post2);
        posts.add(post1);

        return posts;
    }

    @Override
    public ForumPost createForumPost(int gardenId, String text, String imgLink) {
        return new ForumPost(-1, new Garden(gardenId), text, imgLink, new Date(), 0, new ArrayList<>());
    }

    @Override
    public List<Plant> getAllPlants() {
        List<Plant> plantsList = new ArrayList<>();
        plantsList.add(getPlant(1));
        return plantsList;
    }

    @Override
    public Garden updateGardenTier(int gardenId, Constants.Tier tier) {
        return new Garden(gardenId, tier);
    }

    @Override
    public void fillGardens() {

    }

    @Override
    public Garden getBasicGarden(int id) {
        return getGarden(id);
    }

    @Override
    public int createPostLike(int gardenId, int postId) {
        return 1;
    }

    @Override
    public int createCommentLike(int gardenId, int commentId) {
        return 1;
    }

    @Override
    public Comment createComment(int gardenId, int postId, String text) {
        return new Comment(10, createForumPost(1, "no", "no")
                ,createGarden("john", Constants.Tier.FREE), text, 0);
    }

    @Override
    public Robot createRobot(String robotName, int gardenId, String description) {
        return new Robot(robotName,description, 49);
    }

    @Override
    public void harvest(int ownedPlantId, int gardenId) {

    }
}
