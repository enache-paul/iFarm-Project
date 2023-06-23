package be.howest.ti.mars.logic.domain;

import java.util.Date;
import java.util.Objects;

public class MarketplaceAd {
    private final int id;
    private Garden garden;
    private final Plant plant;
    private final String description;
    private final int quantity;
    private final Constants.AdStatus status;
    private final Date uploadTime;
    private String imgLink;


    public MarketplaceAd(int id, Plant plant, String description, int quantity, Constants.AdStatus status, Date uploadTime) {
        this.id = id;
        this.plant = plant;
        this.description = description;
        this.quantity = quantity;
        this.status = status;
        this.uploadTime = uploadTime;
        this.imgLink = "https://static9.depositphotos.com/1628352/1107/i/950/depositphotos_11071361-stock-photo-tomato.jpg"; // random example
    }
    public MarketplaceAd(int id, Garden garden, Plant plant, String description, int quantity, Constants.AdStatus status, Date uploadTime) {
        this(id, plant, description, quantity,status,uploadTime);
        this.garden = garden;
    }


    public int getId() {
        return id;
    }
    public Garden getGarden() {
        return garden;
    }
    public Plant getPlant() {
        return plant;
    }
    public String getDescription() {
        return description;
    }
    public int getQuantity() {
        return quantity;
    }
    public Constants.AdStatus getStatus() {
        return status;
    }
    public Date getUploadTime() {
        return uploadTime;
    }
    public String getImgLink() {
        return imgLink;
    }

    public void setImg(String imgLink) {
        this.imgLink = imgLink;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MarketplaceAd)) return false;
        MarketplaceAd that = (MarketplaceAd) o;
        return id == that.id;
    }
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
