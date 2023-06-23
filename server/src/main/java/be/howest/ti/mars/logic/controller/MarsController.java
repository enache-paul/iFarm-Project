package be.howest.ti.mars.logic.controller;

import be.howest.ti.mars.logic.domain.Garden;
import be.howest.ti.mars.logic.domain.Plant;
import be.howest.ti.mars.logic.domain.*;

import java.util.List;

public interface MarsController {
    Plant getPlant(int plantId);

    Plant createPlant(String platName, String imgLink);

    Scan createScan(int gardenId, int ownedPlantId);

    OwnedPlant createOwnedPlant(int plantId, int gardenId, Constants.Status status);

    Garden getGarden(int gardenId);

    Garden createGarden(String name, Constants.Tier tier);

    ForumPost getForumPost(int forumPostId);

    List<ForumPost> getRecentForumPosts();

    ForumPost createForumPost(int gardenId, String text, String imgLink);

    List<Plant> getAllPlants();

    Garden updateGardenTier(int gardenId, Constants.Tier tier);

    void fillGardens();

    Garden getBasicGarden(int id);

    int createPostLike(int gardenId, int postId);

    int createCommentLike(int gardenId, int commentId);

    Comment createComment(int gardenId, int postId, String text);
    Robot createRobot(String robotName, int gardenId, String description);

    void harvest(int ownedPlantId, int gardenId);
}
