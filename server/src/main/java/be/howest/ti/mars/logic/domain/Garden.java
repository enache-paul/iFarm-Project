package be.howest.ti.mars.logic.domain;

import be.howest.ti.mars.logic.controller.DefaultMarsController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Garden {
    private final int id;
    private String name;
    private Constants.Tier tier;
    private List<ForumPost> posts = new ArrayList<>();
    private List<OwnedPlant> plants = new ArrayList<>();
    private List<Robot> robots = new ArrayList<>();
    private List<MarketplaceAd> marketplaceAds = new ArrayList<>();

    public Garden(int id, String name, Constants.Tier tier) {
        this.id = id;
        this.name = name;
        this.tier = tier;
    }
    public Garden(int id, String name, Constants.Tier tier, List<ForumPost> posts,
                  List<OwnedPlant> plants, List<Robot> robots, List<MarketplaceAd> marketplaceAds) {
        this(id, name, tier);
        this.posts = posts;
        this.plants = plants;
        this.robots = robots;
        this.marketplaceAds = marketplaceAds;
    }
    public Garden(int id) {
        this.id = id;
    }

    public Garden(int gardenId, Constants.Tier tier) {
        this.id = gardenId;
        this.tier = tier;
    }

    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public Constants.Tier getTier() {
        return tier;
    }
    public List<ForumPost> getPosts() {
        return posts;
    }
    public List<OwnedPlant> getPlants() {
        return plants;
    }
    public List<MarketplaceAd> getMarketplaceAds() {return marketplaceAds;}
    public List<Robot> getRobots() {return robots;}
    public void changeTier(Constants.Tier tier) {
        this.tier = tier;
    }
    public void addPost(String text, String imgLink) {
        // insert into database and get id (via repository interface)
        int newId = 1;
        Date uploadTime = new Date();
        posts.add(new ForumPost(newId, this, text, imgLink, uploadTime, 0, new ArrayList<>()));
    }
    public void addPlant(Plant plant, Constants.Status status) {
        OwnedPlant newPlant = DefaultMarsController.getInstance().createOwnedPlant(plant.getId(), id, status);
        plants.add(newPlant);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Garden)) return false;
        Garden garden = (Garden) o;
        return id == garden.id;
    }
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
